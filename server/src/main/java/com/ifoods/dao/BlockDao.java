package com.ifoods.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ifoods.model.Address;
import com.ifoods.model.AddressTransaction;
import com.ifoods.model.Block;
import com.ifoods.model.Transaction;
import com.ifoods.model.TxType;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月23日
 */
@Component
@Slf4j
public class BlockDao {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    /**
     * 每页显示的 区块的简化信息
     */
    public List<Block> getBlockBypage(int pageNum, int pageSize, long currentTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("blockData.timestamp").lt(currentTime));
        query.with(new Sort(Direction.DESC, "_id"));
        query.skip((pageNum-1)*pageSize).limit(pageSize);
        
        return mongoTemplate.find(query, Block.class);
    }

    

    /**
     * @param pageSize
     * @param currenttime
     * @param excludeTxtype
     * @return
     */
    public List<Transaction> getTransactionExcludeTxtype(int pageNum, Integer pageSize, Long currenttime, List<Integer> excludeTxtypes) {
        Query query = new Query();
        query.addCriteria(Criteria.where("timestamp").lt(currenttime));
        query.addCriteria(Criteria.where("txType").nin(excludeTxtypes));
        query.with(new Sort(Direction.DESC, "timestamp"));
        query.skip((pageNum-1)*pageSize).limit(pageSize);
        
        return mongoTemplate.find(query, Transaction.class);
    }

    /**
     * @param blockId
     * @return
     */
    public Block getBlockByHeight(Long blockId) {
        return mongoTemplate.findById(blockId, Block.class);
    }

    /**
     * @param hash
     * @return
     */
    public Transaction getTransactionByHash(String hash) {
        return mongoTemplate.findById(hash, Transaction.class);
    }
    
    /**
     * @param id
     * @param count
     * @return
     */
    public Address getAddressById(String id, int count, long currentTime) {
        Address address = null;
        try {
            address = mongoTemplate.findById(id, Address.class);
            if(null == address) {
                return null;
            }
            
            //查询关联的交易
            Query query = new Query(Criteria.where("address").is(id));
            query.addCriteria(Criteria.where("transactionTime").lt(currentTime));
            query.with(new Sort(Direction.DESC, "transactionTime"));
            query.limit(count);
            
            List<AddressTransaction> addressTransactions = mongoTemplate.find(query, AddressTransaction.class);
            
            List<Transaction> transactions = new ArrayList<>();
            for(AddressTransaction addressTransaction: addressTransactions) {
                transactions.add(addressTransaction.getTransaction());
            }
            
            address.setTransactions(transactions);
        }catch(Exception e) {
            log.error("getAddressById error.", e);
        }
        return address;
    }



    /**
     * @param currenttime
     * @return
     */
    public Long getBlockCount(Long currenttime) {
        Query query = new Query(Criteria.where("blockData.timestamp").lt(currenttime));
        return mongoTemplate.count(query, Block.class);
    }



    /**
     * @param currenttime
     * @param excludeList
     * @return
     */
    public Long getTransactionCount(Long currenttime, List<Integer> excludeTxtypes) {
        Query query = new Query(Criteria.where("timestamp").lt(currenttime));
        query.addCriteria(Criteria.where("txType").nin(excludeTxtypes));
        
        return mongoTemplate.count(query, Transaction.class);
    }


    /**
     * @param hash
     * @return
     */
    public Block getblockInfoByHash(String hash) {
        Query query = new Query(Criteria.where("hash").is(hash));
        return mongoTemplate.findOne(query, Block.class);
    }



    /**
     * 最新交易时间排序
     * @param currenttime
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<Address> getAddressPage(Long currenttime, Integer pageNumber, Integer pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("lastTime").lt(currenttime));
        query.with(new Sort(Direction.DESC, "_id"));
        query.skip((pageNumber-1)*pageSize).limit(pageSize);
        
        return mongoTemplate.find(query, Address.class);
    }
}
