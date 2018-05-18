package com.lbcy.service;

import com.lbcy.model.Address;
import com.lbcy.model.AddressTransaction;
import com.lbcy.model.Transaction;
import com.lbcy.model.TransactionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 
 */
@Service
public class AddressService
{
    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Transaction transaction)
    {
        //找到交易相关的所有地址
        List<Address> addresses = new ArrayList<>();
        List<Transaction.UTXOInput> utxoInputs = transaction.getUtxoInputs();
        if (!CollectionUtils.isEmpty(utxoInputs))
        {
            for (int i = 0; i < utxoInputs.size(); i++)
            {
                Transaction.UTXOInput utxoInput = utxoInputs.get(i);
                TransactionOutput output = utxoInput.getOutput();

                String addressId = output.getAddress();

                Address address = mongoTemplate.findById(addressId, Address.class);
                if (null == address)
                {
                    address = new Address(addressId);
                }
                if (!addresses.contains(address))
                {
                    addresses.add(address);
                }
            }
        }
        List<TransactionOutput> outputs = transaction.getOutputs();
        if (!CollectionUtils.isEmpty(outputs))
        {
            for (int i = 0; i < outputs.size(); i++)
            {
                TransactionOutput output = outputs.get(i);

                String addressId = output.getAddress();

                Address address = mongoTemplate.findById(addressId, Address.class);
                if (null == address)
                {
                    address = new Address(addressId);
                    address.setCreateTime(transaction.getTimestamp());
                }
                if (!addresses.contains(address))
                {
                    addresses.add(address);
                }
            }
        }


        for (Address address : addresses)
        {
            //将该交易添加到该地址的交易流水里
            AddressTransaction addressTransaction = mongoTemplate.findById(address.getId() + transaction.getHash(), AddressTransaction.class);
            if(null == addressTransaction)
            {
                AddressTransaction addressTransactionNew = new AddressTransaction();
                //addressTransaction id 为 address+transaction
                addressTransactionNew.setId(address.getId() + transaction.getHash());
                addressTransactionNew.setTransaction(transaction);
                addressTransactionNew.setTransactionTime(transaction.getTimestamp());
                mongoTemplate.save(addressTransactionNew);
            }

            //计算当前交易从而计算出余额
            //最新一条未花费输出（最新转账入账）
            Map<String, BigDecimal> assetMap = new LinkedHashMap<>();
            //资产赋值给assetMap，赋值资产余额
            if(!CollectionUtils.isEmpty(address.getAssets()))
            {
                for(Address.Asset asset : address.getAssets())
                {
                    assetMap.put(asset.getAssetName(), asset.getAssetValue());
                }
            }

            for (TransactionOutput output : transaction.getOutputs())
            {
                if (address.getId().equals(output.getAddress()))
                {
                    String assetName = output.getAsset().getName();

                    BigDecimal assetValue = output.getValue();

                    BigDecimal beforeValue = assetMap.get(assetName);

                    if (null == beforeValue)
                    {
                        beforeValue = new BigDecimal(0);
                    }

                    assetValue = beforeValue.add(assetValue);
                    assetMap.put(assetName, assetValue);
                }
            }

            for (Transaction.UTXOInput utxoInput : transaction.getUtxoInputs())
            {
                TransactionOutput output = utxoInput.getOutput();
                if (address.getId().equals(output.getAddress()))
                {
                    String assetName = output.getAsset().getName();
                    BigDecimal assetValue = output.getValue();

                    BigDecimal beforeValue = assetMap.get(assetName);

                    if (null == beforeValue)
                    {
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
}
