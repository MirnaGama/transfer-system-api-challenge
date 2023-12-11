package com.mirna.transferapi.integration.controllers;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
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

		userRepository.save(user);
		userRepository.save(user2);
	}
	
	@Test
	@DisplayName("Should add new transaction")
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

}
