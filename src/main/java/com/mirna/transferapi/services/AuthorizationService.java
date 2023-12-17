package com.mirna.transferapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mirna.transferapi.external.gateway.HttpAuthorizationGateway;

@Service
public class AuthorizationService {

	@Autowired
	private HttpAuthorizationGateway authorizationGateway;
	
	private static final String AUTHORIZATION_EXPECTED_MESSAGE = "Autorizado";
	 
	public boolean isTransactionAuthorized() {
		return authorizationGateway.getAuthorization()
                .equalsIgnoreCase(getAuthorizationExpectedMessage());
	}
	
	public String getAuthorizationExpectedMessage() {
		return AUTHORIZATION_EXPECTED_MESSAGE;
	}
}
