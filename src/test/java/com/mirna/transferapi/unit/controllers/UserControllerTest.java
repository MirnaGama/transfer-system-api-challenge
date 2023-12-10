package com.mirna.transferapi.unit.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mirna.transferapi.controllers.UserController;
import com.mirna.transferapi.domain.dtos.UserDTO;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.UserDocumentException;
import com.mirna.transferapi.exceptions.UserEmailException;
import com.mirna.transferapi.services.UserService;

@DataJpaTest
@ActiveProfiles("test")
public class UserControllerTest {

	@InjectMocks
	private UserController userController;
	
	@Mock
	private UserService userService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DisplayName("Should add user successfully")
	public void testAddUser() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

	    UserDTO userDTO = getUserDTO();
	     
		when(userService.addUser(any(UserDTO.class))).thenReturn(userDTO);

	    ResponseEntity<Object> responseEntity = userController.addUser(userDTO);

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Should throw exception when adding user with same document")
	public void testAddUserDocumentFailure() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

	    UserDTO userDTO = getUserDTO();
	     
		when(userService.addUser(any(UserDTO.class))).thenThrow(UserDocumentException.class);

	    ResponseEntity<Object> responseEntity = userController.addUser(userDTO);

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@Test
	@DisplayName("Should throw exception when adding user with same email")
	public void testAddUserEmailFailure() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

	    UserDTO userDTO = getUserDTO();
	     
		when(userService.addUser(any(UserDTO.class))).thenThrow(UserEmailException.class);

	    ResponseEntity<Object> responseEntity = userController.addUser(userDTO);

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@Test
	@DisplayName("Should get user successfully")
	public void testFetchUser() throws Exception {
	    
        String document = "1234567";
		User user = getUserEntity();
		
		when(userService.fetchUser(document)).thenReturn(user);

		ResponseEntity<Object> responseEntity = userController.getUser(document);

		assertThat(responseEntity.getBody()).isInstanceOf(User.class);
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Should return http status not found when user document does not exists")
	public void testFetchUserFailure() throws Exception {
	    String document = "1234567";
	    
	    when(userService.fetchUser(document)).thenThrow(EntityNotPresentException.class);
	    
	    ResponseEntity<Object> responseEntity = userController.getUser(document);

	    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	private UserDTO getUserDTO() {
		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName("test");
		userDTO.setLastName("test");
		userDTO.setEmail("test@gmail.com");
		userDTO.setPassword("test123");
		userDTO.setDocument("1234567");
		userDTO.setBalance(new BigDecimal(0.01));
		userDTO.setUserType("COMMON");
		
		return userDTO;
	}
	
	private User getUserEntity() {
		User user = new User();
		user.setId(1L);
		user.setFirstName("test");
		user.setLastName("test");
		user.setEmail("test@gmail.com");
		user.setPassword("#######");
		user.setDocument("1234567");
		user.setBalance(new BigDecimal(0.01));
		user.setUserType(UserType.COMMON);
		
		return user;
	}
}
