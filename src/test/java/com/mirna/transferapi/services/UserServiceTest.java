package com.mirna.transferapi.services;

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
import com.mirna.transferapi.repositories.UserRepository;
import com.mirna.transferapi.security.auth.util.PasswordEncryptorUtil;

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
	public void testAddUser() throws Exception {

		UserDTO userDTO = getUserDTO();
		User user = getUserEntity();
		
		when(userMapper.toUserEntity(userDTO)).thenReturn(user);
		when(userMapper.toUserDTO(user)).thenReturn(userDTO);
		
		when(userRepository.save(user)).thenReturn(user);

		UserDTO userAdded = userService.addUser(userDTO);

		assertThat(userAdded).isNotNull();
	}
	
	@Test
	@DisplayName("Should fetch user by document successfully")
	public void testFetchUser() throws Exception {
        String document = "1234567";
		User user = getUserEntity();
		
		when(userRepository.findUserByDocument(document)).thenReturn(Optional.of(user));

		User userAdded = userService.fetchUser(document);

		assertThat(userAdded).isNotNull();
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
