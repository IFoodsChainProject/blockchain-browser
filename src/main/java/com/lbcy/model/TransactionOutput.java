package com.lbcy.model;

import com.google.gson.annotations.SerializedName;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * 
 */
@Document(collection="dna_transaction_output")
public class TransactionOutput
{
    /**
     * 交易编号_数组位置
     */
    @Id
    private String id;

    /**
     * 资产编号
     */
    @SerializedName("AssetID")
    private String assetID;

    /**
     * 资产信息
     */
    @DBRef(lazy=true)
    @SerializedName("Asset")
    private Asset asset;

    /**
     * 交易资产数量
     */
    @SerializedName("Value")
    private BigDecimal value;

    /**
     * 收账地址
     */
    @SerializedName("ProgramHash")
    private String address;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAssetID()
    {
        return assetID;
    }

    public void setAssetID(String assetID)
    {
        this.assetID = assetID;
    }

    public Asset getAsset()
    {
        return asset;
    }

    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }

    public BigDecimal getValue()
    {
        return value;
    }

    public void setValue(BigDecimal value)
    {
        this.value = value;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
