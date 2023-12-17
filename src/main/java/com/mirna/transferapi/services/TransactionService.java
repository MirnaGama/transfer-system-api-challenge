package com.mirna.transferapi.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mirna.transferapi.domain.dtos.TransactionDTO;
import com.mirna.transferapi.domain.entities.Transaction;
import com.mirna.transferapi.domain.entities.User;
import com.mirna.transferapi.domain.enums.UserType;
import com.mirna.transferapi.exceptions.EntityNotPresentException;
import com.mirna.transferapi.exceptions.InsufficientBalanceException;
import com.mirna.transferapi.exceptions.SenderUserTypeInvalidException;
import com.mirna.transferapi.exceptions.UnauthorizedTransactionException;
import com.mirna.transferapi.repositories.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorizationService authorizationService;

	@Transactional
    (rollbackOn = Exception.class)
	public Transaction addTransaction(TransactionDTO transactionDTO) throws EntityNotPresentException, SenderUserTypeInvalidException, InsufficientBalanceException, UnauthorizedTransactionException {

		Transaction transaction = new Transaction();

		User sender = userService.fetchUser(transactionDTO.getSenderDocument());
		User receiver = userService.fetchUser(transactionDTO.getReceiverDocument());

		transaction.setSender(sender);
		transaction.setReceiver(receiver);
		transaction.setAmount(transactionDTO.getAmount());
		transaction.setTimestamp(LocalDateTime.now());
		
		validateTransaction(sender, transaction);

		BigDecimal newBalanceSender = sender.getBalance().subtract(transaction.getAmount());
		sender.setBalance(newBalanceSender);

		BigDecimal newBalanceReceiver = receiver.getBalance().add(transaction.getAmount());
		receiver.setBalance(newBalanceReceiver);
		
		if (!authorizationService.isTransactionAuthorized()) {
			throw new UnauthorizedTransactionException();
		}

		userService.updateUser(sender, sender.getDocument());
		userService.updateUser(receiver, receiver.getDocument());

		return transactionRepository.save(transaction);
	}
	
	private void validateTransaction(User sender, Transaction transaction) throws UnauthorizedTransactionException, SenderUserTypeInvalidException, InsufficientBalanceException {
		
		if (sender.getUserType().equals(UserType.SHOPKEEPER)) {
			throw new SenderUserTypeInvalidException();
		}
		
		BigDecimal senderFinalBalance = sender.getBalance().subtract(transaction.getAmount());
		
		if (senderFinalBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new InsufficientBalanceException();
		}
			
		if (!authorizationService.isTransactionAuthorized()) {
			throw new UnauthorizedTransactionException();
		}
	}
	
}
