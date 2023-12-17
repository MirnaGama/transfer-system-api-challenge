package com.mirna.transferapi.external.gateway;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.mirna.transferapi.external.gateway.base.HttpBaseGateway;

@Component
public class HttpAuthorizationGateway {
	
	@Autowired
	private HttpBaseGateway baseGateway;
	
	@Value("${app.external.api.authorization.url}")
	private String AUTHORIZATION_TRANSACTION_URL;
	
	public String getAuthorization() {
		ResponseEntity<Map> authorizationTransactionResponse = baseGateway.get(AUTHORIZATION_TRANSACTION_URL);
		
		 if(authorizationTransactionResponse.getStatusCode() != HttpStatus.OK) {
	            return "";
	     }
		 
		 return (String) Objects.requireNonNull(authorizationTransactionResponse.getBody()).get("message");
	}
}