package com.ts4.customer.data.utils.enums;


import lombok.Getter;

@Getter
public enum OrderMensajesEnum {
		
	INVALID_CUSTOMER("InvalidCustomerException","Usuario proporcionado incorrecto "),
	INVALID_ORDER_STATUS("InvalidOrderStatusException","Estatus de orden incorrecto"),
	CUSTOMER_NOT_FOUND("CustomerNotFoundException","El usuario proporcionado no se ha encontrado"),
	
	INVALID_TOKEN_EXCEPTION("InvalidAccessTokenException","El token es incorrecto"),
	EXPIRED_TOKEN_EXCEPTION("ExpiredTokenException","El token ha expirado"),
	ERROR_NOT_HANDLE("ErrorNotHandle","Error interno del servidor"),
	ERROR_HOOK_EXCEPTION("HookStatusException","Error interno del servidor");	

	private final String messageSf;
	private final String messageCustom;

	OrderMensajesEnum( String messageSf,String messageCustom) {
	        this.messageSf = messageSf;
	        this.messageCustom = messageCustom;

	}	
	
	public static OrderMensajesEnum getMessageValueOf(String messageSf) {
		for(OrderMensajesEnum enumInd:OrderMensajesEnum.values()) {
			if(enumInd.messageSf.equalsIgnoreCase(messageSf)) {
				return enumInd;
			}
		}
		return ERROR_NOT_HANDLE;
	}
}
