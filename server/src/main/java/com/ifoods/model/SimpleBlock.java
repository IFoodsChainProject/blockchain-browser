package com.ifoods.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 简化的 block
 * @author zhenghui.li
 * @date 2018年5月22日
 */
@Data
public class SimpleBlock implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long height;
    private String hash;
    private Long timestamp;
    private Integer transactionNum;
    private int pageNum = 1;
    private Long totalNum;
    
    public SimpleBlock(int pageNum, Long totalNum) {
        super();
        this.pageNum = pageNum;
        this.totalNum = totalNum;
    }
    public SimpleBlock() {
        super();
        
    }
    
}
