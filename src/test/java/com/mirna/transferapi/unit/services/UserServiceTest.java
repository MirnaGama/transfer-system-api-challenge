package com.mirna.transferapi.unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.mirna.transferapi.domain.dtos.UserDTO;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.domain.mappers.UserMapper;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.UserDocumentException;
import com.mirna.transferapi.exceptions.UserEmailException;
import com.mirna.transferapi.repositories.UserRepository;
import com.mirna.transferapi.services.UserService;

@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private UserMapper userMapper;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DisplayName("Should add user successfully")
	public void testAddUserSuccess() throws Exception {

		UserDTO userDTO = getUserDTO();
		User user = getUserEntity();
		
		when(userMapper.toUserEntity(userDTO)).thenReturn(user);
		when(userMapper.toUserDTO(user)).thenReturn(userDTO);
		
		when(userRepository.save(user)).thenReturn(user);

		UserDTO userAdded = userService.addUser(userDTO);

		assertThat(userAdded).isNotNull();
	}
	
	@Test
	@DisplayName("Should throw exception when adding user with same email")
	public void testAddUserFailureEmail() throws Exception {

		UserDTO userDTO = getUserDTO();
		User user = getUserEntity();
		
		when(userMapper.toUserEntity(userDTO)).thenReturn(user);
		when(userMapper.toUserDTO(user)).thenReturn(userDTO);
		
		when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

		assertThrows(UserEmailException.class, () -> userService.addUser(userDTO));
	}
	
	@Test
	@DisplayName("Should throw exception when adding user with same document number")
	public void testAddUserFailureDocument() throws Exception {

		UserDTO userDTO = getUserDTO();
		User user = getUserEntity();
		
		when(userMapper.toUserEntity(userDTO)).thenReturn(user);
		when(userMapper.toUserDTO(user)).thenReturn(userDTO);
		
		when(userRepository.findUserByDocument(user.getDocument())).thenReturn(Optional.of(user));

		assertThrows(UserDocumentException.class, () -> userService.addUser(userDTO));
	}
	
	@Test
	@DisplayName("Should fetch user by document successfully")
	public void testFetchUserSuccess() throws Exception {
        String document = "1234567";
		User user = getUserEntity();
		
		when(userRepository.findUserByDocument(document)).thenReturn(Optional.of(user));

		User userAdded = userService.fetchUser(document);

		assertThat(userAdded).isNotNull();
	}
	
	@Test
	@DisplayName("Should throw exception if the user document does not exist when fetching the user")
	public void testFetchUserFailure() throws Exception {
        String document = "1234567";
		
		assertThrows(EntityNotPresentException.class, () -> userService.fetchUser(document));
	}
	
	@Test
	@DisplayName("Should update user by document successfully")
	public void testUpdateUserSuccess() throws Exception {
        String document = "1234567";
		
        User user = getUserEntity();
        
        when(userRepository.findUserByDocument(document)).thenReturn(Optional.of(user));
        
        when(userRepository.save(user)).thenReturn(user);
        
        User updatedUser = userService.updateUser(user, document);
		
        assertThat(updatedUser).isNotNull();
	}
	
	@Test
	@DisplayName("Should throw exception if the user document does not exist when updating the user")
	public void testUpdateUserFailure() throws Exception {
        String document = "1234567";
		
        User user = getUserEntity();
        
        assertThrows(EntityNotPresentException.class, () -> userService.updateUser(user, document));
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
