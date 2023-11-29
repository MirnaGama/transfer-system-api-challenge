package com.mirna.transferapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mirna.transferapi.domain.dtos.UserDTO;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.mappers.UserMapper;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.UserDocumentException;
import com.mirna.transferapi.exceptions.UserEmailException;
import com.mirna.transferapi.repositories.UserRepository;
import com.mirna.transferapi.security.auth.util.PasswordEncryptorUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;

	public User fetchUser(String document) throws EntityNotPresentException {
		
		Optional<User> user = userRepository.findUserByDocument(document);

		if (user.isPresent()) {
			return user.get();
		}

		throw new EntityNotPresentException("User not found with document "+document);
	}
	
	public UserDTO addUser(UserDTO userDTO) throws UserEmailException, UserDocumentException {
		
		if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
			throw new UserEmailException();
		} else if (userRepository.findUserByDocument(userDTO.getDocument()).isPresent()) {
			throw new UserDocumentException();
		}
		
		String encodedPassword = PasswordEncryptorUtil.encryptPassword(userDTO.getPassword());
		userDTO.setPassword(encodedPassword);

		User userEntity = userMapper.toUserEntity(userDTO);
		userEntity = userRepository.save(userEntity);
		
		return userMapper.toUserDTO(userEntity);
	}
	
}
