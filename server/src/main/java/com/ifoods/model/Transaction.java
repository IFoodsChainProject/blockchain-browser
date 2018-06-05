package com.ifoods.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.annotations.SerializedName;
import com.ifoods.common.Constants;
import com.ifoods.common.util.BlockUtils;
import com.ifoods.common.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Document(collection="dna_transaction")
@Slf4j
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易编号
     */
    @Id
    @SerializedName("Hash")
    private String hash;

    /**
     * 区块高度
     */
    @Indexed
    private Long blockId;

    @Indexed
    private Long timestamp;

    /**
     * 交易类型  0 记账交易， 64 注册资产， 1 发行资产， 128 转账
     */
    @SerializedName("TxType")
    @Indexed
    private Integer txType;

    /**
     * 交易类型名称
     */
    private String typeName;

    @SerializedName("PayloadVersion")
    private Integer payloadVersion;

    @SerializedName("Payload")
    private Payload payload;

    /**
     * 随机数 防止攻击用的
     */
    @SerializedName("Attributes")
    private List<Map<String, Object>> attributes;

    /**
     * UTXO模块，转账资产来源
     */
    @SerializedName("UTXOInputs")
    private List<UTXOInput> utxoInputs = new ArrayList<>();

    @SerializedName("BalanceInputs")
    private List<Map<String, Object>> balanceInputs;

    /**
     * 转账地址入账
     */
    @DBRef(lazy=true)
    @SerializedName("Outputs")
    private List<TransactionOutput> outputs = new ArrayList<>();

    /**
     * 验证这个交易的公钥和签名
     */
    @SerializedName("Programs")
    private List<Map<String, Object>> programs;

    @SerializedName("AssetOutputs")
    private List<Map<String, Object>> assetOutputs;

    @SerializedName("AssetInputAmount")
    private List<Map<String, Object>> assetInputAmount;

    @SerializedName("AssetOutputAmount")
    private List<Map<String, Object>> assetOutputAmount;

    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public Long getBlockId()
    {
        return blockId;
    }

    public void setBlockId(Long blockId)
    {
        this.blockId = blockId;
    }

    public Long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public Integer getTxType()
    {
        return txType;
    }

    public void setTxType(Integer txType)
    {
        this.txType = txType;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public Integer getPayloadVersion()
    {
        return payloadVersion;
    }

    public void setPayloadVersion(Integer payloadVersion)
    {
        this.payloadVersion = payloadVersion;
    }

    public Payload getPayload()
    {
        return payload;
    }

    public void setPayload(Payload payload)
    {
        this.payload = payload;
    }

    public List<Map<String, Object>> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(List<Map<String, Object>> attributes)
    {
        this.attributes = attributes;
    }

    public List<UTXOInput> getUtxoInputs()
    {
        return utxoInputs;
    }

    public void setUtxoInputs(List<UTXOInput> utxoInputs)
    {
        this.utxoInputs = utxoInputs;
    }

    public List<Map<String, Object>> getBalanceInputs()
    {
        return balanceInputs;
    }

    public void setBalanceInputs(List<Map<String, Object>> balanceInputs)
    {
        this.balanceInputs = balanceInputs;
    }

    public List<TransactionOutput> getOutputs()
    {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs)
    {
        this.outputs = outputs;
    }

    public List<Map<String, Object>> getPrograms()
    {
        return programs;
    }

    public void setPrograms(List<Map<String, Object>> programs)
    {
        this.programs = programs;
    }

    public List<Map<String, Object>> getAssetOutputs()
    {
        return assetOutputs;
    }

    public void setAssetOutputs(List<Map<String, Object>> assetOutputs)
    {
        this.assetOutputs = assetOutputs;
    }

    public List<Map<String, Object>> getAssetInputAmount()
    {
        return assetInputAmount;
    }

    public void setAssetInputAmount(List<Map<String, Object>> assetInputAmount)
    {
        this.assetInputAmount = assetInputAmount;
    }

    public List<Map<String, Object>> getAssetOutputAmount()
    {
        return assetOutputAmount;
    }

    public void setAssetOutputAmount(List<Map<String, Object>> assetOutputAmount)
    {
        this.assetOutputAmount = assetOutputAmount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Transaction that = (Transaction) o;

        return hash.equals(that.hash);
    }

    public class UTXOInput implements Serializable {
        private static final long serialVersionUID = 1L;

        @SerializedName("ReferTxID")
        private String referTxID;

        @SerializedName("ReferTxOutputIndex")
        private Integer referTxOutputIndex;

        @DBRef(lazy=true)
        @SerializedName("Output")
        private TransactionOutput output;

        public String getReferTxID()
        {
            return referTxID;
        }

        public void setReferTxID(String referTxID)
        {
            this.referTxID = referTxID;
        }

        public Integer getReferTxOutputIndex()
        {
            return referTxOutputIndex;
        }

        public void setReferTxOutputIndex(Integer referTxOutputIndex)
        {
            this.referTxOutputIndex = referTxOutputIndex;
        }

        public TransactionOutput getOutput()
        {
            return output;
        }

        public void setOutput(TransactionOutput output)
        {
            this.output = output;
        }
    }

    public class Payload
    {
        /**
         * 资产信息
         */
        @DBRef(lazy=true)
        @SerializedName("Asset")
        private Asset asset;

        /**
         * 资产总数
         */
        @SerializedName("Amount")
        private BigDecimal amount;

        /**
         * 发行人公钥
         */
        @SerializedName("Issuer")
        private Issuer issuer;

        /**
         * 资产管理员地址
         */
        @SerializedName("Controller")
        private String controller;

        @SerializedName("Nonce")
        private String nonce;
        
        /**
         * 记录数据-类型
         *     -- record
         */
        @SerializedName("RecordType")
        private String recordType;
        
        /**
         * 记录数据-信息
         */
        @SerializedName("RecordData")
        private String recordData;
        
        /**
         * 解析数据信息
         */
        private MeatInfo meatInfo;
        
        /**
         * 解析数据
         */
        public MeatInfo getMeatInfo() {
            MeatInfo meatInfo = new MeatInfo();
            try{
                Map<String, Object> map = BlockUtils.toAddressAndMeatInfo(recordData);
                if(map.get(Constants.RECORDTYPE_DATA) == null) {
                    meatInfo.setNoMeatInfo(BlockUtils.toPrimitiveData(recordData));
                }else {
                    meatInfo = (MeatInfo)map.get(Constants.RECORDTYPE_DATA);
                    //小数 转 百分比
                    meatInfo.setRes(CommonUtils.toPercentage(meatInfo.getRes()));
                }
            }catch(Exception e) {
                meatInfo.setNoMeatInfo(BlockUtils.toPrimitiveData(recordData));
            }
            return meatInfo;
        }

        public void setMeatInfo(MeatInfo meatInfo) {
            this.meatInfo = meatInfo;
        }

        public String getRecordType() {
            return recordType;
        }

        public void setRecordType(String recordType) {
            this.recordType = recordType;
        }

        public String getRecordData() {
            return recordData;
        }

        public void setRecordData(String recordData) {
            this.recordData = recordData;
        }

        public Asset getAsset()
        {
            return asset;
        }

        public void setAsset(Asset asset)
        {
            this.asset = asset;
        }

        public BigDecimal getAmount()
        {
            return amount;
        }

        public void setAmount(BigDecimal amount)
        {
            this.amount = amount;
        }

        public Issuer getIssuer()
        {
            return issuer;
        }

        public void setIssuer(Issuer issuer)
        {
            this.issuer = issuer;
        }

        public String getController()
        {
            return controller;
        }

        public void setController(String controller)
        {
            this.controller = controller;
        }

        public String getNonce()
        {
            return nonce;
        }

        public void setNonce(String nonce)
        {
            this.nonce = nonce;
        }

        public class Issuer
        {
            @SerializedName("X")
            private String x;

            @SerializedName("Y")
            private String y;

            public String getX()
            {
                return x;
            }

            public void setX(String x)
            {
                this.x = x;
            }

            public String getY()
            {
                return y;
            }

            public void setY(String y)
            {
                this.y = y;
            }
        }
    }
}
