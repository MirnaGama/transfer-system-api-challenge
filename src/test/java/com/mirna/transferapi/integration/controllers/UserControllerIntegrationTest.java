package com.mirna.transferapi.integration.controllers;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import com.mirna.transferapi.domain.dtos.UserDTO;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.repositories.UserRepository;
import com.mirna.transferapi.security.auth.util.PasswordEncryptorUtil;
import com.mirna.transferapi.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ObjectMapper mapper;

	@BeforeEach
	public void init() {
		User user = new User();
		user.setEmail("test@gmail.com");
		user.setFirstName("Test");
		user.setLastName("Test");
		user.setDocument("0123456");
		user.setBalance(new BigDecimal(1000));
		user.setPassword(PasswordEncryptorUtil.encryptPassword("p4ss0wrd"));
		user.setUserType(UserType.COMMON);

		userRepository.save(user);
	}

	@AfterEach
	public void terminate() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("Should get user by document")
	public void testGetUserByDocument() throws Exception {

		String document = "0123456";

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/" + document))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	@DisplayName("Should not get user by document when it does not exist")
	public void testGetUserByDocumentFailure() throws Exception {

		String document = "789101112";

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/" + document))
				.andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());

	}

	@Test
	@DisplayName("Should add new user")
	public void testAddUser() throws Exception {

		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("john@gmail.com");
		userDTO.setDocument("111111111111");
		userDTO.setFirstName("John");
		userDTO.setLastName("Doe");
		userDTO.setPassword("p4ss0wrd");
		userDTO.setUserType("COMMON");

		String userDTOContent = mapper.writeValueAsString(userDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(userDTOContent)).andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	@DisplayName("Should not add new user with existing document")
	public void testAddUserDocumentFailure() throws Exception {

		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("mary@gmail.com");
		userDTO.setDocument("0123456");
		userDTO.setFirstName("Mary");
		userDTO.setLastName("Sue");
		userDTO.setPassword("p4ss0wrd");
		userDTO.setUserType("COMMON");

		String userDTOContent = mapper.writeValueAsString(userDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(userDTOContent)).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	@DisplayName("Should not add new user with existing email")
	public void testAddUserEmailFailure() throws Exception {

		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("test@gmail.com");
		userDTO.setDocument("0123456");
		userDTO.setFirstName("Mary");
		userDTO.setLastName("Sue");
		userDTO.setPassword("p4ss0wrd");
		userDTO.setUserType("COMMON");

		String userDTOContent = mapper.writeValueAsString(userDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/users").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(userDTOContent)).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andDo(MockMvcResultHandlers.print());

	}

}
