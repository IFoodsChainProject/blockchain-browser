package com.ifoods.service.transaction.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ifoods.model.Transaction;
import com.ifoods.service.blockchain.AddressService;
import com.ifoods.service.transaction.TransactionService;

/**
 * 数据记录TxType=129
 */
@Service
public class RecordTransactionService implements TransactionService {
    private final static Logger logger = LoggerFactory.getLogger(RecordTransactionService.class);
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private AddressService addressService;

    @Override
    public void deal(Transaction transaction) {
      //保存交易信息
      transaction.setTypeName("数据记录");
      
      addressService.saveData(transaction);
      
      mongoTemplate.save(transaction);
    }
    
}
