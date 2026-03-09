package com.ts4.customer.data.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ts4.customer.data.client.*;
import com.ts4.customer.data.exception.ExceptionResponseGeneral;
import com.ts4.customer.data.exception.customerror.ErrorResponse;
import com.ts4.customer.data.exception.customerror.HttpStatusErrorCustom;
import com.ts4.customer.data.exception.ocapi.ErrorOcapi;
import com.ts4.customer.data.model.OauthModel;
import com.ts4.customer.data.model.basket.Basket;
import com.ts4.customer.data.model.basket.GeneralBasket;
import com.ts4.customer.data.model.customer.Address;
import com.ts4.customer.data.model.customer.Customer;
import com.ts4.customer.data.model.customer.ResultAddress;
import com.ts4.customer.data.model.membership.request.MembershipsReq;
import com.ts4.customer.data.model.membership.response.MembershipAddressResp;
import com.ts4.customer.data.model.membership.response.MembershipDetailsResp;
import com.ts4.customer.data.utils.custompreferences.CustomPreferencesUtils;
import com.ts4.customer.data.utils.enums.CustomerMensajesEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
public class CustomerDataService {
    private static final Class ObjectNode = null;


	//private final Logger LOGGER = LogManager.getLogger(CustomerDataService.class.getName());
    
	@Autowired
	private CustomerDataClient customerDataClient;

	@Autowired
	private CustomerPasswordResetClient customerPasswordResetClient;
	
	@Autowired
	private Environment env;

	@Autowired
	private CustomerGetBasketsClient customerGetBasketsClient;
	
	@Autowired 
	private CustomerAddressClient customerAddressClient;
	
	@Autowired 
	private CustomPreferencesUtils customPreferencesUtils;
	
	@Autowired
	private MembershipClient membershipClient;

	@Autowired
	private OauthServiceBM oauthServiceBM;

	@Autowired
	private CustomerDataSitesSite customerDataSitesSite;

	@Autowired
	private MembershipTerminationClient membershipTerminationClient;

	public ResponseEntity<?> getTerminosCondiciones( String authorization ) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	    log.info("authorization "+authorization);	  	     
        ResponseEntity<?> responseEntity=null;
        try {
        	Map<String,String>mapaUrl=new HashMap<String,String>();
        	String url =env.getProperty("cityclub.url.url-terminos");
        	log.info("URL TERMINOS"+url);
        	mapaUrl.put("urlTerminos",url);        
        	responseEntity=ResponseEntity.status(HttpStatus.OK).body(mapaUrl);        	   
        } catch (Exception exception) {
        	log.info("URL TERMINOS exception "+exception.getMessage());
			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
			responseEntity = new ResponseEntity<>(err,
					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
		}
        return responseEntity;
	}
	
	public ResponseEntity<?> registerCustomer( Object dataParams,String authorization  ,String headerActionCustomer) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
	    String jsonParams = objectMapper.writeValueAsString(dataParams);  
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	    log.info("LEYENDO DATOS DE ENTRADA "+jsonParams);
	    log.info("authorization "+authorization);	  
	    log.info("headerActionCustomer "+headerActionCustomer);	   

