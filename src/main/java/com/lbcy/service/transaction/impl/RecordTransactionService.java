package com.lbcy.service.transaction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.lbcy.model.Transaction;
import com.lbcy.service.transaction.TransactionService;

/**
 * 数据记录TxType=129
 */
@Service
public class RecordTransactionService implements TransactionService
{
    private final static Logger logger = LoggerFactory.getLogger(RecordTransactionService.class);
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deal(Transaction transaction)
    {
        logger.info("data:"+transaction.getPayload());
    }
}
