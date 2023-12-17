package com.mirna.transferapi.integration.controllers;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.Transaction;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.exceptions.SenderUserTypeInvalidException;
import com.mirna.transferapi.repositories.TransactionRepository;
import com.mirna.transferapi.repositories.UserRepository;
import com.mirna.transferapi.security.auth.util.PasswordEncryptorUtil;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionControllerIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	ObjectMapper mapper;

	@BeforeAll
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

		User user3 = new User();
		user3.setEmail("store123@gmail.com");
		user3.setFirstName("Store");
		user3.setLastName("Test");
		user3.setDocument("333333333");
		user3.setBalance(new BigDecimal(1000));
		user3.setPassword(PasswordEncryptorUtil.encryptPassword("p4ss0wrd"));
		user3.setUserType(UserType.SHOPKEEPER);

		User user4 = new User();
		user4.setEmail("josh@gmail.com");
		user4.setFirstName("Josh");
		user4.setLastName("Test");
		user4.setDocument("444444444");
		user4.setBalance(new BigDecimal(50));
		user4.setPassword(PasswordEncryptorUtil.encryptPassword("p4ss0wrd"));
		user4.setUserType(UserType.COMMON);

		userRepository.save(user);
		userRepository.save(user2);
		userRepository.save(user3);
		userRepository.save(user4);
	}
	
	@AfterAll
	public void terminate() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();
	}
	
	@Test
	@DisplayName("Should return http status ok when adding transaction successfully")
	public void testAddTransaction() throws Exception {

		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setAmount(new BigDecimal(100));
		transactionDTO.setReceiverDocument("111111111");
		transactionDTO.setSenderDocument("222222222");

		String transactionDTOContent = mapper.writeValueAsString(transactionDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/transactions").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(transactionDTOContent))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	@DisplayName("Should return http status not found when adding transiction with non-existent user")
	public void testAddTransactionFailure() throws Exception {

		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setAmount(new BigDecimal(100));
		transactionDTO.setReceiverDocument("111111111");
		transactionDTO.setSenderDocument("4444444444");

		String transactionDTOContent = mapper.writeValueAsString(transactionDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/transactions").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(transactionDTOContent))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	@DisplayName("Should return http status unprocessable entity when adding transiction with sender with invalid user type")
	public void testAddTransactionSenderUserTypeInvalidFailure() throws Exception {

		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setAmount(new BigDecimal(100));
		transactionDTO.setReceiverDocument("111111111");
		transactionDTO.setSenderDocument("333333333");

		String transactionDTOContent = mapper.writeValueAsString(transactionDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/transactions").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(transactionDTOContent))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity()).andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Should return http status not acceptable when adding transiction with sender balance below the amount")
	public void testAddTransactionSenderInsufficientBalanceFailure() throws Exception {

		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setAmount(new BigDecimal(100));
		transactionDTO.setReceiverDocument("111111111");
		transactionDTO.setSenderDocument("444444444");

		String transactionDTOContent = mapper.writeValueAsString(transactionDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/transactions").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(transactionDTOContent))
				.andExpect(MockMvcResultMatchers.status().isNotAcceptable()).andDo(MockMvcResultHandlers.print());
	}

}
