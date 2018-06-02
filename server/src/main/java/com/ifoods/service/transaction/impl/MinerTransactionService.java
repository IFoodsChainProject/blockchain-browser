package com.ifoods.service.transaction.impl;

import com.ifoods.model.Transaction;
import com.ifoods.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * 记账交易 TxType=0
 */
@Service
public class MinerTransactionService implements TransactionService
{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deal(Transaction transaction)
    {
        transaction.setTypeName("记账交易");
        //保存交易信息
        mongoTemplate.save(transaction);
    }
}
