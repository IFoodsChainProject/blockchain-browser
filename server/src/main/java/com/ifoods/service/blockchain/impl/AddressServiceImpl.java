package com.ifoods.service.blockchain.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ifoods.common.Constants;
import com.ifoods.common.util.BlockUtils;
import com.ifoods.common.util.CommonUtils;
import com.ifoods.model.Address;
import com.ifoods.model.AddressTransaction;
import com.ifoods.model.Transaction;
import com.ifoods.model.Transaction.Payload;
import com.ifoods.model.TransactionOutput;
import com.ifoods.service.blockchain.AddressService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService
{
    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Transaction transaction) {
        //找到交易相关的所有地址
        List<Address> addresses = new ArrayList<>();
        List<Transaction.UTXOInput> utxoInputs = transaction.getUtxoInputs();
        if (!CollectionUtils.isEmpty(utxoInputs)) {
            for (int i = 0; i < utxoInputs.size(); i++) {
                Transaction.UTXOInput utxoInput = utxoInputs.get(i);
                TransactionOutput output = utxoInput.getOutput();

                String addressId = output.getAddress();

                Address address = mongoTemplate.findById(addressId, Address.class);
                if (null == address) {
                    address = new Address(addressId);
                }
                if (!addresses.contains(address)) {
                    addresses.add(address);
                }
            }
        }
        List<TransactionOutput> outputs = transaction.getOutputs();
        if (!CollectionUtils.isEmpty(outputs)) {
            for (int i = 0; i < outputs.size(); i++) {
                TransactionOutput output = outputs.get(i);

                String addressId = output.getAddress();

                Address address = mongoTemplate.findById(addressId, Address.class);
                if (null == address) {
                    address = new Address(addressId);
                    address.setCreateTime(transaction.getTimestamp());
                }
                if (!addresses.contains(address)) {
                    addresses.add(address);
                }
            }
        }


        for (Address address : addresses) {
            //将该交易添加到该地址的交易流水里
            AddressTransaction addressTransaction = mongoTemplate.findById(address.getId() + transaction.getHash(), AddressTransaction.class);
            if(null == addressTransaction) {
                AddressTransaction addressTransactionNew = new AddressTransaction();
                //addressTransaction id 为 address+transaction
                addressTransactionNew.setId(address.getId() + transaction.getHash());
                addressTransactionNew.setAddress(address.getId());
                addressTransactionNew.setTransaction(transaction);
                addressTransactionNew.setTransactionTime(transaction.getTimestamp());
                mongoTemplate.save(addressTransactionNew);
            }

            //计算当前交易从而计算出余额
            //最新一条未花费输出（最新转账入账）
            Map<String, BigDecimal> assetMap = new LinkedHashMap<>();
            //资产赋值给assetMap，赋值资产余额
            if(!CollectionUtils.isEmpty(address.getAssets())) {
                for(Address.Asset asset : address.getAssets()) {
                    assetMap.put(asset.getAssetName(), asset.getAssetValue());
                }
            }

            for (TransactionOutput output : transaction.getOutputs()) {
                if (address.getId().equals(output.getAddress())) {
                    String assetName = output.getAsset().getName();

                    BigDecimal assetValue = output.getValue();

                    BigDecimal beforeValue = assetMap.get(assetName);

                    if (null == beforeValue) {
                        beforeValue = new BigDecimal(0);
                    }

                    assetValue = beforeValue.add(assetValue);
                    assetMap.put(assetName, assetValue);
                }
            }

            for (Transaction.UTXOInput utxoInput : transaction.getUtxoInputs()) {
                TransactionOutput output = utxoInput.getOutput();
                if (address.getId().equals(output.getAddress())) {
                    String assetName = output.getAsset().getName();
                    BigDecimal assetValue = output.getValue();

                    BigDecimal beforeValue = assetMap.get(assetName);

                    if (null == beforeValue) {
                        beforeValue = new BigDecimal(0);
                    }

                    assetValue = beforeValue.subtract(assetValue);
                    assetMap.put(assetName, assetValue);
                }
            }

            address.setLastTime(transaction.getTimestamp());
            address.setAssets(assetMap);
            mongoTemplate.save(address);
        }
    }

    @Override
    public void saveData(Transaction transaction) {
        //获得记录的数据数据;
        Payload payload = transaction.getPayload();
        if((!Constants.RECORDTYPE.equals(payload.getRecordType())) || StringUtils.isEmpty(payload.getRecordData())) {
            
            return;
        }
        
        //解析成对应的参数, 地址 和 数据;
        log.info("txType=129 data={}", payload.getRecordData());
        Map<String, Object> map = null;
        try {
            map = BlockUtils.toAddressAndMeatInfo(payload.getRecordData());
        }catch(Exception e) {
        }
        
        //保存对应的地址;
        String addressId = null;
        try {
            if(map == null) {
                return;
            }
            addressId = map.get(Constants.RECORDTYPE_ADDRESS).toString();
            if(StringUtils.isEmpty(addressId)) {
                return;
            }
        }catch(Exception e) {
            log.error("save data error. resultmap={}", map, e);
            return;
        }
        
        
        Address address = mongoTemplate.findById(addressId, Address.class);
        if (null == address) {
            address = new Address(addressId);
        }
        
        //保存地址关联的交易
        AddressTransaction addressTransaction = mongoTemplate.findById(address.getId() + transaction.getHash(), AddressTransaction.class);
        if(null == addressTransaction) {
            AddressTransaction addressTransactionNew = new AddressTransaction();
            addressTransactionNew.setId(address.getId() + transaction.getHash());
            addressTransactionNew.setAddress(address.getId());
            addressTransactionNew.setTransaction(transaction);
            addressTransactionNew.setTransactionTime(transaction.getTimestamp());
            mongoTemplate.save(addressTransactionNew);
        }

        address.setLastTime(transaction.getTimestamp());
        mongoTemplate.save(address);
    }

}
