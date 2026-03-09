package com.ts4.customer.data.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
	//private final Logger LOGGER = LogManager.getLogger(CustomErrorDecoder.class.getName());
	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {
		int status = response.status();
		String reason = response.reason();
		Map<String, Collection<String>> headers = response.headers();
		String body = "";
		String mensajeFuncional = "";
		if (response.body() != null) {
			try (Reader reader = new BufferedReader(
					new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder sb = new StringBuilder();
				int c;
				while ((c = reader.read()) != -1) {
					sb.append((char) c);
				}
				body = sb.toString();
				log.info("Feign error body: {}", body);
				try {
					JsonNode json = mapper.readTree(body);
					if (json.hasNonNull("message")) {
						mensajeFuncional = json.get("message").asText();
					} else if (json.hasNonNull("Desc_MensajeError")) {
						mensajeFuncional = json.get("Desc_MensajeError").asText();
					}
				} catch (Exception e) {
					log.warn("Body no es JSON válido");
				}

			} catch (IOException e) {
				log.warn("No se pudo leer el body de error", e);
			}
		}
		if (status >= 400 && status < 500) {
			return new ExceptionResponseGeneral(
					status,
					reason,
					headers,
					body,
					mensajeFuncional
			);
		}
		return new ExceptionResponseGeneral(
				status,
				reason,
				headers,
				body,
				"Error en servicio externo"
		);
	}
}

