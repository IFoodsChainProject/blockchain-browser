package com.ifoods.service.blockchain;

import java.util.List;

import com.ifoods.model.Address;
import com.ifoods.model.Block;
import com.ifoods.model.SimpleBlock;
import com.ifoods.model.Transaction;
import com.ifoods.model.TxType;

/**
 * 区块链的服务
 * @author zhenghui.li
 * @date 2018年5月22日
 */
public interface BlockchainService {

    /**
     * 根据区块编号获取区块信息
     * @param blockId 区块编号
     */
    public Block getBlockFromBlockchain(Long blockId);

    /**
     * 获取区块链上的最大区块编号
     */
    public Long getMaxBlockIdFromBlockchain();

    /**
     * 获取数据库中最大区块编号
     */
    public Long getMaxBlockIdFromDb();

    public boolean findAndSave(long blockId);
    
    /**
     * 获取mongodb中高度最大的块
     */
    public Block getMaxBlockFromDb();
    
    /**
     * @param currenttime
     * @param pageSize
     * @return
     */
    public List<SimpleBlock> simpleBlockListByTimeDesc(Long currenttime, int pageNum, Integer pageSize);

    /**
     * @param currenttime
     * @param pageSize
     * @param excludeTxtype
     * @return
     */
    public List<Transaction> getTransactionExcludeTxtype(Long currenttime, int pageNum, Integer pageSize, List<Integer> excludeTxtypes);

    /**
     * @param blockId
     * @return
     */
    public Block getblockInfoByHeight(Long blockId);

    /**
     * @param hash
     * @return
     */
    public Transaction getTransactionByHash(String hash);

    /**
     * @param id
     * @return
     */
    public Address getAddressById(String id, int count, long currenttime);

    /**
     * @param currenttime
     * @return
     */
    public Long getBlockCount(Long currenttime);

    /**
     * @param currenttime
     * @param excludeList
     * @return
     */
    public Long getTransactionCount(Long currenttime, List<Integer> excludeList);

    /**
     * @param value
     * @return
     */
    public Block getblockInfoByHash(String hash);

    /**
     * @param currenttime
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<Address> getAddressPage(Long currenttime, Integer pageNumber, Integer pageSize);
}