        ResponseEntity<?> responseEntity=null;
        try {
        	responseEntity= customerDataClient.registerCustomer(dataParams ,authorization, headerActionCustomer );
    		String resultJsonRespuesta = responseEntity.getBody().toString();
            Map respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class) ;
    		log.info("data registerCustomer **" + resultJsonRespuesta);
			HttpHeaders headersrespuesta = responseEntity.getHeaders();
			String tokenEnvio= headersrespuesta.get(HttpHeaders.AUTHORIZATION).get(0).replace("Bearer", "").trim();
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+tokenEnvio );
			//Customer customerResponse = objectMapper.readValue(resultJsonRespuesta, Customer.class);
        	responseEntity=new ResponseEntity<>( respuestaMap ,headers,HttpStatus.valueOf(responseEntity.getStatusCode().value()) );
        }catch (ExceptionResponseGeneral exceptionResponseGeneral) {
			log.info("registerCustomer ");
 			responseEntity= getGenericCustomError(exceptionResponseGeneral,objectMapper);
		} catch (Exception exception) {
			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
			responseEntity = new ResponseEntity<>(err,
					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
		}     
        
        return responseEntity;
	}

	public ResponseEntity<?> updateCustomerById( String authorization  ,String headerActionCustomer,String customer_id,Object customer ) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	    String jsonParams = objectMapper.writeValueAsString(customer);  

	    log.info("LEYENDO DATOS DE ENTRADA "+jsonParams);
	    log.info("authorization "+authorization);	  
	    log.info("headerActionCustomer "+headerActionCustomer);	   
	    log.info("customer_id "+customer_id);	   

        ResponseEntity<?> responseEntity=null;
        try {
			if (headerActionCustomer!=null && headerActionCustomer.equals("membership-link")){
				eliminaBasket(authorization,customer_id);
				
			}
        	responseEntity= customerDataClient.updateCustomerById( authorization  , headerActionCustomer, customer_id, customer  );
        	String resultJsonRespuesta = responseEntity.getBody().toString();
        	if(     Optional.ofNullable(headerActionCustomer).isPresent()  
        			&&  headerActionCustomer.equals("membership-link") 
        			&& responseEntity.getStatusCode().is2xxSuccessful()) {
        		
        		boolean isInsertMembresia=insertarAddressMembresia(authorization, customer_id,objectMapper,resultJsonRespuesta);
        	}
            Map respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class) ;
    		log.info("data updateCustomerById **" + resultJsonRespuesta);
    		//Customer customerResponse = objectMapper.readValue(resultJsonRespuesta, Customer.class);
        	responseEntity=new ResponseEntity<>( respuestaMap  ,HttpStatus.valueOf(responseEntity.getStatusCode().value()) );        	       
        }catch (ExceptionResponseGeneral exceptionResponseGeneral) {
	

			log.info("error updateCustomerById ");
 			responseEntity= getGenericCustomError(exceptionResponseGeneral,objectMapper);


		} catch (Exception exception) {
			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
			responseEntity = new ResponseEntity<>(err,
					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
		}   
        return responseEntity;
	}
	
	public ResponseEntity<?> getCustomerById( String authorizationHeader ,
			 							      String customer_id,  Map<String,Object> queryParams) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

	    log.info("authorizationHeader "+authorizationHeader);
	    log.info("authorization "+customer_id);	  
	
        	        
        ResponseEntity<?> responseEntity=null;
        try {
        	responseEntity= customerDataClient.getCustomerById(authorizationHeader ,customer_id ,queryParams);
    		String resultJsonRespuesta = responseEntity.getBody().toString();
            Map respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class) ;
    		log.info("data getCustomerById **" + resultJsonRespuesta);
    		//Customer customerResponse = objectMapper.readValue(resultJsonRespuesta, Customer.class);
        	responseEntity=new ResponseEntity<>( respuestaMap  ,HttpStatus.valueOf(responseEntity.getStatusCode().value()) );        	       
        }catch (ExceptionResponseGeneral exceptionResponseGeneral) {
    		log.info("error getCustomerById ");
 			responseEntity= getGenericCustomError(exceptionResponseGeneral,objectMapper);
  		} catch (Exception exception) {
  			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
  					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
  			responseEntity = new ResponseEntity<>(err,
  					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
  		}   
        
        return responseEntity;
	}
	
	public ResponseEntity<?> setPasswordReset( Object dataParams, Map<String,Object> queryParams) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
	    String jsonParams = objectMapper.writeValueAsString(dataParams);  
	    log.info("LEYENDO DATOS DE ENTRADA "+jsonParams);
		
	    if(queryParams == null ) 
    		queryParams=new HashMap<String,Object>();
    	
        queryParams.put("client_id", env.getProperty("cityclub.consumer.key"));
	    
        ResponseEntity<?> responseEntity=null;
        try {
        	responseEntity  = customerPasswordResetClient.setPasswordReset( dataParams ,queryParams );
        	
        	if( responseEntity.getBody() != null) {
        		String resultJsonRespuesta = responseEntity.getBody().toString();
        		log.info("data setPasswordReset **" + resultJsonRespuesta);
                Map respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class) ;
        		//LoginCustomerModel accessGuestModel = objectMapper.readValue(resultJsonRespuesta, LoginCustomerModel.class);
            	responseEntity=new ResponseEntity<>( respuestaMap ,HttpStatus.valueOf(responseEntity.getStatusCode().value()) );
        	}        	        	
        }catch (ExceptionResponseGeneral exceptionResponseGeneral) {
     			log.info("error setPasswordReset ");
     			responseEntity= getGenericCustomError(exceptionResponseGeneral,objectMapper);

     		} catch (Exception exception) {
     			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
     					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
     			responseEntity = new ResponseEntity<>(err,
     					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
     		}      		
		return responseEntity;
	}

	
	public ResponseEntity<?> setPasswordResetCustomer( Object dataParams,  Map<String,Object> queryParams) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
	    String jsonParams = objectMapper.writeValueAsString(dataParams);  
	    log.info("LEYENDO DATOS DE ENTRADA "+jsonParams);
        ResponseEntity<?> responseEntity=null;
        if(queryParams == null ) 
    		queryParams=new HashMap<String,Object>();
    	
        queryParams.put("client_id", env.getProperty("cityclub.consumer.key"));
        
        try {
        	responseEntity  = customerPasswordResetClient.setPasswordReset( dataParams , queryParams  );
        	
        	if( responseEntity.getBody() != null) {
        		String resultJsonRespuesta = responseEntity.getBody().toString();
        		log.info("data setPasswordResetCustomer **" + resultJsonRespuesta);
                Map respuestaMap = objectMapper.readValue(resultJsonRespuesta, Map.class) ;
        		//LoginCustomerModel accessGuestModel = objectMapper.readValue(resultJsonRespuesta, LoginCustomerModel.class);
            	responseEntity=new ResponseEntity<>( respuestaMap ,HttpStatus.valueOf(responseEntity.getStatusCode().value()) );
        	}        	        	
        }catch (ExceptionResponseGeneral exceptionResponseGeneral) {
     			log.info("error setPasswordResetCustomer");
     			responseEntity= getGenericCustomError(exceptionResponseGeneral,objectMapper);

     		} catch (Exception exception) {
     			ErrorResponse err = new ErrorResponse(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode(),
     					HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason(), exception.getMessage(), null);
     			responseEntity = new ResponseEntity<>(err,
     					HttpStatus.valueOf(HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getCode()));
     		}   		
		return responseEntity;
	}
	public ResponseEntity<?> getGenericCustomError(ExceptionResponseGeneral exceptionResponseGeneral ,ObjectMapper objectMapper) throws JsonMappingException, JsonProcessingException {
			log.info("error getGenericCustomError " + exceptionResponseGeneral.getMessage());
			log.info("error exceptionResponseGeneral.getBody() "+exceptionResponseGeneral.getBody().toString());
			
			ErrorOcapi errorOcapi = objectMapper.readValue(exceptionResponseGeneral.getBody(), ErrorOcapi.class);
			log.info("error --- " + objectMapper.writeValueAsString(errorOcapi));
			String traceMessage = exceptionResponseGeneral.getMessage();
			String message = HttpStatusErrorCustom.INTERNAL_SERVER_ERROR.getReason();

			if (Optional.ofNullable(errorOcapi.getFault()).isPresent()
					&& Optional.ofNullable(errorOcapi.getFault().getType()).isPresent()) {

				message = CustomerMensajesEnum.getMessageValueOf(errorOcapi.getFault().getType())
						.getMessageCustom();

				traceMessage = errorOcapi.getFault().getMessage();
				
				if(errorOcapi.getFault().getType().equals(CustomerMensajesEnum.ERROR_HOOK_EXCEPTION.getMessageSf())) {
					if(Optional.ofNullable(  errorOcapi.getFault().getArguments() ).isPresent() &&
							Optional.ofNullable(  errorOcapi.getFault().getArguments().getStatusDetails() ).isPresent() &&							
							Optional.ofNullable(  errorOcapi.getFault().getArguments().getStatusDetails().getError() ).isPresent() ) {
						message= errorOcapi.getFault().getArguments().getStatusDetails().getError().getMessage();						
					}
				}
				
			}
			ErrorResponse err = new ErrorResponse(exceptionResponseGeneral.getStatus(), message, traceMessage, null);
			return new ResponseEntity<>(err, HttpStatus.valueOf(exceptionResponseGeneral.getStatus()));
	}


	private boolean insertarAddressMembresia(String authorization  ,String customer_id ,ObjectMapper objectMapper,String resultJsonCustomer) {
		
		boolean isUpdateResult=false;
		try {

			Customer customer = objectMapper.readValue(resultJsonCustomer, Customer.class) ;            			
			String numeroMembresia=customer.getCMembershipNumber() ;    								
			String customerNo=customer.getCustomerNo();
										
			ResultAddress resultadoDireccionMembresia=getObjectResultadoDireccionMembresia(authorization, customer_id, objectMapper);
			
			if(Optional.ofNullable(resultadoDireccionMembresia).isPresent() 
					&& Optional.ofNullable(resultadoDireccionMembresia.getCount()).isPresent()
					&& resultadoDireccionMembresia.getCount().intValue() <= 0 ) {
				log.info("No se encontraron direcciones de membresia");
								       		       	        	        	
	        	MembershipDetailsResp membresia=getObjetcDetailsMembresia(customerNo, numeroMembresia, objectMapper);
	         	if(Optional.ofNullable(membresia).isEmpty() ) {
	        		log.error("No se pudo obtener detalle de membresia ");
	        		return isUpdateResult;
	        	}	        	
	        	MembershipAddressResp dirMercurioMembership = membresia.getMembershipAddress();	
	        	
	    		String apellidPaterno=Optional.ofNullable( customer.getLastName() ).isPresent() ? customer.getLastName() : customer.getCFatherName();
				String apellidoMaterno=Optional.ofNullable( customer.getCMotherName() ).isPresent() ? customer.getCMotherName():"";
								
	        	Address direccion=new Address();
	        	direccion.setTitle("Membership");
				direccion.setAddress1(dirMercurioMembership.getStreetName());
				direccion.setAddress2(dirMercurioMembership.getColonia());
				direccion.setCity(dirMercurioMembership.getCity());
				direccion.setFirstName(customer.getFirstName());		
				direccion.setLastName( apellidPaterno+" "+apellidoMaterno );
				direccion.setSecondName(apellidPaterno);
				direccion.setPhone(Optional.ofNullable(customer.getPhoneHome()).isPresent() ? customer.getPhoneHome() : customer.getPhoneMobile() );
				direccion.setPostalCode(dirMercurioMembership.getPostalCode());
				direccion.setStateCode(dirMercurioMembership.getState());
				direccion.setCColonia(dirMercurioMembership.getColonia());
				direccion.setStreetNumber(dirMercurioMembership.getStreetNumber());
				direccion.setCBuildingNumber(dirMercurioMembership.getBuildingNumber());
				direccion.setCReference(dirMercurioMembership.getDetails());
				
				String jsonMembresia=objectMapper.writeValueAsString(direccion);
				Object nodoMembrsia= objectMapper.readValue(jsonMembresia, Object.class);
				ResponseEntity<String> respuestaAddressAdded = customerAddressClient.addAddressCustomer(authorization, "membership-address", nodoMembrsia,customer_id);
		    	
				if(!respuestaAddressAdded.getStatusCode().is2xxSuccessful() ||
	        			Optional.ofNullable(respuestaAddressAdded.getBody()).isEmpty()
	        			) {
	        		log.error("respuestaAddressAdded detalle de membresia incorrecto ");
	        		return isUpdateResult;
	        	}
				isUpdateResult=true;
			}						
		}catch(Exception e){
			log.error("Error insertarAddressMembresia -- "+e.getMessage());
		}
		return isUpdateResult;
	}
	
	private ResultAddress getObjectResultadoDireccionMembresia(String authorization,String customer_id,ObjectMapper objectMapper) {
		ResultAddress resultAddress=new ResultAddress();
		resultAddress.setTotal(1);
		resultAddress.setCount(1);		
		try {
			ResponseEntity<String> respuestaMembresiaAddress = customerAddressClient.getAllAddressCustomer(authorization, "membership-address", customer_id);
	
			String resultJsonRespuesta = respuestaMembresiaAddress.getBody().toString();				
			resultAddress=objectMapper.readValue(resultJsonRespuesta, ResultAddress.class) ;			
		}catch (ExceptionResponseGeneral exceptionResponseGeneral) {
 			log.info("error getObjectResultadoDireccionMembresia"+exceptionResponseGeneral.getMessage());
 			//Si es 400 setea que no existe direccion de membresia 
 			if( exceptionResponseGeneral.getStatus() == 400 ) {
 				resultAddress.setTotal(0);
 				resultAddress.setCount(0);
 			}
 		} catch (Exception exception) {
 			log.info(" Exception getObjectResultadoDireccionMembresia"+exception.getMessage());

 			resultAddress=new ResultAddress();
 			resultAddress.setTotal(1);
 			resultAddress.setCount(1);
 		}   			
		return resultAddress;
	}
	
	private MembershipDetailsResp getObjetcDetailsMembresia(String customerNo,String numeroMembresia,ObjectMapper objectMapper) {
		MembershipDetailsResp membresia=null;
		try {			
        	HttpHeaders headers = customPreferencesUtils.getHeadersTokenCustomPreferences();        	
        	
        	headers=new HttpHeaders();
//            headers.add("Ocp-Apim-Subscription-Key", "116e6dd99836402598b482d66fb2ba0d");

        	MembershipsReq membershipsReq=new MembershipsReq();
        	membershipsReq.setAction("2");
        	membershipsReq.setClientId(customerNo);
        	membershipsReq.setMembershipID(numeroMembresia);   
        	
//        	membershipsReq.setClientId("03183702");
//        	membershipsReq.setMembershipID("1032053064202");   
        	
         	
			ResponseEntity<String> responseDataDetalleMembresia = membershipClient.getMembershipDetails(headers, membershipsReq);        	
        	if(Optional.ofNullable(responseDataDetalleMembresia.getBody()).isEmpty()) {
        		log.error("responseDataDetalleMembresia detalle de membresia incorrecto ");
        	}
        	String resultJsonMembresia=responseDataDetalleMembresia.getBody().toString();        	        	
        	membresia=objectMapper.readValue(resultJsonMembresia,MembershipDetailsResp.class);
        	if(Optional.ofNullable(membresia).isPresent() && 
        			Optional.ofNullable(membresia.getCveRespCode()).isPresent() && 
        			( membresia.getCveRespCode().equals("0")|| membresia.getCveRespCode().equals("00"))
        			) {
        		
        		return membresia;
        	}
        	membresia=null;
		}catch (ExceptionResponseGeneral exceptionResponseGeneral) {
    		log.error("responseDataDetalleMembresia detalle de membresia incorrecto excepcion general");
 			//Si es 400 setea que no existe direccion de membresia 
 		} catch (Exception exception) {
 			log.error("exception "+exception.getMessage());
 			log.error("ERROR GENERAL DETAILS MEMBRESIA");
 		}  
		return membresia;
	}
	
	private void eliminaBasket(String authorization  ,String customer_id){
		try {
			ResponseEntity<?>  responseEntity = customerGetBasketsClient.getBasketsCustomer(authorization, null,customer_id);
			if (responseEntity!=null && responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody()!=null){
				GeneralBasket res = new Gson().fromJson(new Gson().toJson(responseEntity.getBody()),GeneralBasket.class);
				if (res.getBaskets().size()>0){
					for(Basket bk : res.getBaskets()){
						customerGetBasketsClient.deleteBasketsCustomer(authorization,bk.getBasketId());
					}
				}
			}
		}catch(Exception e){
			System.err.println("Error en eliminar basket : " + e.toString() );
		}
	}

	public ResponseEntity<?> deleteCustomer(String authorization,String customer_id)
	{
		ResponseEntity<?>  responseEntity = null;
		ResponseEntity<?>  responseEntityCustomer = null;
		HashMap<String,Object> map = new HashMap<>();
		JsonNode jsonRespuesta = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			//asegurarnos que el customer sea correcto
			responseEntityCustomer= customerDataClient.getCustomerById(authorization ,customer_id, map);
			String response = responseEntityCustomer.getBody().toString();
			jsonRespuesta = objectMapper.readTree(response);
			jsonRespuesta.get("customer_no");
			String customer_no = jsonRespuesta.get("customer_no").toString();
			customer_no = customer_no.replace("\"", "");;


			if (customer_no.equals("00049112"))
			{
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("message","El cliente no puede ser eliminado"));
			}

            if(!jsonRespuesta.get("c_membershipLvl").isNull())
			{
				//HttpHeaders headers = customPreferencesUtils.getHeadersTokenCustomPreferences();
				String perfil = env.getProperty("cityclub-deployment-profile");
				HttpHeaders headers=new HttpHeaders();
				if(perfil.equals("prod"))
				{
					headers.add("Ocp-Apim-Subscription-Key", "664e1515be9d4c9db7b39943953673e1");
				}
				else
				{
					headers.add("Ocp-Apim-Subscription-Key", "05241f7fbb224ddfbc8b848f8e2f1593");
				}
				//Desvinculamos cuenta
				ResponseEntity<?> terminationMembership;
				try {
					Map<String,String> clientId = new HashMap<>();
					clientId.put("ClientId",customer_no);

					if (customer_no.equals("00048955"))
					{
						authorization ="Bearer asvsda";
						terminationMembership = membershipTerminationClient.terminationMembership(headers,authorization,clientId);
					}
					else{
						terminationMembership = membershipTerminationClient.terminationMembership(headers,authorization,clientId);
					}
					JsonNode jsonTermination;
					String respTermination = terminationMembership.getBody().toString();
					jsonTermination = objectMapper.readTree(respTermination);

					if (!"00".equals(jsonTermination.get("Cve_RespCode").asText())) {
						return ResponseEntity
								.status(HttpStatus.UNPROCESSABLE_ENTITY)
								.body(Map.of(
										"error", "Error en la desvinculación",
										"mensaje", jsonTermination.get("Desc_MensajeError").asText(),
										"codigo", jsonTermination.get("Cve_RespCode").asText()
								));
					}
				}
				catch (ExceptionResponseGeneral ex) {
					int status = ex.getStatus();
					if (status >= 400 && status < 500) {
						return ResponseEntity
								.status(HttpStatus.BAD_REQUEST)
								.body("Error del cliente: " + ex.getMessage());
					}
					if (status >= 500) {
						return ResponseEntity
								.status(HttpStatus.INTERNAL_SERVER_ERROR)
								.body("Error en servicio externo de desvinculación: " + ex.getMessage());
					}
				} catch (JsonProcessingException ex) {
					return ResponseEntity
							.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Error al parsear la respuesta de desvinculación: " + ex.getMessage());
				} catch (Exception ex) {
					return ResponseEntity
							.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Error inesperado: " + ex.getMessage());
				}
			}

			OauthModel modelOauth = oauthServiceBM.getOauthEntity();
			String tokenbm="Bearer "+modelOauth.accessToken();
			String client_id = env.getProperty("cityclub-consumer-key");
			//procedemos a eliminar el cliente por customer_no
			responseEntity = customerDataSitesSite.deleteCustomerByCustomerNo(tokenbm,client_id,customer_no);
		}
		catch (ExceptionResponseGeneral e)
		{
			if (e.getStatus() >= 400 && e.getStatus() <= 499) {
				return new ResponseEntity<>(e.getBody(),HttpStatus.BAD_REQUEST);
			}
			if (e.getStatus() == 500){
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
		HashMap<Object, Object> status =  new HashMap<>();
		status.put("statusCodeValue", 200);

		return new ResponseEntity<>(status,HttpStatus.OK);
	}
}
