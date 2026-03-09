package com.ts4.customer.data.utils.custompreferences;

import java.util.Properties;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts4.customer.data.client.CustomPreferencesClient;
import com.ts4.customer.data.exception.ExceptionResponseGeneral;
import com.ts4.customer.data.service.CustomerDataService;


import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class CustomPreferencesUtils {
    
    private final String INSTANCE_TYPE="cityclub.config.instance_type";      
    private final String GROUP_ID_SORIANA_CONFIG="sorianaConfig";
    private final String PREFERENCE_ID_SORIANA_API_KEY="sorianaApiKey";
    
	@Autowired private Environment env;	   
	@Autowired private CustomPreferencesClient customPreferencesClient;
		
	public HttpHeaders getHeadersTokenCustomPreferences() {		
		ObjectMapper objectMapper=new ObjectMapper();

		HttpHeaders headers = new HttpHeaders();
		
        try {
        	String instance_type=env.getProperty(INSTANCE_TYPE);
        	ResponseEntity<?> responseEntity  = customPreferencesClient.getCustomPreferences(
        										GROUP_ID_SORIANA_CONFIG,instance_type,PREFERENCE_ID_SORIANA_API_KEY	);

			System.err.println(new Gson().toJson(responseEntity));
        	
    		String resultJsonRespuesta = responseEntity.getBody().toString();
    		log.info("data getHeadersTokenCustomPreferences **" + resultJsonRespuesta);
           
            String site_values="site_values";
            String CityClub_C="CityClub_C";

            JsonNode node_custom = objectMapper.readTree(resultJsonRespuesta);
            log.info("NODE_CUSTOM");
            JsonNode node_site_values = node_custom.get(site_values);              
            JsonNode node_city_club = node_site_values.get(CityClub_C);

            String header_name = node_city_club.get("name").asText();
            String value_header = node_city_club.get("value").asText();
            headers.add(header_name,value_header);

        }catch(ExceptionResponseGeneral exception) {
        	log.error("ExceptionResponseGeneral getHeadersTokenCustomPreferences "+exception.getMessage());
        }catch(Exception e) {
        	log.error("Exception getHeadersTokenCustomPreferences "+e.getMessage());
        }

		log.error(new Gson().toJson(headers));

		return headers;
	}
	
}
