package com.mirna.transferapi.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mirna.transferapi.domain.dtos.UserDTO;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.UserDocumentException;
import com.mirna.transferapi.exceptions.UserEmailException;
import com.mirna.transferapi.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/users")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> addUser(@RequestBody UserDTO userDTO) {

		UserDTO result = null;
		try {
			result = userService.addUser(userDTO);
		} catch (UserEmailException | UserDocumentException userException) {
			Map<String, Object> body = new HashMap<>();
			body.put("message", userException.getMessage());

			return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/{document}", method = RequestMethod.GET)
	public ResponseEntity<Object> getUser(@PathVariable("document") String document) {

		User user = null;
		try {
			user = userService.fetchUser(document);
		} catch (EntityNotPresentException entityNotPresentException) {
			Map<String, Object> body = new HashMap<>();
			body.put("message", entityNotPresentException.getMessage());

			return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(user);
	}

}
