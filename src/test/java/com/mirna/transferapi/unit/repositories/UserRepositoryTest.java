package com.mirna.transferapi.unit.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.repositories.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@DisplayName("Should fetch user by email successfully")
	void testFindUserByEmailSuccess() {

		User user = getUser();
		
		when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(user));

		Optional<User> result = userRepository.findUserByEmail("test@gmail.com");

		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("Should not fetch user by email when user does not exists")
	void testFindUserByEmailFailure() {

		Optional<User> result = userRepository.findUserByEmail("test2@gmail.com");

		assertThat(result.isEmpty()).isTrue();
	}
	
	@Test
	@DisplayName("Should fetch user by document successfully")
	void testFindUserByDocumentSuccess() {

		User user = getUser();
		
		when(userRepository.findUserByDocument("123456789")).thenReturn(Optional.of(user));

		Optional<User> result = userRepository.findUserByDocument("123456789");

		assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Should not fetch user by document when user does not exists")
	void testFindUserByDocumentFailure() {

		Optional<User> result = userRepository.findUserByDocument("123456789");

		assertThat(result.isEmpty()).isTrue();
	}
	
	private User getUser() {
		User user = new User();
		user.setFirstName("test");
		user.setLastName("test");
		user.setEmail("test@gmail.com");
		user.setPassword("test123");
		user.setDocument("1234567");
		user.setBalance(new BigDecimal(0.01));
		user.setUserType(UserType.COMMON);
		
		return user;
	}

}
