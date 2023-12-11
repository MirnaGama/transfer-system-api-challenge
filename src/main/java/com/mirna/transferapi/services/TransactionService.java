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
import com.mirna.transferapi.exceptions.SenderUserTypeInvalidException;
import com.mirna.transferapi.repositories.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserService userService;

	public Transaction addTransaction(TransactionDTO transactionDTO) throws EntityNotPresentException, SenderUserTypeInvalidException {

		Transaction transaction = new Transaction();

		User sender = userService.fetchUser(transactionDTO.getSenderDocument());
		User receiver = userService.fetchUser(transactionDTO.getReceiverDocument());
		
		if (sender.getUserType().equals(UserType.SHOPKEEPER)) {
			throw new SenderUserTypeInvalidException();
		}

		transaction.setSender(sender);
		transaction.setReceiver(receiver);
		transaction.setAmount(transactionDTO.getAmount());
		transaction.setTimestamp(LocalDateTime.now());

		BigDecimal newBalanceSender = sender.getBalance().subtract(transaction.getAmount());
		sender.setBalance(newBalanceSender);

		BigDecimal newBalanceReceiver = receiver.getBalance().add(transaction.getAmount());
		receiver.setBalance(newBalanceReceiver);

		userService.updateUser(sender, sender.getDocument());
		userService.updateUser(receiver, receiver.getDocument());

		return transactionRepository.save(transaction);
	}
}
