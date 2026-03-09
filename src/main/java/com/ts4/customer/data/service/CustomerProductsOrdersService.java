package com.ts4.customer.data.service;

import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netflix.discovery.converters.Auto;
import com.ts4.customer.data.client.*;
import com.ts4.customer.data.exception.customerror.HttpStatusErrorCustom;
import com.ts4.customer.data.model.OauthModel;
import com.ts4.customer.data.model.comercialstructure.Datumcomercial;
import com.ts4.customer.data.model.comercialstructure.OutputComercialStructure;
import com.ts4.customer.data.model.comercialstructure.ParentCategoryTree;
import com.ts4.customer.data.model.comprarnuevo.Datum;
import com.ts4.customer.data.model.comprarnuevo.OutputComprarNuevo;
import com.ts4.customer.data.model.comprarnuevo.Refinement;
import com.ts4.customer.data.model.comprarnuevo.SortingOption;
import com.ts4.customer.data.model.order.search.Hit;
import com.ts4.customer.data.model.order.search.InputOrderSearch;
import com.ts4.customer.data.model.order.search.OutputOrderSearch;
import com.ts4.customer.data.model.order.search.ProductItem;
import com.ts4.customer.data.model.splitshipment.InputSplitShipment;
import com.ts4.customer.data.model.splitshipment.OutputSplitShipment;
import com.ts4.customer.data.model.splitshipment.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts4.customer.data.exception.ExceptionResponseGeneral;
import com.ts4.customer.data.exception.customerror.ErrorResponse;
import com.ts4.customer.data.exception.ocapi.ErrorOcapi;
import com.ts4.customer.data.model.mockrecomender.response.RecommenderCarrouselGeneralResp;
import com.ts4.customer.data.model.mockrecomender.response.RecommenderCarrouselResp;
import com.ts4.customer.data.model.orderproducts.request.ProductsOrderReq;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CustomerProductsOrdersService {
	
	@Autowired private Environment env;
    @Autowired private ProcuctsSearchMiddleware procuctsSearchMiddleware;
	@Autowired private ResourceLoader resourceLoader;
	@Autowired private OrderSearchClient orderSearchClient;
	@Autowired private OauthServiceBM oauthServiceBM;
	@Autowired private SplittShipmentClient splittShipmentClient;
	@Autowired private CommercialStructureClient commercialStructureClient;
	@Value("${cityclub.url.findproducts}")
	private String findProductsUrl;
	private String jsonString = "{"
			+ "\"query\": {"
			+ "    \"bool_query\": {"
			+ "        \"must\": ["
			+ "            {"
			+ "                \"term_query\": {"
			+ "                    \"fields\": ["
			+ "                        \"customer_no\""
			+ "                    ],"
			+ "                    \"operator\": \"is\","
			+ "                    \"values\": ["
			+ "                        \"DEV_00172395\""
			+ "                    ]"
			+ "                }"
			+ "            },"
			+ "            {"
			+ "                \"term_query\": {"
			+ "                    \"fields\": ["
			+ "                        \"c_orderStatus\""
			+ "                    ],"
			+ "                    \"operator\": \"is\","
			+ "                    \"values\": ["
			+ "                        \"delivered\""
			+ "                    ]"
			+ "                }"
			+ "            }"
			+ "        ]"
			+ "    }"
			+ "},"
			+ "\"select\": \"(**)\","
			+ "\"sorts\": ["
			+ "    {"
			+ "        \"field\": \"creation_date\","
			+ "        \"sort_order\": \"desc\""
			+ "    }"
			+ "],"
			+ "\"count\": 10"
			+ "}";

  // @Autowired private OrderSearchClient orderSearchClient;
       
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        
    @PostConstruct
    public void init() {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }
    
	public ResponseEntity<?> getProductsOrders(  String authorization, String idCustomer, ProductsOrderReq productsOrderReq,Boolean homeReq) throws JsonProcessingException {
	    
	    String jsonParams = OBJECT_MAPPER.writeValueAsString(productsOrderReq);  

	    System.out.println("LEYENDO DATOS DE ENTRADA == "+jsonParams);
	    System.out.println("AUTHORIZATION =="+authorization);	  
	    System.out.println("ID CUSTOMER=== "+idCustomer);
		Double subtotalOrden = 0d;

        ResponseEntity<?> responseEntity=null;
        try {
			OauthModel modelOauth = oauthServiceBM.getOauthEntity();
			if( Optional.ofNullable(modelOauth).isEmpty()
					||	Optional.ofNullable(modelOauth.accessToken()).isEmpty()   ) {
				log.info("### RESPONSE ENETITY GET getCampaigns");
				ErrorResponse err=new ErrorResponse(  HttpStatusErrorCustom.BAD_REQUEST.getCode(), "No se pudo realizar la autenticacion [BM]" , "get BM AUTENTICACION", null);
				log.error("ERROR get coupons");
				return new ResponseEntity<>(err ,HttpStatus.valueOf( err.getCode() ) );
			}
			Map<String,Product> products = new HashMap<>();
			String tokenBussinessManager="Bearer "+modelOauth.accessToken();
			List<String> values = Arrays.asList(productsOrderReq.getCustomerNo());
			InputOrderSearch inputOrderSearch = new Gson().fromJson(jsonString,InputOrderSearch.class);
			inputOrderSearch.getQuery().getBool_query().getMust().get(0).getTerm_query().setValues(values);
			System.err.println(new Gson().toJson(inputOrderSearch));
			ResponseEntity<OutputOrderSearch> orderSearch = orderSearchClient.getOrderSeachs(tokenBussinessManager,inputOrderSearch);

			if (orderSearch.getStatusCode().is2xxSuccessful()) {
				if (orderSearch.getBody()!= null && orderSearch.getBody().getHits()!=null && orderSearch.getBody().getHits().size()>0) {
					for (Hit hit : orderSearch.getBody().getHits()) {
						for (ProductItem product : hit.getData().getProduct_items()) {
							if (!product.getProduct_id().equals("3003973") || !product.getProduct_id().equals("3003984") || !product.getProduct_id().equals("3006636") || !product.getProduct_id().equals("3007224") || !product.getProduct_id().equals("3007349")) {
								if (!products.containsKey(product.getProduct_id())) {
									System.err.println(product.getProduct_id() + " -- " + product.getQuantity() + " -- " + product.getPrice());
									Product p = new Product();
									p.setBarcode(product.getProduct_id());
									p.setQuantity(product.getQuantity());
									p.setPrice(product.getPrice());
									products.put(product.getProduct_id(), p);
									subtotalOrden += product.getPrice();
								} else {
									products.get(product.getProduct_id()).setQuantity(products.get(product.getProduct_id()).getQuantity() + product.getQuantity());
								}
							}
						}
					}
				}
			}
			List<Product> list = new ArrayList<Product>(products.values());
			InputSplitShipment inputSplitShipment = new InputSplitShipment();
			inputSplitShipment.setProducts(list);
			inputSplitShipment.setStoreId(productsOrderReq.getStoreId());
			inputSplitShipment.setPostalCode(productsOrderReq.getPostalCode());
			inputSplitShipment.setDeliveryInStore(productsOrderReq.getDeliveryInStore());
			inputSplitShipment.setSubtotalOrder(Double.valueOf(String.format("%.2f", subtotalOrden)));
			inputSplitShipment.setAction("splitShipment");


			System.err.println("-------------");
			System.err.println(new Gson().toJson(inputSplitShipment));
			List<String> listaProductosFinal = new ArrayList<>();
			ResponseEntity<OutputSplitShipment> outputSplitShipment = splittShipmentClient.splitShipment(authorization,inputSplitShipment);
			if (outputSplitShipment.getStatusCode().is2xxSuccessful()) {
				if (outputSplitShipment.getBody() != null && outputSplitShipment.getBody().getMainGroupes() != null && outputSplitShipment.getBody().getMainGroupes().getGrocery().getShipment().getProducts().size() > 0) {
					for (Product p : outputSplitShipment.getBody().getMainGroupes().getGrocery().getShipment().getProducts()){
						listaProductosFinal.add(p.getBarcode());
					}
				}else{
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
			}else{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

    		//List<String >listadoIdsProductosConsulta=Arrays.asList("456,3048402,3109650,3132801,3143908,11693755,3139222,11782209,11698758,3138673".split(","));
			String query = "availability,images,prices,variations";
			int cantidadElementos = Math.min(listaProductosFinal.size(), 24);

			ResponseEntity<OutputComprarNuevo> responseGetProductByIdSFCC = getProductByIdSFCC(String.join(",", listaProductosFinal.subList(0, cantidadElementos)),authorization,homeReq);

			for (Datum datum: responseGetProductByIdSFCC.getBody().getData()) {
				if(products.containsKey(datum.getId())) {
					datum.setMdw_purchasedPartsProduct(products.get(datum.getId()).getQuantity());
				}
			}

			responseEntity = responseGetProductByIdSFCC;

        }catch (ExceptionResponseGeneral exceptionResponseGeneral) {
			log.error("ExceptionResponseGeneral  exceptionResponseGeneral "+OBJECT_MAPPER.writeValueAsString(exceptionResponseGeneral)); 			
			log.error("error getGenericCustomError " + exceptionResponseGeneral.getMessage());
			log.info("error exceptionResponseGeneral.getBody() "+exceptionResponseGeneral.getBody().toString());			
			
			System.out.println("ExceptionResponseGeneral  exceptionResponseGeneral "+OBJECT_MAPPER.writeValueAsString(exceptionResponseGeneral)); 			
			System.out.println("error getGenericCustomError " + exceptionResponseGeneral.getMessage());
			System.out.println("error exceptionResponseGeneral.getBody() "+exceptionResponseGeneral.getBody().toString());			
			
			
			Map objetoRespuesta = OBJECT_MAPPER.readValue(exceptionResponseGeneral.getBody(), Map.class);
			return new ResponseEntity<>(objetoRespuesta, HttpStatus.valueOf(exceptionResponseGeneral.getStatus())); 			
		} catch (Exception exception) {
			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
			responseEntity = new ResponseEntity<>(err,
					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
		}     
	    
		return responseEntity;
	}
    
	/*public ResponseEntity<?> getMockOrders(  String authorization, String idCustomer, ProductsOrderReq productsOrderReq) throws JsonProcessingException {
	    
	    String jsonParams = OBJECT_MAPPER.writeValueAsString(productsOrderReq);  

	    System.out.println("LEYENDO DATOS DE ENTRADA == "+jsonParams);
	    System.out.println("AUTHORIZATION =="+authorization);	  
	    System.out.println("ID CUSTOMER=== "+idCustomer);	   

        ResponseEntity<?> responseEntity=null;
        try {        
        	     			     			
    		List<String >listadoIdsProductosConsulta=Arrays.asList("3091664,3038274,1797143,3064627,3135308,3133587,3127576,3074034,3133166,3134337,3134338,3000747,3000748,3126777,3126946".split(","));
    		Map<String,Object>mapaParams=new HashMap<String,Object>();
    	    responseEntity = productsBasketClient.getProductsByIds(authorization, listadoIdsProductosConsulta, mapaParams);    	    
    		Object resultJsonRespuesta = responseEntity.getBody();
    		String json=OBJECT_MAPPER.writeValueAsString(resultJsonRespuesta);
    		
            Map respuestaMap = OBJECT_MAPPER.readValue(json, Map.class) ;
            
    	    List<Object> respuestaData = (List<Object>) respuestaMap.get("data");
    	    RecommenderCarrouselGeneralResp mockRespuesta = cargarMockReccomender();    
    	    mockRespuesta.getCarrousels().get(0).setData(respuestaData);
    	    mockRespuesta.getCarrousels().get(0).setCount(respuestaData.size());	
    	    mockRespuesta.getCarrousels().get(1).setData(respuestaData);
    	    mockRespuesta.getCarrousels().get(1).setCount(respuestaData.size());	

			System.out.println("mockRespuesta "+OBJECT_MAPPER.writeValueAsString(mockRespuesta));
			responseEntity=ResponseEntity.ok(mockRespuesta);
        }catch (ExceptionResponseGeneral exceptionResponseGeneral) {
			log.error("ExceptionResponseGeneral  exceptionResponseGeneral "+OBJECT_MAPPER.writeValueAsString(exceptionResponseGeneral)); 			
			log.error("error getGenericCustomError " + exceptionResponseGeneral.getMessage());
			log.info("error exceptionResponseGeneral.getBody() "+exceptionResponseGeneral.getBody().toString());			
			
			System.out.println("ExceptionResponseGeneral  exceptionResponseGeneral "+OBJECT_MAPPER.writeValueAsString(exceptionResponseGeneral)); 			
			System.out.println("error getGenericCustomError " + exceptionResponseGeneral.getMessage());
			System.out.println("error exceptionResponseGeneral.getBody() "+exceptionResponseGeneral.getBody().toString());			
			
			
			Map objetoRespuesta = OBJECT_MAPPER.readValue(exceptionResponseGeneral.getBody(), Map.class);
			
	
			
			return new ResponseEntity<>(objetoRespuesta, HttpStatus.valueOf(exceptionResponseGeneral.getStatus())); 			
		} catch (Exception exception) {
			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
			responseEntity = new ResponseEntity<>(err,
					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
		}     
	    
		return responseEntity;
	}*/
    
	public RecommenderCarrouselGeneralResp cargarMockReccomender() {
    	try {
    		RecommenderCarrouselGeneralResp request=OBJECT_MAPPER.readValue(resourceLoader.getResource("classpath:mockrecomender.json").getInputStream(), RecommenderCarrouselGeneralResp.class);
        	return request;
    	} catch (Exception e) {
	        // Manejo de la excepción. Podrías lanzar una excepción personalizada o manejarla de otra manera
	        e.printStackTrace();
	        return null;
	    }
	}


	public ResponseEntity<OutputComprarNuevo> getProductByIdSFCC(String sku,String autorization,Boolean homeReq) {


		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json; charset=utf-8");
		headers.add("Content-type", "application/json; charset=utf-8");
		headers.add("Authorization", "Bearer " + autorization);
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		RestTemplate restTemplate = new RestTemplate();

		System.err.println("URUURURURURUR - "+findProductsUrl+ "/("+sku+")"+ "?expand=availability,images,prices,variations");

		try{
			ResponseEntity<OutputComprarNuevo> jsonObjectStr = restTemplate.exchange(findProductsUrl+ "/("+sku+")"+ "?expand=availability,images,prices,variations",
					HttpMethod.GET, entity, OutputComprarNuevo.class);
			System.err.println("------------");
			System.err.println(new Gson().toJson(jsonObjectStr));
			ArrayList<SortingOption> sortingOption = new Gson().fromJson("[\n" +
					"   {\n" +
					"      \"_type\":\"product_search_sorting_option\",\n" +
					"      \"id\":\"product_order_date_register\",\n" +
					"      \"label\":\"Fecha de compra\"\n" +
					"   },\n" +
					"   {\n" +
					"      \"_type\":\"product_search_sorting_option\",\n" +
					"      \"id\":\"AcomodoAlfabetico\",\n" +
					"      \"label\":\"Orden alfabético\"\n" +
					"   },\n" +
					"   {\n" +
					"      \"_type\":\"product_search_sorting_option\",\n" +
					"      \"id\":\"Purchase\",\n" +
					"      \"label\":\"Mayor compra\"\n" +
					"   }\n" +
					"]", new TypeToken<ArrayList<SortingOption>>() {}.getType());
			jsonObjectStr.getBody().setSorting_options(sortingOption);
			if (homeReq==null)
				jsonObjectStr.getBody().setRefinements(getRefinements(autorization,jsonObjectStr.getBody()));

			return jsonObjectStr;
		}catch(Exception e){
			System.err.println("Error en consulta : " + e.toString());
			return null;
		}


	}

	private ArrayList<Refinement> getRefinements(String autorization,OutputComprarNuevo outputWs){
		try{
			ArrayList<Refinement> refArr = new ArrayList<>();
			Refinement ref = new Refinement();
			ref.set_type("product_search_refinement");
			ref.setAttribute_id("cgid");
			ref.setLabel("Categoría");
			List<String> categorias = new ArrayList<>();
			for (Datum prod : outputWs.getData()){
				categorias.add(prod.getPrimary_category_id());
			}

			List<String> categoriasSinDuplicados = categorias.stream()
					.distinct()
					.toList();

			ResponseEntity<OutputComercialStructure> resp = commercialStructureClient.getCategorias(autorization,categoriasSinDuplicados);
			ArrayList<com.ts4.customer.data.model.comprarnuevo.Value> values = new ArrayList<>();
			Map<String,String> mapCat = new HashMap<>();
			for (Datumcomercial cat : resp.getBody().getData()) {
				mapCat.put(cat.getParent_category_tree().get(0).getId(),cat.getParent_category_tree().get(0).getName());
			}

			for (Map.Entry<String, String> entry : mapCat.entrySet()) {
				System.out.println("Clave: " + entry.getKey() + ", Valor: " + entry.getValue());
				com.ts4.customer.data.model.comprarnuevo.Value val = new com.ts4.customer.data.model.comprarnuevo.Value();
				val.setId(entry.getKey());
				val.setLabel(entry.getValue());
				for (Datumcomercial cat : resp.getBody().getData()){
					for (ParentCategoryTree catTree : cat.getParent_category_tree()){
						if (entry.getKey().equals(catTree.getId())){
							val.set_type(cat.get_type());
							val.setValue(val.getValue().equals("") ? cat.getId() : val.getValue() + "," +cat.getId());

						}
					}
				}
				values.add(val);
			}


			/*for (Datumcomercial cat : resp.getBody().getData()){
				com.ts4.customer.data.model.comprarnuevo.Value val = new com.ts4.customer.data.model.comprarnuevo.Value();
				val.set_type(cat.get_type());
				val.setId(cat.getId());
				val.setLabel(cat.getName());
				for (ParentCategoryTree catTree : cat.getParent_category_tree()){
					val.setValue(val.getValue().equals("") ? catTree.getId() : val.getValue() + "," +catTree.getId());
				}
				values.add(val);
			}*/

			ref.setValues(values);
			refArr.add(ref);
			return refArr;
		}catch(Exception ex){
			return null;
		}
	}
}
