package com.ts4.customer.data.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts4.customer.data.model.OauthModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class OauthServiceBM {

	private String consumerSecret;
	private String consumerKey;

	@Autowired 
	private Environment env;
	
	

	public OauthServiceBM(@Value("${cityclub-consumer-key}") String consumerSecret,
                          @Value("${cityclub-consumer-secret}") String consumerKey) {
		
		super();
		this.consumerSecret = consumerSecret;
		this.consumerKey = consumerKey;
	}
	
	
	public OauthModel getOauthEntity() {
	    
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        try {
        
	        RestTemplate restTemplate = new RestTemplate();
	
	        // Define la URL de la API a la que deseas acceder
	        String apiUrl = env.getProperty("cityclub.url.ouath-bsmanager");
	        log.info("URL PRINTING "+apiUrl);
	       // String base64Credentials = Base64.getEncoder().encodeToString(consumerKey.getBytes(StandardCharsets.UTF_8));
	        String credentials = consumerSecret + ":" + consumerKey;
	       
	        
	        //userbm: ${cityclub-consumer-user-bm}
	        //passbm: ${cityclub-consumer-creed-bm}:${cityclub-consumer-secret}
	        
	        
	        //String credentials = "lpacheco@ts4.mx" + ":" + "Edy333221Edy:C1tyCl7b_2023#";
	        log.info("--->consumerSecret --- "+consumerSecret + ">>> consumerKey  "+consumerKey);
	        byte[] credentialsBytes = credentials.getBytes(StandardCharsets.UTF_8);
	        String base64Credentials = Base64.getEncoder().encodeToString(credentialsBytes);
	        String basic= "Basic " + base64Credentials;	        

	        // Crea un objeto HttpHeaders para configurar la autenticación Basic
	        HttpHeaders headers = new HttpHeaders();
	    //    headers.set("Authorization", "Basic bHBhY2hlY29AdHM0Lm14OkVkeTMzMzIyMUVkeTpDMXR5Q2w3Yl8yMDIzIw==");
	        headers.set("Authorization", basic);
	
	        // Crea los datos a enviar en el cuerpo
	        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
	        requestBody.add("grant_type", "client_credentials");
	
	        // Crea una solicitud HTTP con los encabezados y el cuerpo
	        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
	
	        // Realiza la solicitud POST
	        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
	
	        // Obtiene la respuesta del servidor
	         HttpStatusCode statusCode = responseEntity.getStatusCode();
	        String responseBody = responseEntity.getBody();
	   
			return objectMapper.readValue(responseBody,OauthModel.class);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			log.error("### Error en el llamado a segundo Auth");
		}        
		return null;
	}
		
	
}
