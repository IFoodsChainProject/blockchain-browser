package com.ifoods.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月29日
 */
@Data
public class SimpleTran implements Serializable {

    private static final long serialVersionUID = 1L;

    private String hash;
    private Long blockId;
    private Integer txType;
    private String typeName;
    private Long timestamp;
    private int pageNum;
    private Long totalNum;
    
    public SimpleTran(Transaction transaction) {
        if(transaction == null) {
            return;
        }
        this.hash = transaction.getHash();
        this.blockId = transaction.getBlockId();
        this.txType = transaction.getTxType();
        this.typeName = transaction.getTypeName();
        this.timestamp = transaction.getTimestamp();
    }
    
    public SimpleTran(Transaction transaction, int pageNum, Long totalNum) {
        if(transaction == null) {
            return;
        }
        this.hash = transaction.getHash();
        this.blockId = transaction.getBlockId();
        this.txType = transaction.getTxType();
        this.typeName = transaction.getTypeName();
        this.timestamp = transaction.getTimestamp();
        this.pageNum = pageNum;
        this.totalNum = totalNum;
    }

    public SimpleTran() {
        super();
        
    }
    
}
