package com.ifoods.service.transaction;

import com.ifoods.model.Transaction;

/**
 * 交易类型
 */
public interface TransactionService {
    public void deal(Transaction transaction);
}