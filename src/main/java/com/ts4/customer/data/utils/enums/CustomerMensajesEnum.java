package com.ts4.customer.data.utils.enums;


import lombok.Getter;

@Getter
public enum CustomerMensajesEnum {
	CUSTOMER_EXIST("CustomerAlreadyRegisteredException","El usuario ya esta registrado"),
	MISSING_EMAIL("MissingEmailException","Debes capturar el email"),
	MISSING_LASTNAME("MissingLastNameException","Catura apellido materno"),
	MISSING_LOGIN("MissingLoginException","Captura el login del usuario"),
	LOGIN_ALREADY_IN_USE("LoginAlreadyInUseException","El correo ya esta en uso"),
	INVALID_LOGIN("InvalidLoginException","El campo login no cumple con los criterios"),
	INVALID_PASSWORD("InvalidPasswordException","El password no cumple con los criterios"),
	MISSIN_PASSWORD("MissingPasswordException","No se ha definido un password"),
	
	//Update customer
	INVALID_CUSTOMER("InvalidCustomerException","Usuario proporcionado incorrecto "),
	INVALID_EMAIL("InvalidEmailException","El correo es incorrecto"),
	CUSTOMER_NOT_FOUND("CustomerNotFoundException","El usuario proporcionado no se ha encontrado"),
	 
	INVALID_TOKEN_EXCEPTION("InvalidAccessTokenException","El token es incorrecto"),
	EXPIRED_TOKEN_EXCEPTION("ExpiredTokenException","El token ha expirado"),
	ERROR_NOT_HANDLE("ErrorNotHandle","Error interno del servidor"),	
	ERROR_HOOK_EXCEPTION("HookStatusException","Error interno del servidor");	

	private final String messageSf;
	private final String messageCustom;

	CustomerMensajesEnum( String messageSf,String messageCustom) {
	        this.messageSf = messageSf;
	        this.messageCustom = messageCustom;
	}	
	
	public static CustomerMensajesEnum getMessageValueOf(String messageSf) {
		for(CustomerMensajesEnum enumInd:CustomerMensajesEnum.values()) {
			if(enumInd.messageSf.equalsIgnoreCase(messageSf)) {
				return enumInd;
			}
		}
		return ERROR_NOT_HANDLE;
	}
}
