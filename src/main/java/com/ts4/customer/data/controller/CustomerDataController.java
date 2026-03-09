package com.ts4.customer.data.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.ts4.customer.data.utils.logs.TrazaPrincipal;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ts4.customer.data.exception.DefaultException;
import com.ts4.customer.data.model.customer.CustomerRegistration;
import com.ts4.customer.data.model.orderproducts.request.ProductsOrderReq;
import com.ts4.customer.data.service.CustomerDataService;
import com.ts4.customer.data.service.CustomerGetBasketsService;
import com.ts4.customer.data.service.CustomerProductsOrdersService;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/customers")
public class CustomerDataController {
	private final Tracer tracer;

	public CustomerDataController(Tracer tracer) {
		this.tracer = tracer;
	}

	@Autowired private TrazaPrincipal trazaPrincipal;
	@Autowired private CustomerDataService customerDataService;
	@Autowired private CustomerGetBasketsService customerGetBasketsService;
	@Autowired private CustomerProductsOrdersService customerProductsOrdersService;

	/**
	 * Maneja las solicitudes GET para recuperar los términos y condiciones.
	 *
	 * @param authorizationHeader el encabezado de autorización que contiene las credenciales del usuario
     * @return un ResponseEntity que contiene los términos y condiciones o un mensaje de error
	 */
	@GetMapping("/terminos")
	public ResponseEntity<?> getTerminosCondiciones(@RequestHeader("Authorization") String authorizationHeader ) {
		log.info("terminos");
		try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerDataService.getTerminosCondiciones( authorizationHeader);
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorizationHeader);
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
		} catch (Exception e) {
			throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/**
	 * 
	 * @param authorizationHeader
	 * //@param sfcc-cc-action header  Opcional (became-new-member OR is-cc-member )
	 * 		header = is-cc-member      ya se tiene membresia 
	 * 			customer.c_membershipNumber
	 * 		header = became-new-member requiere ser miembro 
	 * 			Se tiene que enviar customer.c_membershipAddress.postal_code,
	 * 			required customer.c_membershipAddress.city (Alcaldía o Municipio),
	 * 			required customer.c_membershipAddress.state_code ,
	 * 			required customer.c_membershipAddress.c_colonia,
	 * 			required customer.c_membershipAddress.c_streetNumber (numero exterior)
	 * 			optional customer.c_membershipAddress.c_buildingNumber(numero interor)
	 *			optional customer.c_membershipAddress.c_reference (referencias)
	 *
	 * @param params CustomerRegistration.class
	 * @return Customer.class
	 */
	@PostMapping
	public ResponseEntity<?> registerCustomer(@RequestHeader("Authorization") String authorizationHeader ,
											  @RequestHeader(value="sfcc-cc-action",required =false ) String actionCustomer ,
											  @RequestBody Object params) {
		log.info("registerCustomer");
		try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerDataService.registerCustomer( params , authorizationHeader,actionCustomer);
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorizationHeader);
			mapParam.put("actionCustomer",actionCustomer);
			mapParam.put("http.requestPrincipal",new Gson().toJson(params));
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
		} catch (Exception e) {
			throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	/**
	 * 
	 * @param authorizationHeader
	 * //@param sfcc-cc-action header Opcional (resend-account-activation-email OR membership-link )
	 * 		header = resend-account-activation-email  Solicitud reactivacion
	 * 				body empty{}
	 * 		header = became-new-member es porque quiere ser miembro 
	 * 			required c_membershipNumber numero de membresia
	 *
	 * //@param params CustomerRegistration.class
	 * @return Customer.class
	 */
	@PatchMapping("/{customer_id}")
	public ResponseEntity<?> updateCustomerById(@RequestHeader("Authorization") String authorizationHeader ,
												@RequestHeader(value="sfcc-cc-action",required =false ) String actionCustomer ,
												@PathVariable("customer_id")    String customer_id,
												@RequestBody(required = false) Object customer ) {
		log.info("updateCustomerById");
		try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerDataService.updateCustomerById(  authorizationHeader  , actionCustomer, customer_id, customer );
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorizationHeader);
			mapParam.put("actionCustomer",actionCustomer);
			mapParam.put("customer_id",customer_id);
			mapParam.put("http.requestPrincipal",new Gson().toJson(customer));
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
		} catch (Exception e) {
			throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	/**
	 * Maneja las solicitudes GET para recuperar la información de un cliente por su ID.
	 *
	 * @param authorizationHeader el encabezado de autorización que contiene las credenciales del usuario
	 * @param customer_id el ID del cliente
	 * @param queryParams los parámetros de consulta adicionales
	 * @return un ResponseEntity que contiene la información del cliente o un mensaje de error
	 */
	@GetMapping("/{customer_id}")
	public ResponseEntity<?> getCustomerById(@RequestHeader("Authorization") String authorizationHeader ,
											 @PathVariable("customer_id")    String customer_id,
											 @RequestParam Map<String,Object> queryParams ) {
		log.info("getCustomerById");
		try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerDataService.getCustomerById( authorizationHeader , customer_id,queryParams);
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorizationHeader);
			mapParam.put("customer_id",customer_id);
			mapParam.put("http.requestPrincipal",new Gson().toJson(queryParams));
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
		} catch (Exception e) {
			throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	/**
	 * Maneja las solicitudes POST para restablecer la contraseña de un cliente.
	 *
	 * @param params los parámetros del cuerpo de la solicitud
	 * @param queryParams los parámetros de consulta adicionales
	 * @return un ResponseEntity que contiene el resultado del restablecimiento de la contraseña o un mensaje de error
	 */
	@PostMapping("/password_reset")
	public ResponseEntity<?> setPasswordReset(@RequestBody Object params , @RequestParam Map<String,Object> queryParams) {
		log.info("password reset");
		try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerDataService.setPasswordReset( params ,queryParams );
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("params",queryParams);
			mapParam.put("http.requestPrincipal",new Gson().toJson(params));
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
		} catch (Exception e) {
			throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	/**
	 * Maneja las solicitudes GET para recuperar las cestas de un cliente (versión 2).
	 *
	 * @param authorization el encabezado de autorización que contiene las credenciales del usuario
	 * @param actionBasket el encabezado opcional que especifica la acción de la cesta
	 * @param sfccAction el encabezado opcional que especifica la acción de SFCC
	 * @param fullData el encabezado opcional que especifica si se deben recuperar todos los datos
	 * @param customer_id el ID del cliente
	 * @return un ResponseEntity que contiene las cestas del cliente o un mensaje de error
	 */
	@GetMapping("/{customer_id}/basketsv2")
	public ResponseEntity<?> getBasketsCustomer(
			@RequestHeader("Authorization") String authorization,
			@RequestHeader(value="sf-action-basket",required =false ) String actionBasket ,
            @RequestHeader(value="sfcc-cc-action",required =false ) String sfccAction ,
			@RequestHeader(value="fullData",required =false ) Boolean fullData,
			@PathVariable("customer_id") String customer_id) {
		try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerGetBasketsService.getBasketsCustomer(authorization , actionBasket , sfccAction,customer_id );
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorization);
			mapParam.put("actionBasket",actionBasket);
			mapParam.put("sfccAction",sfccAction);
			mapParam.put("fullData",fullData);
			mapParam.put("customer_id",customer_id);
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
		} catch (Exception e) {
			throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	/**
	 * Maneja las solicitudes GET para recuperar las cestas de un cliente.
	 *
	 * @param authorization el encabezado de autorización que contiene las credenciales del usuario
	 * @param actionBasket el encabezado opcional que especifica la acción de la cesta
	 * @param sfccAction el encabezado opcional que especifica la acción de SFCC
	 * @param headerIsCuponesDetails el encabezado opcional que especifica si se deben recuperar los detalles de los cupones
	 * @param fullData el encabezado opcional que especifica si se deben recuperar todos los datos
	 * @param customer_id el ID del cliente
	 * @return un ResponseEntity que contiene las cestas del cliente o un mensaje de error
	 */
	@GetMapping("/{customer_id}/baskets")
	public ResponseEntity<?> getBasketsCustomerV2(
	        @RequestHeader("Authorization") String authorization,
	        @RequestHeader(value="sf-action-basket",required =false ) String actionBasket ,
            @RequestHeader(value="sfcc-cc-action",required =false ) String sfccAction ,
            @RequestHeader(value="sfcc-cupones",required =false ) Boolean headerIsCuponesDetails ,
			@RequestHeader(value="fullData",required =false ) Boolean fullData,
	        @PathVariable("customer_id") String customer_id) {
	    try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerGetBasketsService.getBasketsCustomerv2(authorization , actionBasket, sfccAction,fullData,customer_id,headerIsCuponesDetails );
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorization);
			mapParam.put("actionBasket",actionBasket);
			mapParam.put("sfccAction",sfccAction);
			mapParam.put("headerIsCuponesDetails",headerIsCuponesDetails);
			mapParam.put("fullData",fullData);
			mapParam.put("customer_id",customer_id);
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
	    } catch (Exception e) {
	        throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
	    }		
	}

	/**
	 * Maneja las solicitudes POST para recuperar los productos por pedidos de un cliente.
	 *
	 * @param authorization el encabezado de autorización que contiene las credenciales del usuario
	 * @param homeReq el encabezado opcional que especifica si se requiere la entrega a domicilio
	 * @param customer_id el ID del cliente
	 * @param productsOrderReq el cuerpo de la solicitud que contiene los detalles del pedido de productos
	 * @return un ResponseEntity que contiene los productos por pedidos del cliente o un mensaje de error
	 */
	@PostMapping("/{customer_id}/products-orders")
	public ResponseEntity<?> getProductsByOrders(
	        @RequestHeader("Authorization") String authorization,
			@RequestHeader(value = "homeReq",required = false) Boolean homeReq,
	        @PathVariable("customer_id") String customer_id,
	        @RequestBody ProductsOrderReq productsOrderReq) {
	    try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerProductsOrdersService.getProductsOrders(authorization , customer_id , productsOrderReq ,homeReq);
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorization);
			mapParam.put("homeReq",homeReq);
			mapParam.put("customer_id",customer_id);
			mapParam.put("http.requestPrincipal",new Gson().toJson(productsOrderReq));
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
	    } catch (Exception e) {
	        throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
	    }		
	}

	/**
	 * Maneja las solicitudes GET para verificar el estado de salud del servicio.
	 *
	 * @return un ResponseEntity que contiene un mensaje de estado del servicio
	 */
	@GetMapping("healthcheck")
	public ResponseEntity<?> healthcheck() {
		return new ResponseEntity<>("Servicio en linea",HttpStatus.OK);
	}

	/**
	 * Maneja las solicitudes GET para recuperar los pedidos de un cliente.
	 *
	 * @param authorization el encabezado de autorización que contiene las credenciales del usuario
	 * @param customer_id el ID del cliente
	 * @param queryParams los parámetros de consulta adicionales
	 * @param headerIsCuponesDetails el encabezado opcional que especifica si se deben recuperar los detalles de los cupones
	 * @return un ResponseEntity que contiene los pedidos del cliente o un mensaje de error
	 */
	@GetMapping("/{customer_id}/orders")
	public ResponseEntity<?> getOrdersCustomer(@RequestHeader("Authorization") String authorization,
											   @PathVariable("customer_id") String customer_id,
											   @RequestParam Map<String,Object> queryParams ,
											   @RequestHeader(value="sfcc-cupones",required =false ) Boolean headerIsCuponesDetails) {
		try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerGetBasketsService.getOrdersCustomer(authorization,customer_id,queryParams,headerIsCuponesDetails);
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorization);
			mapParam.put("customer_id",customer_id);
			mapParam.put("queryParams",queryParams);
			mapParam.put("headerIsCuponesDetails",headerIsCuponesDetails);
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
		} catch (Exception e) {
			throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	@DeleteMapping("/{customer_id}")
	public ResponseEntity<?> deletCustomer(@RequestHeader("Authorization") String authorization, @PathVariable("customer_id") String customer_id)
	{
		try {
			Span span = tracer.spanBuilder("TRAZA PRINCIPAL").startSpan();
			ResponseEntity<?> response = customerDataService.deleteCustomer(authorization,customer_id);
			Map<String,Object> mapParam = new HashMap<>();
			mapParam.put("Authorization",authorization);
			mapParam.put("customer_id",customer_id);
			mapParam.put("headerIsCuponesDetails",customer_id);
			trazaPrincipal.trazaPrincipal(mapParam, response,span);
			return response;
		} catch (Exception e) {
			throw new DefaultException("Error interno, por favor vuelva a intentar", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
