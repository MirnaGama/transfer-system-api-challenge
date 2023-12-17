package com.mirna.transferapi.unit.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.mirna.transferapi.external.gateway.HttpAuthorizationGateway;
import com.mirna.transferapi.services.AuthorizationService;

@DataJpaTest
@ActiveProfiles("test")
public class AuthorizationServiceTest {

	@InjectMocks
	private AuthorizationService authorizationService;
	
	@Mock
	private HttpAuthorizationGateway authorizationGateway;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	

	@Test
    @DisplayName("Should verify if request is AUTHORIZED")
	public void testIsAuthorized(){
		
	 when(authorizationGateway.getAuthorization()).thenReturn(authorizationService.getAuthorizationExpectedMessage());
	 
	 boolean isTransactionAuthorized = authorizationService.isTransactionAuthorized();

	 verify(authorizationGateway, times(1)).getAuthorization();
	 
	 assertTrue(isTransactionAuthorized);
	 
	 }
}