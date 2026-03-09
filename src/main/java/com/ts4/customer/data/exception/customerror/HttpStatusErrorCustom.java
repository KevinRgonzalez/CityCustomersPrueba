package com.ts4.customer.data.exception.customerror;

import lombok.Getter;

@Getter
public enum  HttpStatusErrorCustom {
	BAD_REQUEST(400, "Solicitud erronea"),
    NOT_FOUND(404, "Recurso no encontrado"),
    INTERNAL_SERVER_ERROR(500, "Error interno del servidor");
	
	private final int code;
	private final String reason;

	HttpStatusErrorCustom(int code, String reason) {
	        this.code = code;
	        this.reason = reason;
	}

}
