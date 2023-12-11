package com.mirna.transferapi.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.Transaction;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.mappers.TransactionMapper;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.repositories.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private TransactionMapper transactionMapper;
	
	@Autowired
	private UserService userService;
	
	public Transaction addTransaction(TransactionDTO transactionDTO) throws EntityNotPresentException {
		
		Transaction transaction = transactionMapper.toTransactionEntity(transactionDTO);
		
		User sender = transaction.getSender();
		User receiver = transaction.getReceiver();
		
		BigDecimal newBalanceSender = sender.getBalance().subtract(transaction.getAmount());
		sender.setBalance(newBalanceSender);
		
		BigDecimal newBalanceReceiver = receiver.getBalance().add(transaction.getAmount());
		receiver.setBalance(newBalanceReceiver);
		
		userService.updateUser(sender, sender.getDocument());
		userService.updateUser(receiver, receiver.getDocument());
		
		transaction.setSender(sender);
		transaction.setReceiver(receiver);
		
		return transactionRepository.save(transaction);
	}
}