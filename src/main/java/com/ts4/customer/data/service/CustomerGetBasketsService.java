package com.ts4.customer.data.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.ts4.customer.data.client.*;
import com.ts4.customer.data.model.badge.BadgeConfig;
import com.ts4.customer.data.model.badge.BadgeWrapper;
import com.ts4.customer.data.model.customerdos.CustomerDos;
import com.ts4.customer.data.model.order.response.OrderResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.netflix.discovery.converters.Auto;
import com.ts4.customer.data.exception.ExceptionResponseGeneral;
import com.ts4.customer.data.exception.customerror.ErrorResponse;
import com.ts4.customer.data.exception.customerror.HttpStatusErrorCustom;
import com.ts4.customer.data.exception.ocapi.ErrorOcapi;
import com.ts4.customer.data.model.basket.Basket;
import com.ts4.customer.data.utils.enums.BasketMensajesEnum;
import com.ts4.customer.data.utils.enums.CustomerMensajesEnum;
import com.ts4.customer.data.utils.enums.OrderMensajesEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ts4.customer.data.model.basket.Item;
import com.ts4.customer.data.model.basket.Shipment;
import com.ts4.customer.data.model.coupon.response.CouponData;
import com.ts4.customer.data.model.coupon.response.CouponResult;
import com.ts4.customer.data.model.product.Product;
import com.ts4.customer.data.model.product.ProductResult;
import com.ts4.customer.data.model.promotionassigmentsearch.request.CampaignSearchRequest;
import com.ts4.customer.data.model.promotionassigmentsearch.response.CampaignRes;
import com.ts4.customer.data.model.promotionassigmentsearch.response.DetailCuponBasketCustom;
import com.ts4.customer.data.model.promotionassigmentsearch.response.HintPromotionAssignmentSearch;
import com.ts4.customer.data.model.promotionassigmentsearch.response.PromotionRes;
import com.ts4.customer.data.model.promotionassigmentsearch.response.ResultPromotionAssignmentSearchResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Slf4j
@Service
public class CustomerGetBasketsService {
	//private final Logger LOGGER = LogManager.getLogger(CustomerGetBasketsService.class.getName());

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private CustomerGetBasketsClient customerGetBasketsClient;

	@Autowired
	private BaskeItemsClient baskeItemsClient;

	@Autowired
	private BasketShipmentsClient basketShipmentsClient;

	@Autowired
	private CustomerOrdersClient customerOrdersClient;

	@Autowired
	private ProductsBasketClient productsBasketClient;

	@Autowired private CustomerDataClient customerDataClient;
	@Autowired private ResourceLoader resourceLoader;
	@Autowired private PromotionCampaignAssignmentClient promotionCampaignAssignmentClient;
	
