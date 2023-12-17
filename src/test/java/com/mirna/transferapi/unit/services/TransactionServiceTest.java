package com.mirna.transferapi.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.Transaction;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.InsufficientBalanceException;
import com.mirna.transferapi.exceptions.SenderUserTypeInvalidException;
import com.mirna.transferapi.exceptions.UnauthorizedTransactionException;
import com.mirna.transferapi.repositories.TransactionRepository;
import com.mirna.transferapi.services.AuthorizationService;
import com.mirna.transferapi.services.TransactionService;
import com.mirna.transferapi.services.UserService;

@DataJpaTest
@ActiveProfiles("test")
public class TransactionServiceTest {

	@InjectMocks
	private TransactionService transactionService;
	
	@Mock
	private TransactionRepository transactionRepository;
	
	@Mock
	private UserService userService;
	
	@Mock
	private AuthorizationService authorizationService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DisplayName("Should add transaction successfully")
	public void testAddTransactionSuccess() throws Exception {

		TransactionDTO transactionDTO = getTransactionDTO();
		
		Transaction transaction = getTransactionEntity();
		
		User sender = getSender();
		User receiver = getReceiver();
		
		when(userService.fetchUser(transactionDTO.getSenderDocument())).thenReturn(sender);
		when(userService.fetchUser(transactionDTO.getReceiverDocument())).thenReturn(receiver);
		
		when(authorizationService.isTransactionAuthorized()).thenReturn(true);
		
		when(userService.updateUser(sender, sender.getDocument())).thenReturn(sender);
		when(userService.updateUser(receiver, receiver.getDocument())).thenReturn(receiver);
		
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
		
		Transaction transactionAdded = transactionService.addTransaction(transactionDTO);
		
		assertThat(transactionAdded).isNotNull();
	}
	
	@Test
	@DisplayName("Should throw exception if any user does not exist when adding the transaction")
	public void testAddTransactionUserDoesNotExistFailure() throws Exception {

		TransactionDTO transactionDTO = getTransactionDTO();
		
		when(userService.fetchUser(any(String.class))).thenThrow(EntityNotPresentException.class);
		
		assertThrows(EntityNotPresentException.class, () -> transactionService.addTransaction(transactionDTO));
	}
	
	@Test
	@DisplayName("Should throw exception if sender user type is shopkeeper when adding the transaction")
	public void testAddTransactionSenderUserTypeInvalidFailure() throws Exception {

		TransactionDTO transactionDTO = getTransactionDTO();
		
		Transaction transaction = getTransactionEntity();
		
		User sender = getSender();
		sender.setUserType(UserType.SHOPKEEPER);
		
		User receiver = getReceiver();
		
		when(userService.fetchUser(transactionDTO.getSenderDocument())).thenReturn(sender);
		when(userService.fetchUser(transactionDTO.getReceiverDocument())).thenReturn(receiver);
		
		assertThrows(SenderUserTypeInvalidException.class, () -> transactionService.addTransaction(transactionDTO));
	}
	
	@Test
	@DisplayName("Should throw exception if sender has balance below transaction amount")
	public void testAddTransactionSenderBalanceInvalidFailure() throws Exception {

		TransactionDTO transactionDTO = getTransactionDTO();
		
		Transaction transaction = getTransactionEntity();
		
		User sender = getSender();
		sender.setBalance(new BigDecimal(50));
		
		User receiver = getReceiver();
		
		when(userService.fetchUser(transactionDTO.getSenderDocument())).thenReturn(sender);
		when(userService.fetchUser(transactionDTO.getReceiverDocument())).thenReturn(receiver);
		
		assertThrows(InsufficientBalanceException.class, () -> transactionService.addTransaction(transactionDTO));
	}
	
	@Test
	@DisplayName("Should throw exception if transaction is not authorized")
	public void testAddTransactionUnauthorizedFailure() throws Exception {

		TransactionDTO transactionDTO = getTransactionDTO();
		
		Transaction transaction = getTransactionEntity();
		
		User sender = getSender();
		User receiver = getReceiver();
		
		when(userService.fetchUser(transactionDTO.getSenderDocument())).thenReturn(sender);
		when(userService.fetchUser(transactionDTO.getReceiverDocument())).thenReturn(receiver);
		
		when(authorizationService.isTransactionAuthorized()).thenReturn(false);
		
		assertThrows(UnauthorizedTransactionException.class, () -> transactionService.addTransaction(transactionDTO));
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
		transactionDTO.setAmount(new BigDecimal(1000));
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
