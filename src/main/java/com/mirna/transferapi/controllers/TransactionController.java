package com.mirna.transferapi.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.Transaction;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.InsufficientBalanceException;
import com.mirna.transferapi.exceptions.SenderUserTypeInvalidException;
import com.mirna.transferapi.services.TransactionService;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> addTransaction(@RequestBody TransactionDTO transactionDTO) {

		Transaction result = null;
		try {
			result = transactionService.addTransaction(transactionDTO);
		} catch (EntityNotPresentException entityNotPresentException) {
			Map<String, Object> body = new HashMap<>();
			body.put("message", entityNotPresentException.getMessage());

			return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
		} catch (SenderUserTypeInvalidException userSenderInvalidException) {
			Map<String, Object> body = new HashMap<>();
			body.put("message", userSenderInvalidException.getMessage());

			return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (InsufficientBalanceException insufficientBalanceException) {
			Map<String, Object> body = new HashMap<>();
			body.put("message", insufficientBalanceException.getMessage());

			return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
		}

		return ResponseEntity.ok(result);
	}
}
