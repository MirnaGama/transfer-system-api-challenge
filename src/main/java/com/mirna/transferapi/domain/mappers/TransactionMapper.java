package com.mirna.transferapi.domain.mappers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.Transaction;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.services.UserService;

@Component
public class TransactionMapper {

	@Autowired
	private UserService userService;
	
	 public Transaction toTransactionEntity(TransactionDTO transactionDTO) throws EntityNotPresentException {
	        Transaction transaction = new Transaction();
			
	        User sender = userService.fetchUser(transactionDTO.getSenderDocument());
	        User receiver = userService.fetchUser(transactionDTO.getReceiverDocument());
	        
	        transaction.setSender(sender);
	        transaction.setReceiver(receiver);
	        transaction.setAmount(transactionDTO.getAmount());
	        transaction.setTimestamp(LocalDateTime.now());
	        
	        return transaction;
	 }
}
