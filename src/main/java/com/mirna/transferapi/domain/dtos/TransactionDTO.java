package com.mirna.transferapi.domain.dtos;

import java.math.BigDecimal;

public class TransactionDTO {

	private String senderDocument;
	private String receiverDocument;
	private BigDecimal amount;
	
	public String getSenderDocument() {
		return senderDocument;
	}
	public void setSenderDocument(String senderDocument) {
		this.senderDocument = senderDocument;
	}
	public String getReceiverDocument() {
		return receiverDocument;
	}
	public void setReceiverDocument(String receiverDocument) {
		this.receiverDocument = receiverDocument;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
