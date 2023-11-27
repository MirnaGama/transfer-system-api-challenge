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
import org.springframework.web.bind.annotation.RestController;

import com.mirna.transferapi.domain.dtos.UserDTO;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.UserDocumentException;
import com.mirna.transferapi.exceptions.UserEmailException;
import com.mirna.transferapi.services.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    
	@Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public UserDTO addUser(@RequestBody UserDTO userDTO) throws UserEmailException, UserDocumentException {
    	return userService.addUser(userDTO);
    }
    
    @RequestMapping(value = "/{document}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable("document") String document) throws EntityNotPresentException {
        
       User user = userService.fetchUser(document);
       return ResponseEntity.ok(user);
    }
    
    @ExceptionHandler(EntityNotPresentException.class)
    public ResponseEntity<Object> handleEntityNotPresentException(EntityNotPresentException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler({UserEmailException.class, UserDocumentException.class})
    public ResponseEntity<Object> handleUserEmailException(Exception exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
}
