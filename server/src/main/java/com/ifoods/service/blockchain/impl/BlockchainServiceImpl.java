package com.ifoods.service.blockchain.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifoods.dao.BlockDao;
import com.ifoods.model.Address;
import com.ifoods.model.Block;
import com.ifoods.model.BlockchainResponse;
import com.ifoods.model.SimpleBlock;
import com.ifoods.model.Transaction;
import com.ifoods.model.TxType;
import com.ifoods.service.blockchain.BlockchainService;
import com.ifoods.service.transaction.TransactionService;

import lombok.extern.slf4j.Slf4j;

/**
 */
@Service
@Slf4j
public class BlockchainServiceImpl implements BlockchainService {
    
    /** 根据高度获得详情 */
    private static final String HEIGHT_DETAIL = "/api/v1/block/details/height/{blockId}";
    private static final String BLOCK_HIGHEST = "/api/v1/block/height";
    
    @Value("${blockchain.url}")
    private String blockchainUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private BlockDao blockDao;

    @Autowired
    private Map<String, TransactionService> transactionServiceMap;

    /**
     * 根据区块编号获取区块信息
     * @param blockId 区块编号
     */
    public Block getBlockFromBlockchain(Long blockId) {
        BlockchainResponse blockchainResponse = restTemplate.getForObject(blockchainUrl + HEIGHT_DETAIL, BlockchainResponse.class, blockId);

        if (0 != blockchainResponse.getError()) {
            try {
                log.error("getBlockInfoFromBlockchain fail blockchainResponse is {}", new ObjectMapper().writeValueAsString(blockchainResponse));
            } catch (JsonProcessingException e) {
                log.error("getBlockInfoFromBlockchain fail.");
            }
            return null;
        }
        return blockchainResponse.getResult();
    }

    /**
     * 获取区块链上的最大区块编号
     */
    public Long getMaxBlockIdFromBlockchain() {
        long height = 0;

        Map<String, Object> heightInfo = restTemplate.getForObject(blockchainUrl + BLOCK_HIGHEST, Map.class);
        if (0 != (Double)heightInfo.get("Error")) {
            try {
                log.error("getMaxBlockIdFromBlockchain fail heightInfo is {}", new ObjectMapper().writeValueAsString(heightInfo));
            } catch (JsonProcessingException e) {
                log.error("getMaxBlockIdFromBlockchain fail. writeValueAsString error", e);
            }
            return height;
        }

        if (heightInfo.get("Result") instanceof Integer)
        {
            height = new Long((Integer) heightInfo.get("Result"));
        }
        else if (heightInfo.get("Result") instanceof Long)
        {
            height = (Long) heightInfo.get("Result");
        }
        else if (heightInfo.get("Result") instanceof Double)
        {
            height = ((Double) heightInfo.get("Result")).longValue();
        }
        return height;
    }

    /**
     * 获取数据库中最大区块编号
     */
    public Long getMaxBlockIdFromDb()
    {
        Query query = new Query().with(new Sort(Sort.Direction.DESC, "_id"));
        Block block = mongoTemplate.findOne(query, Block.class);

        if (null == block)
        {
            return new Long(-1);
        }
        else
        {
            return block.getId();
        }
    }