	public ResponseEntity<?> getBasketsCustomer(  String authorization,String actionBasket,String sfccAction,String idCustomer) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);

		log.info("params "+objectMapper.writeValueAsString(idCustomer));
		log.info("actionBasket "+actionBasket);
		log.info("sfccAction "+sfccAction);

		ResponseEntity<?> responseEntity=null;
		try {

			responseEntity  = customerGetBasketsClient.getBasketsCustomer(authorization, sfccAction , idCustomer);
			String resultJsonRespuesta = responseEntity.getBody().toString();
			Map respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class) ;

			if(Optional.ofNullable( actionBasket ).isPresent() && actionBasket.equals("true")) {

				log.info(" INIT RESET BASKET ");
				log.info("LISTADO EXISTENTE --- "+respuestaMap.containsKey("baskets") );
				if(respuestaMap.containsKey("baskets")) {

					JsonNode actualObj = objectMapper.readTree(resultJsonRespuesta);
					log.info("### STRING JSON BASKET "+actualObj.get("baskets").toString() );
					List<Basket> listaDeObjetos = objectMapper.readValue( actualObj.get("baskets").toString(), new TypeReference<List<Basket>>() {});
					Basket basket =listaDeObjetos.get(0);

					boolean isUpdateProducts=false;
					boolean isUpdateShipments=false;

					if( Optional.ofNullable( basket.getProductItems() ).isPresent() &&  basket.getProductItems().size() > 0) {
						//Si hay mas shipments de los que debe haber por default
						int cantidadProductos=basket.getProductItems().size();
						List<Item>productosShipmentDefault= basket.getProductItems()
								.stream().filter(producto->producto.getShipmentId().equals("me"))
								.collect(Collectors.toList());

						if( productosShipmentDefault.size() != cantidadProductos ) {
							log.info("INIT PRODUCTS UPDATING DELETING"+basket.getProductItems().size());
							Map<String,List<Item>>itemsProductosRepetidos=new HashMap<>();

							//Agrega an mapa para saber si existen repetidos
							for(Item item: basket.getProductItems() )  {
								if(!itemsProductosRepetidos.containsKey(item.getProductId())) {
									itemsProductosRepetidos.put(item.getProductId(), new ArrayList<Item>());
								}
								itemsProductosRepetidos.get(item.getProductId() ).add(item);
							}

							int indexPibote=0;
							Item itempibote=null;
							Object itemPiboteProduct=null;

							for (Map.Entry<String, List<Item>> entry : itemsProductosRepetidos.entrySet()) {
								for(Item item : entry.getValue()  ) {
									itempibote=new Item();
									try {
										if(indexPibote==0) {
											double cantidadTotal = basket.getProductItems()
													.stream().filter(prod->prod.getProductId().equals(item.getProductId()))
													.mapToDouble(prod->prod.getQuantity()).sum();
											itempibote.setQuantity(cantidadTotal);
											itempibote.setShipmentId("me");
											itemPiboteProduct=objectMapper.readValue(objectMapper.writeValueAsString(itempibote) ,Object.class);
											responseEntity=baskeItemsClient.updateItemBasket(authorization, basket.getBasketId() , item.getItemId(), itemPiboteProduct);
											log.info("#### PRODUCTO ACTUALIZADO");
										}else {
											responseEntity=baskeItemsClient.deleteItemBasket(authorization,  basket.getBasketId() , item.getItemId() );
											log.info("#### PRODUCTO ELIMINADO");
										}
									}catch(Exception e) {
										log.error("ERROR "+e.getMessage());
										log.info("ERROR PARSING PRODUCT");
									}
									indexPibote++;
								}
								indexPibote=0;
							}
							isUpdateProducts=true;
						}
					}
					if(Optional.ofNullable(basket.getShipments()).isPresent() && basket.getShipments().size() > 0) {
						List<Shipment>listadoShipments=basket.getShipments().stream()
								.filter(envio->!envio.getShipmentId().toLowerCase().equals("me"))
								.collect(Collectors.toList());
						if(listadoShipments.size()>0){
							log.info("INIT SHIPMENT DELETING");
							for(Shipment envio:listadoShipments) {
								responseEntity=basketShipmentsClient.deleteShipmentBasket(authorization, basket.getBasketId(), envio.getShipmentId());
								log.info("SHIPMENT DELETED");

							}
							isUpdateShipments=true;
						}
					}

					if(isUpdateProducts || isUpdateShipments ) {
						//Vuelve a consultar los carritos
						responseEntity  = customerGetBasketsClient.getBasketsCustomer(authorization,sfccAction, idCustomer);
						resultJsonRespuesta = responseEntity.getBody().toString();
						respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class) ;
						responseEntity=new ResponseEntity<>( respuestaMap ,HttpStatus.valueOf(responseEntity.getStatusCode().value()) );
						return responseEntity;
					}

				}
			}
			log.info("data getBasketsCustomer **" + resultJsonRespuesta);
			responseEntity=new ResponseEntity<>( respuestaMap ,HttpStatus.valueOf(responseEntity.getStatusCode().value()) );

		}   catch (ExceptionResponseGeneral exceptionResponseGeneral) {
			log.info("error " + exceptionResponseGeneral.getMessage());
			ErrorOcapi errorOcapi = objectMapper.readValue(exceptionResponseGeneral.getBody(), ErrorOcapi.class);
			log.info("error --- " + objectMapper.writeValueAsString(errorOcapi));
			String traceMessage = exceptionResponseGeneral.getMessage();
			String message = HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason();
			if (Optional.ofNullable(errorOcapi.getFault()).isPresent()
					&& Optional.ofNullable(errorOcapi.getFault().getType()).isPresent()) {
				message = BasketMensajesEnum.getMessageValueOf(errorOcapi.getFault().getType())
						.getMessageCustom();
				traceMessage = errorOcapi.getFault().getMessage();
			}
			ErrorResponse err = new ErrorResponse(exceptionResponseGeneral.getStatus(), message, traceMessage, null);
			log.error("ERROR registerCustomer " + exceptionResponseGeneral.getReason());
			responseEntity = new ResponseEntity<>(err, HttpStatus.valueOf(exceptionResponseGeneral.getStatus()));
		} catch (Exception exception) {
			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
			responseEntity = new ResponseEntity<>(err,
					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
		}

		return responseEntity;
	}

	public ResponseEntity<?> getBasketsCustomerv2(String authorization, String actionBasket,
			String sfccAction,Boolean fullData, String idCustomer,Boolean headerIsCuponesDetails)
			throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		log.info("getBasketsCustomerv2 ");
		log.info("params " + objectMapper.writeValueAsString(idCustomer));
		log.info("actionBasket " + actionBasket);
		log.info("fullData " + fullData);
		log.info("headerIsCuponesDetails " + headerIsCuponesDetails);

		ResponseEntity<?> responseEntity = null;


		try {

			responseEntity = customerGetBasketsClient.getBasketsCustomer(authorization,sfccAction, idCustomer);
			String resultJsonRespuesta = responseEntity.getBody().toString();
			//Map respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class);


			JsonNode responseGetBaskets =objectMapper.readTree(resultJsonRespuesta);

			log.info("INIT RESET BASKET ");
			log.info("LISTADO EXISTENTE --- " + responseGetBaskets.has("baskets"));

			//Variable para identificar si se manda a traer la busqueda de productos
			List<String >idsProductos=null;
			List<String>idsCupones=null;
			if (responseGetBaskets.has("baskets")) {

				log.info("### STRING JSON BASKET " + responseGetBaskets.get("baskets").toString());
				List<Basket> listaDeObjetos = objectMapper.readValue(responseGetBaskets.get("baskets").toString(),new TypeReference<List<Basket>>() {});
				Basket basket = listaDeObjetos.get(0);

				try{
					Map<String,Object> param = new HashMap<>();
					param.put("expand","addresses");
					ResponseEntity<?> res = customerDataClient.getCustomerById(authorization,idCustomer,param);
					System.err.println(res.getStatusCode());
					CustomerDos customerData = OBJECT_MAPPER.readValue(res.getBody().toString(), CustomerDos.class);
					ObjectNode jsonNode = objectMapper.createObjectNode();
					if (customerData.getCTempStoreId() != null && customerData.getCTempStoreId() != "") {
						if (!basket.getC_storeID().equals(customerData.getCTempStoreId())) {
							jsonNode.put("c_storeID", customerData.getCTempStoreId());
							ResponseEntity<?> responseEntityUpdate = null;

							baskeItemsClient.updateBasket(authorization, basket.getBasketId(), jsonNode);

							responseEntityUpdate = customerGetBasketsClient.getBasketsCustomer(authorization,sfccAction, idCustomer);
							String resultJsonRespuestaUpdate = responseEntityUpdate.getBody().toString();
							responseGetBaskets = objectMapper.readTree(resultJsonRespuestaUpdate);
							listaDeObjetos = objectMapper.readValue(responseGetBaskets.get("baskets").toString(), new TypeReference<List<Basket>>() {});
							basket = listaDeObjetos.get(0);
						}
					}
				}catch(Exception e){
					System.err.println("Error al actualizar storeID : " +e.toString());
				}

				if (Optional.ofNullable(basket.getProductItems()).isPresent() && basket.getProductItems().size() > 0) {
					idsProductos= basket.getProductItems().stream().map(e->e.getProductId()).collect(Collectors.toList());
				}
				if( Optional.ofNullable(basket.getCouponItems()).isPresent() 
						&& basket.getCouponItems().size() >0 ) {
					idsCupones=basket.getCouponItems().stream().map(cupon->cupon.getCode()).collect(Collectors.toList());
				}
				if (Optional.ofNullable(actionBasket).isPresent() && actionBasket.equals("true")) {
					boolean isUpdateProducts = false;
					boolean isUpdateShipments = false;

					if (Optional.ofNullable(basket.getProductItems()).isPresent() && basket.getProductItems().size() > 0) {
						// Si hay mas shipments de los que debe haber por default
						int cantidadProductos = basket.getProductItems().size();
						List<Item> productosShipmentDefault = basket.getProductItems().stream()
								.filter(producto -> producto.getShipmentId().equals("me")).collect(Collectors.toList());

						if (productosShipmentDefault.size() != cantidadProductos) {
							log.info("INIT PRODUCTS UPDATING DELETING" + basket.getProductItems().size());
							Map<String, List<Item>> itemsProductosRepetidos = new HashMap<>();

							// Agrega an mapa para saber si existen repetidos
							for (Item item : basket.getProductItems()) {
								if (!itemsProductosRepetidos.containsKey(item.getProductId())) {
									itemsProductosRepetidos.put(item.getProductId(), new ArrayList<Item>());
								}
								itemsProductosRepetidos.get(item.getProductId()).add(item);
							}

							int indexPibote = 0;
							Item itempibote = null;
							Object itemPiboteProduct = null;

							for (Map.Entry<String, List<Item>> entry : itemsProductosRepetidos.entrySet()) {
								for (Item item : entry.getValue()) {
									itempibote = new Item();
									try {
										if (indexPibote == 0) {
											double cantidadTotal = basket.getProductItems().stream()
													.filter(prod -> prod.getProductId().equals(item.getProductId()))
													.mapToDouble(prod -> prod.getQuantity()).sum();
											itempibote.setQuantity(cantidadTotal);
											itempibote.setShipmentId("me");
											itemPiboteProduct = objectMapper.readValue(
													objectMapper.writeValueAsString(itempibote), Object.class);
											responseEntity = baskeItemsClient.updateItemBasket(authorization,
													basket.getBasketId(), item.getItemId(), itemPiboteProduct);
											log.info("#### PRODUCTO ACTUALIZADO");
										} else {
											responseEntity = baskeItemsClient.deleteItemBasket(authorization,
													basket.getBasketId(), item.getItemId());
											log.info("#### PRODUCTO ELIMINADO");
										}
									} catch (Exception e) {
										log.error("ERROR " + e.getMessage());
										log.info("ERROR PARSING PRODUCT");
									}
									indexPibote++;
								}
								indexPibote = 0;
							}
							isUpdateProducts = true;
						}
					}
					if (Optional.ofNullable(basket.getShipments()).isPresent() && basket.getShipments().size() > 0) {
						List<Shipment> listadoShipments = basket.getShipments().stream()
								.filter(envio -> !envio.getShipmentId().toLowerCase().equals("me"))
								.collect(Collectors.toList());
						if (listadoShipments.size() > 0) {
							log.info("INIT SHIPMENT DELETING");
							for (Shipment envio : listadoShipments) {
								responseEntity = basketShipmentsClient.deleteShipmentBasket(authorization,
										basket.getBasketId(), envio.getShipmentId());
								log.info("SHIPMENT DELETED");

							}
							isUpdateShipments = true;
						}
					}

					if (isUpdateProducts || isUpdateShipments) {
						// Vuelve a consultar los carritos
						responseEntity = customerGetBasketsClient.getBasketsCustomer(authorization, sfccAction ,idCustomer);
						resultJsonRespuesta = responseEntity.getBody().toString();
						//respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class);
						responseGetBaskets=objectMapper.readTree(resultJsonRespuesta);
//						responseEntity = new ResponseEntity<>(respuestaMap,
//								HttpStatus.valueOf(responseEntity.getStatusCode().value()));
//						return responseEntity;
					}


				}
				
				//Validacion para agregar información de cupon al listado de cupones del basket
				if(Optional.ofNullable(headerIsCuponesDetails).isPresent() && 
						headerIsCuponesDetails.booleanValue()== Boolean.TRUE && 
						Optional.ofNullable(idsCupones).isPresent() && idsCupones.size() > 0) {
					
					ResultPromotionAssignmentSearchResponse resultPromotion = obtenerInfoPromotionAssignment(authorization,idsCupones,objectMapper);
					if(Optional.ofNullable(resultPromotion).isPresent() 
							&& Optional.ofNullable(resultPromotion.getHits()).isPresent() && 
							resultPromotion.getHits().size()>0) {
							
						for(HintPromotionAssignmentSearch hintAssignment:resultPromotion.getHits()) {
							if( Optional.ofNullable(hintAssignment.getCampaign()).isPresent() && 
							    Optional.ofNullable(hintAssignment.getCampaign().getCoupons()).isPresent() && 
							    hintAssignment.getCampaign().getCoupons().size() > 0 ) {								
								//Setea el id de promotion con el que debe coincider de cupon del basket
								hintAssignment.setCustomIdPromotionCampaign(hintAssignment.getCampaign().getCoupons().get(0));
							}
						}												
						if (responseGetBaskets.has("baskets") && responseGetBaskets.get("baskets").isArray() ) {														
							DetailCuponBasketCustom detailCupon=null;
							for (JsonNode basketIndNode : responseGetBaskets.get("baskets") ) {
								if(basketIndNode.has("coupon_items") && basketIndNode.get("coupon_items").isArray()  ) {
									for (JsonNode cuponItem : basketIndNode.get("coupon_items") ) {
										String codeProduct=  cuponItem.get("code").asText();
										
										Optional<HintPromotionAssignmentSearch> optionalAssignment = resultPromotion.getHits().stream()
												.filter(promotion-> 
														Optional.ofNullable(promotion.getCustomIdPromotionCampaign()).isPresent() && 
														promotion.getCustomIdPromotionCampaign().equals(codeProduct)
												).findFirst();
										if(optionalAssignment.isPresent()) {
											HintPromotionAssignmentSearch assignmentResult = optionalAssignment.get();
											detailCupon=new DetailCuponBasketCustom();
										
											if(Optional.ofNullable(assignmentResult.getDescription()).isPresent()) {
												detailCupon.setDetails(assignmentResult.getDescription());
											}
											if(Optional.ofNullable(assignmentResult.getCampaign()).isPresent()) {
												CampaignRes campaignAssig = assignmentResult.getCampaign();
												if(Optional.ofNullable(campaignAssig.getEndDate()).isPresent())
													detailCupon.setEndDate(campaignAssig.getEndDate());
												
												if(Optional.ofNullable(campaignAssig.getStartDate()).isPresent())
													detailCupon.setStartDate(campaignAssig.getStartDate());

											}
											if(Optional.ofNullable(assignmentResult.getPromotion()).isPresent()) {
												PromotionRes promotiAssig = assignmentResult.getPromotion();
												if(Optional.ofNullable(promotiAssig.getCalloutMsg()).isPresent())
													detailCupon.setCalloutMsg(promotiAssig.getCalloutMsg());
												
												if(Optional.ofNullable(promotiAssig.getId()).isPresent())
													detailCupon.setId(promotiAssig.getId());
												
												if(Optional.ofNullable(promotiAssig.getPromotionClass()).isPresent())
													detailCupon.setPromotionClass(promotiAssig.getPromotionClass());
												
												if(Optional.ofNullable(promotiAssig.getName()).isPresent())
													detailCupon.setName(promotiAssig.getName());
											}																						
											((ObjectNode) cuponItem).set("info_cupon",objectMapper.readTree(objectMapper.writeValueAsString(detailCupon)) );
										}				
									}
								}
							}
						}
					}
					
					
				}
				if( Optional.ofNullable( idsProductos ).isPresent() && Boolean.TRUE==fullData) {
					try {
						Map<String ,Object> mapaValores=new HashMap<String,Object>();
						mapaValores.put("expand","images,availability,links");
						ResponseEntity<Object> responseBodyProductos = lotesProductos(idsProductos,authorization,  idsProductos, mapaValores);
						if(responseBodyProductos.getStatusCode().is2xxSuccessful() && Optional.ofNullable(responseBodyProductos.getBody()).isPresent()) {
							ProductResult productoResultado= new Gson().fromJson(new Gson().toJson(responseBodyProductos.getBody()),ProductResult.class);
							if(	Optional.ofNullable(productoResultado).isPresent() &&
									Optional.ofNullable(productoResultado.getData()).isPresent() &&
									productoResultado.getData().size()>0 ){

								List<Product> productosFind=productoResultado.getData();
								if (responseGetBaskets.has("baskets") && responseGetBaskets.get("baskets").isArray() ) {
									// Itera los baskets
									for (JsonNode basketIndNode : responseGetBaskets.get("baskets") ) {
										if(basketIndNode.has("product_items") && basketIndNode.get("product_items").isArray()  ) {
											//Accede a los productos del basket
											for (JsonNode productoNode : basketIndNode.get("product_items") ) {
												String idProducto=  productoNode.get("product_id").asText();

												Optional<Product> optionalProduct= productosFind.stream().filter(e-> e.getId().equals(idProducto)).findFirst();
												if(optionalProduct.isPresent()) {

													((ObjectNode) productoNode).set("info_product",objectMapper.readTree(objectMapper.writeValueAsString(optionalProduct.get())) );
													System.err.println("===========================");
													System.err.println("Nombre producto original : " + ((ObjectNode) productoNode).get("product_name"));
													System.err.println("Nombre remplazo : " + optionalProduct.get().getC_nameBySite());
													System.err.println("===========================");

													if (optionalProduct.get().getC_nameBySite()!=null){
														((ObjectNode) productoNode).put("product_name", optionalProduct.get().getC_nameBySite());
														//((ObjectNode) productoNode).set("product_name", optionalProduct.get().getC_nameBySite());
													}

													if (optionalProduct.get().getSaleLimit()==null){
														((ObjectNode) productoNode).put("c_saleLimit", optionalProduct.get().getSaleLimit());
														//((ObjectNode) productoNode).set("product_name", optionalProduct.get().getC_nameBySite());
													}

													if (optionalProduct.get().getMinOrderQuantity()== null){
														((ObjectNode) productoNode).put("min_order_quantity", optionalProduct.get().getMinOrderQuantity());
														//((ObjectNode) productoNode).set("product_name", optionalProduct.get().getC_nameBySite());
													}

												}
											}
										}
									}
								}
								log.info("obteniendo info de resultados ---" +objectMapper.writeValueAsString( productoResultado ) );

							}
						}
					}catch(Exception e) {
						e.printStackTrace();
						log.error("#ERROR AL CONSULTA LOS PRODUCTOS");
					}
				}
			}

			log.info("data getBasketsCustomerv2 **" + resultJsonRespuesta);

			System.err.println(new Gson().toJson(responseGetBaskets.get("baskets")));

			responseGetBaskets = changeBarges(responseGetBaskets);

			responseEntity = new ResponseEntity<>(responseGetBaskets,HttpStatus.valueOf(responseEntity.getStatusCode().value()));

		} catch (ExceptionResponseGeneral exceptionResponseGeneral) {
			log.info("error " + exceptionResponseGeneral.getMessage());
			ErrorOcapi errorOcapi = objectMapper.readValue(exceptionResponseGeneral.getBody(), ErrorOcapi.class);
			log.info("error --- " + objectMapper.writeValueAsString(errorOcapi));
			String traceMessage = exceptionResponseGeneral.getMessage();
			String message = HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason();
			if (Optional.ofNullable(errorOcapi.getFault()).isPresent()
					&& Optional.ofNullable(errorOcapi.getFault().getType()).isPresent()) {
				message = BasketMensajesEnum.getMessageValueOf(errorOcapi.getFault().getType()).getMessageCustom();
				traceMessage = errorOcapi.getFault().getMessage();
			}
			ErrorResponse err = new ErrorResponse(exceptionResponseGeneral.getStatus(), message, traceMessage, null);
			log.error("ERROR registerCustomer " + exceptionResponseGeneral.getReason());
			responseEntity = new ResponseEntity<>(err, HttpStatus.valueOf(exceptionResponseGeneral.getStatus()));
		} catch (Exception exception) {
			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
			responseEntity = new ResponseEntity<>(err,
					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
		}

		return responseEntity;
	}

	public ResponseEntity<?> getOrdersCustomer(  String authorization,String idCustomer,Map<String,Object> queryParams,Boolean headerIsCuponesDetails ) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		log.info("params "+objectMapper.writeValueAsString(idCustomer));

		ResponseEntity<?> responseEntity=null;
		try {
			responseEntity  = customerOrdersClient.getOrdersCustomer(authorization, idCustomer,queryParams);
			String resultJsonRespuesta = responseEntity.getBody().toString();
			log.info("data getOrdersCustomer **" + resultJsonRespuesta);
			JsonNode responseGetOrders =objectMapper.readTree(resultJsonRespuesta);

			
			List<String>idsCupones=new ArrayList<String>();

			if (responseGetOrders.has("data")) {
				log.info("### STRING JSON BASKET " + responseGetOrders.get("data").toString());
				List<OrderResponse> listaDeObjetos = objectMapper.readValue(responseGetOrders.get("data").toString(),new TypeReference<List<OrderResponse>>() {});				
				for(OrderResponse order:listaDeObjetos) {
					if( Optional.ofNullable(order.getCouponItems()).isPresent() 
							&& order.getCouponItems().size() >0 ) {												
						List<String>cuponesTemp=order.getCouponItems().stream().map(cupon->cupon.getCode()).collect(Collectors.toList());
						if(Optional.ofNullable(cuponesTemp).isPresent() && cuponesTemp.size()>0) {
							idsCupones.addAll(cuponesTemp);
						}
					}
				}									
			}

			//Validacion para agregar información de cupon al listado de cupones del basket
			if(Optional.ofNullable(headerIsCuponesDetails).isPresent() && 
					headerIsCuponesDetails.booleanValue()== Boolean.TRUE && 
					Optional.ofNullable(idsCupones).isPresent() && idsCupones.size() > 0) {
				
				ResultPromotionAssignmentSearchResponse resultPromotion = obtenerInfoPromotionAssignment(authorization,idsCupones,objectMapper);
				if(Optional.ofNullable(resultPromotion).isPresent() 
						&& Optional.ofNullable(resultPromotion.getHits()).isPresent() && 
						resultPromotion.getHits().size()>0) {
						
					for(HintPromotionAssignmentSearch hintAssignment:resultPromotion.getHits()) {
						if( Optional.ofNullable(hintAssignment.getCampaign()).isPresent() && 
						    Optional.ofNullable(hintAssignment.getCampaign().getCoupons()).isPresent() && 
						    hintAssignment.getCampaign().getCoupons().size() > 0 ) {								
							//Setea el id de promotion con el que debe coincider de cupon del basket
							hintAssignment.setCustomIdPromotionCampaign(hintAssignment.getCampaign().getCoupons().get(0));
						}
					}												
					if (responseGetOrders.has("data") && responseGetOrders.get("data").isArray() ) {														
						DetailCuponBasketCustom detailCupon=null;
						for (JsonNode orderIndNode : responseGetOrders.get("data") ) {
							if(orderIndNode.has("coupon_items") && orderIndNode.get("coupon_items").isArray()  ) {
								for (JsonNode cuponItem : orderIndNode.get("coupon_items") ) {
									String codeProduct=  cuponItem.get("code").asText();
									
									Optional<HintPromotionAssignmentSearch> optionalAssignment = resultPromotion.getHits().stream()
											.filter(promotion-> 
													Optional.ofNullable(promotion.getCustomIdPromotionCampaign()).isPresent() && 
													promotion.getCustomIdPromotionCampaign().equals(codeProduct)
											).findFirst();
									if(optionalAssignment.isPresent()) {
										HintPromotionAssignmentSearch assignmentResult = optionalAssignment.get();
										detailCupon=new DetailCuponBasketCustom();
									
										if(Optional.ofNullable(assignmentResult.getDescription()).isPresent()) {
											detailCupon.setDetails(assignmentResult.getDescription());
										}
										if(Optional.ofNullable(assignmentResult.getCampaign()).isPresent()) {
											CampaignRes campaignAssig = assignmentResult.getCampaign();
											if(Optional.ofNullable(campaignAssig.getEndDate()).isPresent())
												detailCupon.setEndDate(campaignAssig.getEndDate());
											
											if(Optional.ofNullable(campaignAssig.getStartDate()).isPresent())
												detailCupon.setStartDate(campaignAssig.getStartDate());

										}
										if(Optional.ofNullable(assignmentResult.getPromotion()).isPresent()) {
											PromotionRes promotiAssig = assignmentResult.getPromotion();
											if(Optional.ofNullable(promotiAssig.getCalloutMsg()).isPresent())
												detailCupon.setCalloutMsg(promotiAssig.getCalloutMsg());
											
											if(Optional.ofNullable(promotiAssig.getId()).isPresent())
												detailCupon.setId(promotiAssig.getId());
											
											if(Optional.ofNullable(promotiAssig.getPromotionClass()).isPresent())
												detailCupon.setPromotionClass(promotiAssig.getPromotionClass());
											
											if(Optional.ofNullable(promotiAssig.getName()).isPresent())
												detailCupon.setName(promotiAssig.getName());
										}																						
										((ObjectNode) cuponItem).set("info_cupon",objectMapper.readTree(objectMapper.writeValueAsString(detailCupon)) );
									}				
								}
							}
						}
					}
				}								
			}
			
			
			responseEntity = new ResponseEntity<>(responseGetOrders,HttpStatus.valueOf(responseEntity.getStatusCode().value()));

		}catch (ExceptionResponseGeneral exceptionResponseGeneral) {
			log.info("error " + exceptionResponseGeneral.getMessage());
			ErrorOcapi errorOcapi = objectMapper.readValue(exceptionResponseGeneral.getBody(), ErrorOcapi.class);
			log.info("error --- " + objectMapper.writeValueAsString(errorOcapi));
			String traceMessage = exceptionResponseGeneral.getMessage();
			String message = HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason();
			if (Optional.ofNullable(errorOcapi.getFault()).isPresent()
					&& Optional.ofNullable(errorOcapi.getFault().getType()).isPresent()) {
				message = OrderMensajesEnum.getMessageValueOf(errorOcapi.getFault().getType())
						.getMessageCustom();
				traceMessage = errorOcapi.getFault().getMessage();
			}
			ErrorResponse err = new ErrorResponse(exceptionResponseGeneral.getStatus(), message, traceMessage, null);
			log.error("ERROR registerCustomer " + exceptionResponseGeneral.getReason());
			responseEntity = new ResponseEntity<>(err, HttpStatus.valueOf(exceptionResponseGeneral.getStatus()));
		} catch (Exception exception) {
			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
			responseEntity = new ResponseEntity<>(err,
					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
		}

		return responseEntity;
	}


	private ResponseEntity<Object>  lotesProductos(List<String> idsProductos,String authorization,List<String> ids,Map<String,Object> queryParams){
		int tamanoLote = 24;
		if (idsProductos.size() > tamanoLote) {
			ResponseEntity<Object> responseBodyProductos = null;
			List<Product> fullData = new ArrayList<>();
			int numeroLotes = (int) Math.ceil((double) idsProductos.size() / tamanoLote);
			for (int i = 0; i < numeroLotes; i++) {
				int inicio = i * tamanoLote;
				int fin = Math.min((i + 1) * tamanoLote, idsProductos.size());
				List<String> loteActual = idsProductos.subList(inicio, fin);
				responseBodyProductos = productsBasketClient.getProductsByIds(authorization, loteActual, queryParams);
				if(responseBodyProductos.getStatusCode().is2xxSuccessful() && Optional.ofNullable(responseBodyProductos.getBody()).isPresent()) {
					ProductResult productoResultado = new Gson().fromJson(new Gson().toJson(responseBodyProductos.getBody()), ProductResult.class);
					if (Optional.ofNullable(productoResultado).isPresent() &&
							Optional.ofNullable(productoResultado.getData()).isPresent() &&
							productoResultado.getData().size() > 0) {
						fullData.addAll(productoResultado.getData());
					}
				}
			}
			ProductResult result = new Gson().fromJson(new Gson().toJson(responseBodyProductos.getBody()), ProductResult.class);
			result.setData(fullData);
			return new ResponseEntity<>(result,responseBodyProductos.getStatusCode());
		} else {
			ResponseEntity<Object> responseBodyProductos = productsBasketClient.getProductsByIds(authorization, idsProductos, queryParams);
			return responseBodyProductos;
		}
	}
	
	
	public CampaignSearchRequest cargarEntradaPromotionAssignment() {
    	try {
    		CampaignSearchRequest request=OBJECT_MAPPER.readValue(resourceLoader.getResource("classpath:entradapromotionsassignmentcampaign.json").getInputStream(), CampaignSearchRequest.class);
        	return request;
    	} catch (Exception e) {
	        // Manejo de la excepción. Podrías lanzar una excepción personalizada o manejarla de otra manera
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public ResultPromotionAssignmentSearchResponse obtenerInfoPromotionAssignment( 	String authorization,	List<String> listadoIdsCampanias,ObjectMapper objectMapper) {		
		ResultPromotionAssignmentSearchResponse resultadoPromotionAssignment=null;
		try {			
			log.info("ENTRADA REQUEST -listadoIdsCampanias"+objectMapper.writeValueAsString(listadoIdsCampanias));
			CampaignSearchRequest campaign= cargarEntradaPromotionAssignment();
			ResponseEntity<String>promotionAssignmentSearch=promotionCampaignAssignmentClient.getPromotionsCampaignAssignmentSearch( authorization, campaign);				
			if(Optional.ofNullable(promotionAssignmentSearch).isPresent() && 
					Optional.ofNullable(promotionAssignmentSearch.getBody()).isPresent()) {				
				resultadoPromotionAssignment=objectMapper.readValue(promotionAssignmentSearch.getBody(), ResultPromotionAssignmentSearchResponse.class);		
				String jsonRespuesta=objectMapper.writeValueAsString(resultadoPromotionAssignment);
				log.info("INFO ===== json respuesta"+jsonRespuesta);
				System.out.println("INFO ===== json respuesta"+jsonRespuesta);

			}	
		}catch (ExceptionResponseGeneral exceptionResponseGeneral) {
	    	log.error("ExceptionResponseGeneral getPromotionsByIds  "+exceptionResponseGeneral.getBody());
	    	System.out.println("ExceptionResponseGeneral getPromotionsByIds  "+exceptionResponseGeneral.getBody());
		} catch (Exception exception) {
			exception.printStackTrace();
	    	log.error("Exception getPromotionsByIds  "+exception.getMessage());
	    	System.out.println("Exception getPromotionsByIds  "+exception.getMessage());
	    	
	  	}	
		return resultadoPromotionAssignment;
	}


	private JsonNode changeBarges(JsonNode rootNode){
		try {
			Map<String, BadgeConfig> configBadges = configBadges();
			//System.err.println("configBadges : " + new Gson().toJson(configBadges));
			JsonNode productItemsNode = rootNode.at("/baskets/0/product_items");
			if (productItemsNode.isArray()) {
				ArrayNode productItemsArray = (ArrayNode) productItemsNode;
				for (JsonNode productItem : productItemsArray) {
					JsonNode infoProduct = productItem.get("info_product");
					if(infoProduct!=null){
						JsonNode cListBadgesNode = infoProduct.get("c_listBadges");
						if (cListBadgesNode!= null && cListBadgesNode.isArray()) {
							ArrayNode cListBadgesArray = (ArrayNode) cListBadgesNode;
							for (JsonNode badgeNode : cListBadgesArray) {
								String ppromotionId = badgeNode.path("promotionId").asText();
								//System.err.println("Promotio : " + ppromotionId);
								if (configBadges.containsKey(ppromotionId)) {
									BadgeConfig config = configBadges.get(ppromotionId);
									ArrayNode promotionParametersNode =  (ArrayNode) badgeNode.get("promotionParameters");
									Map<String, String> replacements = new HashMap<>();
									for (int i = 0; i < promotionParametersNode.size(); i++) {
										replacements.put("#var" + (i + 1), promotionParametersNode.get(i).asText());
									}

									//System.err.println(new Gson().toJson(replacements));

									String desplayName = config.getDisplayName();
									String desplayTitle = config.getDisplayTitle();
									for (Map.Entry<String, String> entry : replacements.entrySet()) {
										desplayName = desplayName.replace(entry.getKey(), entry.getValue());
										desplayTitle = desplayTitle.replace(entry.getKey(), entry.getValue());
									}
									((ObjectNode) badgeNode).put("displayName", desplayName);
									((ObjectNode) badgeNode).put("displayTitle", desplayTitle);
									((ObjectNode) badgeNode).put("displayColor", config.getDisplayColor());
									((ObjectNode) badgeNode).put("promotionId", config.getPromotionId());
									((ObjectNode) badgeNode).put("groupPosition", config.getGroupPosition());
								} else {
									((ObjectNode) badgeNode).put("displayColor", "#37464F");
									((ObjectNode) badgeNode).put("groupPosition", 1);
								}
							}
						}
					}



				}
			}
			return rootNode;

		} catch (Exception e) {
			e.printStackTrace();
			return rootNode;
		}
	}

	private Map<String, BadgeConfig> configBadges(){
		Map<String, BadgeConfig> configBadges = new HashMap<>();
		Yaml yaml = new Yaml(new Constructor(BadgeWrapper.class));
		try (InputStream inputStream = BadgeConfig.class.getClassLoader().getResourceAsStream("BadgesConfiguration.yml")) {
			if (inputStream == null) {throw new RuntimeException("Archivo badges.yaml no encontrado en el classpath");}

			BadgeWrapper badgeWrapper = yaml.load(inputStream);
			for (BadgeConfig badgeConfig : badgeWrapper.getCodigobadge()) {
				configBadges.put(badgeConfig.getPromotionId(),badgeConfig);
			}

			return configBadges;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
