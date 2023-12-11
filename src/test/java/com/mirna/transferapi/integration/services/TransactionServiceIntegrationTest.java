package com.mirna.transferapi.integration.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.Transaction;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.repositories.UserRepository;
import com.mirna.transferapi.security.auth.util.PasswordEncryptorUtil;
import com.mirna.transferapi.services.TransactionService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class TransactionServiceIntegrationTest {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	public void init() {
		User user = new User();
		user.setEmail("mary@gmail.com");
		user.setFirstName("Test");
		user.setLastName("Test");
		user.setDocument("111111111");
		user.setBalance(new BigDecimal(1000));
		user.setPassword(PasswordEncryptorUtil.encryptPassword("p4ss0wrd"));
		user.setUserType(UserType.COMMON);

		User user2 = new User();
		user2.setEmail("john@gmail.com");
		user2.setFirstName("Test");
		user2.setLastName("Test");
		user2.setDocument("222222222");
		user2.setBalance(new BigDecimal(1000));
		user2.setPassword(PasswordEncryptorUtil.encryptPassword("p4ss0wrd"));
		user2.setUserType(UserType.COMMON);

		userRepository.save(user);
		userRepository.save(user2);
	}
	
	@Test
	@DisplayName("Should add transaction successfully")
	public void testAddTransaction() throws Exception {

		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setAmount(new BigDecimal(500));
		transactionDTO.setReceiverDocument("111111111");
		transactionDTO.setSenderDocument("222222222");

	    User initialSender = userRepository.findUserByDocument(transactionDTO.getSenderDocument()).get();
	    User initialReceiver = userRepository.findUserByDocument(transactionDTO.getReceiverDocument()).get();
	    
	    assertThat(initialSender).isNotNull();
	    assertThat(initialReceiver).isNotNull();
	    
	    Transaction transaction = transactionService.addTransaction(transactionDTO);

	    assertThat(transaction.getId()).isNotNull();
	    
	    BigDecimal senderFinalBalance = initialSender.getBalance().subtract(transaction.getAmount());
	    BigDecimal receiverFinalBalance = initialReceiver.getBalance().add(transaction.getAmount());
	    
	    User sender = transaction.getSender();
	    User receiver = transaction.getReceiver();
	    
	    assertTrue(sender.getBalance().compareTo(senderFinalBalance) == 0);
	    assertTrue(receiver.getBalance().compareTo(receiverFinalBalance) == 0);
	}
	
}