    public boolean findAndSave(long blockId) {
        Block block = getBlockFromBlockchain(blockId);

        if (null == block) {
            return false;
        }

        block.setId(blockId);

        List<Transaction> transactions = block.getTransactions();
        for (Transaction transaction : transactions) {
            transaction.setBlockId(blockId);
            transaction.setTimestamp(block.getBlockData().getTimestamp());

            TransactionService transactionService = null;
            if (transaction.getTxType() == 0)//记账交易
            {
                transactionService = transactionServiceMap.get("minerTransactionService");
            }
            else if (transaction.getTxType() == 64)//资产注册
            {
                transactionService = transactionServiceMap.get("registerTransactionService");
            }
            else if (transaction.getTxType() == 1)//资产发行
            {
                transactionService = transactionServiceMap.get("issueTransactionService");
            }
            else if (transaction.getTxType() == 128)//合约（转账）交易
            {
                transactionService = transactionServiceMap.get("contractTransactionService");
            }
            else if (transaction.getTxType() == 129)//数据记录
            {
                transactionService = transactionServiceMap.get("recordTransactionService");
            }
            
            if (null == transactionService)
            {
                try {
                    log.error("TxType not find block is {}", new ObjectMapper().writeValueAsString(block));
                } catch (JsonProcessingException e) {
                    log.error("TxType not find block. {}", e);
                }
                return false;
            }

            log.info("transactionService deal start block is {}, TxType is {} ", blockId, transaction.getTxType());
            transactionService.deal(transaction);
            log.info("transactionService deal end.");
        }

        mongoTemplate.save(block);

        try {
            log.info("findAndSave block is {}", new ObjectMapper().writeValueAsString(block));
        } catch (JsonProcessingException e) {
            log.error("findAndSave block is. writeValueAsString error {}", e);
        }

        return true;
    }
    
    /**
     * 获取mongodb中高度最大的块
     */
    public Block getMaxBlockFromDb() {
        Query query = new Query().with(new Sort(Sort.Direction.DESC, "_id"));
        Block block = mongoTemplate.findOne(query, Block.class);

        if (null == block) {
            return null;
        }
        return block;
    }

    @Override
    public List<SimpleBlock> simpleBlockListByTimeDesc(Long currenttime, int pageNum, Integer pageSize) {
        List<Block> blockList = blockDao.getBlockBypage(pageNum, pageSize, currenttime);
        Long totalNum = blockDao.getBlockCount(currenttime);
        List<SimpleBlock> simpleList = new ArrayList<>(0);
        SimpleBlock simpleBlock = null;
        if(!(blockList == null || blockList.size() == 0)) {
            for (Block block : blockList) {
                simpleBlock = new SimpleBlock(pageNum, totalNum);
                simpleBlock.setHeight(block.getId());
                simpleBlock.setHash(block.getHash());
                simpleBlock.setTimestamp(block.getBlockData().getTimestamp());
                simpleBlock.setTransactionNum(block.getTransactions().size());
                
                simpleList.add(simpleBlock);
            }
        }
        return simpleList;
    }

    @Override
    public List<Transaction> getTransactionExcludeTxtype(Long currenttime, int pageNum, Integer pageSize, List<Integer> excludeTxtypes) {
        List<Transaction> tranList = null;
        try {
            tranList = blockDao.getTransactionExcludeTxtype(pageNum, pageSize, currenttime, excludeTxtypes);
        }catch(Exception e) {
            log.error("getTransactionExcludeTxtype error.", e);
        }
        return tranList;
    }

    @Override
    public Block getblockInfoByHeight(Long blockId) {
        Block block = null;
        try {
            block = blockDao.getBlockByHeight(blockId);
        }catch(Exception e) {
            log.error("getblockInfoByHeight error.", e);
        }
        return block;
    }

    @Override
    public Transaction getTransactionByHash(String hash) {
        
        return blockDao.getTransactionByHash(hash);
    }

    @Override
    public Address getAddressById(String id, int count, long currenttime) {
        
        return blockDao.getAddressById(id, count, currenttime);
    }

    @Override
    public Long getBlockCount(Long currenttime) {
        return blockDao.getBlockCount(currenttime);
    }

    @Override
    public Long getTransactionCount(Long currenttime, List<Integer> excludeTxtypes) {
        
        return blockDao.getTransactionCount(currenttime, excludeTxtypes);
    }

    @Override
    public Block getblockInfoByHash(String hash) {
        
        return blockDao.getblockInfoByHash(hash);
    }

    @Override
    public List<Address> getAddressPage(Long currenttime, Integer pageNumber, Integer pageSize) {
        
        return blockDao.getAddressPage(currenttime, pageNumber, pageSize);
    }

}