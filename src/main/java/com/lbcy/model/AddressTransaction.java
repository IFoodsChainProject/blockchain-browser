package com.lbcy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by RZC on 2018-03-30.
 */
@Document(collection="dna_address_transaction")
public class AddressTransaction {
    @Id
    private String id;

    private Transaction transaction;

    /**
     * 交易时间
     */
    private Long transactionTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }
}
