package com.mirna.transferapi.unit.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mirna.transferapi.controllers.TransactionController;
import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.Transaction;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.InsufficientBalanceException;
import com.mirna.transferapi.exceptions.SenderUserTypeInvalidException;
import com.mirna.transferapi.exceptions.UnauthorizedTransactionException;
import com.mirna.transferapi.services.TransactionService;

public class TransactionControllerTest {

	@InjectMocks
	private TransactionController transactionController;
	
	@Mock
	private TransactionService transactionService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DisplayName("Should return http status ok when adding transaction successfully")
	public void testAddTransactionSuccess() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

	    TransactionDTO transactionDTO = getTransactionDTO();
	    Transaction transaction = getTransactionEntity();
	     
		when(transactionService.addTransaction(any(TransactionDTO.class))).thenReturn(transaction);

	    ResponseEntity<Object> responseEntity = transactionController.addTransaction(transactionDTO);

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Should return http status not found when adding transiction with non-existent user")
	public void testAddTransactionUserDoesNotExistFailure() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	     
	    when(transactionService.addTransaction(any(TransactionDTO.class))).thenThrow(EntityNotPresentException.class);

	    ResponseEntity<Object> responseEntity = transactionController.addTransaction(mock(TransactionDTO.class));

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Should return http status unprocessable entity when adding transiction with sender with invalid user type")
	public void testAddTransactionSenderUserTypeInvalidFailure() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	    
	    when(transactionService.addTransaction(any(TransactionDTO.class))).thenThrow(SenderUserTypeInvalidException.class);
	    
	    ResponseEntity<Object> responseEntity = transactionController.addTransaction(mock(TransactionDTO.class));

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@Test
	@DisplayName("Should return http status not acceptable when adding transiction with sender with insufficient balance")
	public void testAddTransactionSenderBalanceInvalidFailure() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	    
	    when(transactionService.addTransaction(any(TransactionDTO.class))).thenThrow(InsufficientBalanceException.class);
	    
	    ResponseEntity<Object> responseEntity = transactionController.addTransaction(mock(TransactionDTO.class));

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
	}
	
	@Test
	@DisplayName("Should return http status unauthorized when adding transiction with not authorized request")
	public void testAddTransactionUnauthorizedFailure() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	    
	    when(transactionService.addTransaction(any(TransactionDTO.class))).thenThrow(UnauthorizedTransactionException.class);
	    
	    ResponseEntity<Object> responseEntity = transactionController.addTransaction(mock(TransactionDTO.class));

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	private User getSender() {
		User user = new User();
		user.setId(1L);
		user.setDocument("11111111");
		user.setEmail("john@gmail.com");
		user.setBalance(new BigDecimal(1000));
		user.setPassword("p3ss3rd");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setUserType(UserType.COMMON);
		
		return user;
	}
	
	private User getReceiver() {
		User user = new User();
		user.setId(1L);
		user.setDocument("22222222");
		user.setEmail("mary@gmail.com");
		user.setBalance(new BigDecimal(1000));
		user.setPassword("p3ss3rd");
		user.setFirstName("Mary");
		user.setLastName("Doe");
		user.setUserType(UserType.COMMON);
		
		return user;
	}
	
	private TransactionDTO getTransactionDTO() {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setAmount(new BigDecimal(500));
		transactionDTO.setReceiverDocument("11111111");
		transactionDTO.setSenderDocument("22222222");
		
		return transactionDTO;
	}
	
	private Transaction getTransactionEntity() {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(500));
		transaction.setReceiver(getReceiver());
		transaction.setSender(getSender());
		transaction.setTimestamp(LocalDateTime.now());
		
		return transaction;
	}
	
}
