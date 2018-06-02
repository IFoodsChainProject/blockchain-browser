package com.ifoods.service.blockchain;

import com.ifoods.model.Transaction;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月22日
 */
public interface AddressService {

    /**
     * 资产交易的保存
     * @param transaction
     */
    public void save(Transaction transaction);
    
    /**
     * 数据记录 的 保存
     */
    public void saveData(Transaction transaction);
}
