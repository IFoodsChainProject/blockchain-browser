package com.ifoods.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ifoods.common.model.CodeMsg;
import com.ifoods.common.model.ResponseModel;
import com.ifoods.model.Address;
import com.ifoods.model.Block;
import com.ifoods.model.SimpleBlock;
import com.ifoods.model.SimpleTran;
import com.ifoods.model.Transaction;
import com.ifoods.service.blockchain.BlockchainService;

import lombok.extern.slf4j.Slf4j;

/**
 * api 数据返回
 * @date 2018年5月21日
 */
@RestController
@RequestMapping("/api/ifood")
@Slf4j
public class ApiController {
    
    private static Integer addressCount = 5;
    
    @Value("${pageSize}")
    private Integer pageSize;

    @Autowired
    private BlockchainService blockchainService;
    
    /**
     * 获得区块分页列表
     */
    @ResponseBody
    @RequestMapping("/block")
    public ResponseModel getBlockRecent(@RequestParam(value = "pageNumber", required=false, defaultValue="1") String pageNumberStr, 
                                    @RequestParam(value="currenttime", required=false, defaultValue="") String currenttimeStr) {
        Long currenttime = System.currentTimeMillis()/1000;
        try {
            if(!StringUtils.isEmpty(currenttimeStr)) {
                currenttime = Long.parseLong(currenttimeStr);
            }
        }catch(Exception e) {
        }
        
        Integer pageNumber = 1;
        try {
            if(!StringUtils.isEmpty(pageNumberStr)) {
                pageNumber = Integer.parseInt(pageNumberStr);
            }
        }catch(Exception e) {
        }
        List<SimpleBlock> list = blockchainService.simpleBlockListByTimeDesc(currenttime, pageNumber, pageSize);
        
        ResponseModel responseModel = new ResponseModel();
        responseModel.setData(list);
        
        return responseModel;
    }
    
    /**
     * 获得交易的分页列表
     */
    @ResponseBody
    @RequestMapping("/tx")
    public ResponseModel getBlockByPage(
            @RequestParam(value = "pageNumber", required=false, defaultValue="1") Integer pageNumber, 
            @RequestParam(value="currenttime", required=false, defaultValue="0") Long currenttime,
            @RequestParam(value="excludeTxTypes", required=false, defaultValue="") String excludeTxTypes) {
        if(currenttime == null || currenttime == 0L) {
            currenttime = System.currentTimeMillis();
        }
        List<Integer> excludeList = new ArrayList<>(0);
        if(!StringUtils.isEmpty(excludeTxTypes)) {
            for(String txTypeStr : excludeTxTypes.split(",")) {
                try {
                    excludeList.add(Integer.parseInt(txTypeStr));
                }catch(Exception e) {
                }
            }
        }
        try {
            Long totalNum = blockchainService.getTransactionCount(currenttime, excludeList);
            List<Transaction> list = blockchainService.getTransactionExcludeTxtype(currenttime, pageNumber, pageSize, excludeList);
            if(list == null) {
                return new ResponseModel();
            }
            List<SimpleTran> listSimple = new ArrayList<>();
            for (Transaction transaction : list) {
                listSimple.add(new SimpleTran(transaction, pageNumber, totalNum));
            }
            ResponseModel responseModel = new ResponseModel();
            responseModel.setData(listSimple);
            
            return responseModel;
        }catch(Exception e) {
            log.error("get tx by page error. pageSize={} timestamp={}", pageSize, currenttime, e);
            return new ResponseModel(CodeMsg.SYSTEM_ERROR);
        }
    }
    
    /**
     * 获得地址的分页列表
     */
    @ResponseBody
    @RequestMapping("/address")
    public ResponseModel getAddressByPage(
            @RequestParam(value = "pageNumber", required=false, defaultValue="1") Integer pageNumber, 
            @RequestParam(value="currenttime", required=false, defaultValue="0") Long currenttime) {
        if(currenttime == null || currenttime == 0L) {
            currenttime = System.currentTimeMillis();
        }
        
        List<Address> addressList = blockchainService.getAddressPage(currenttime, pageNumber, pageSize);
        
        ResponseModel responseModel = new ResponseModel();
        responseModel.setData(addressList);
        
        return responseModel;
    }
    
    /**
     * 根据高度值获得指定的区块
     */
    @ResponseBody
    @RequestMapping("/block/{height}")
    public ResponseModel getBlockByHeight(@PathVariable("height") String height) {
        Long blockId = null;
        try {
            blockId = Long.parseLong(height);
        }catch(Exception e) {
            blockId = 0L;
        }
        
        Block block = blockchainService.getblockInfoByHeight(blockId);
        
        ResponseModel responseModel = new ResponseModel();
        responseModel.setData(block);
        
        return responseModel;
    }
    
    /**
     *  交易明细展示;
     */
    @ResponseBody
    @RequestMapping("/tx/{hash}")
    public ResponseModel getTransactionByHash(@PathVariable("hash") String hash) {
        
        Transaction transaction = blockchainService.getTransactionByHash(hash);
        
        ResponseModel responseModel = new ResponseModel();
        responseModel.setData(transaction);
        
        return responseModel;
    }
    
    /**
     *  地址明细展示;
     *  默认5条
     */
    @ResponseBody
    @RequestMapping("/address/{id}")
    public ResponseModel getAddressById(@PathVariable("id") String id, 
                    @RequestParam(value="count", required=false, defaultValue="50") Integer count,
                    @RequestParam(value="currenttime", required=false, defaultValue="0") Long currenttime) {
        
        if(StringUtils.isEmpty(count)) {
            count = addressCount;
        }
        if(currenttime == null || currenttime == 0L) {
            currenttime = System.currentTimeMillis()/1000;
        }
        
        Address address = blockchainService.getAddressById(id, count, currenttime);
        
        ResponseModel responseModel = new ResponseModel();
        responseModel.setData(address);
        
        return responseModel;
    }
    
    /**
     * 搜索
     */
    @ResponseBody
    @RequestMapping("/search/{value}")
    public ResponseModel search(@PathVariable("value") String value) {
        ResponseModel responseModel = new ResponseModel();
        Map<String, Object> resultMap = new HashMap<>();
        //height
        try {
            Block block = blockchainService.getblockInfoByHeight(Long.parseLong(value));
            if(block != null) {
                resultMap.put("block", block);
                responseModel.setData(resultMap);
                return responseModel;
            }
        }catch(Exception e) {
        }
        try {
            Block block = blockchainService.getblockInfoByHash(value);
            if(block != null) {
                resultMap.put("block", block);
                responseModel.setData(resultMap);
                return responseModel;
            }
        }catch(Exception e) {
        }
        
        //hash
        try {
            Transaction transaction = blockchainService.getTransactionByHash(value);
            if(transaction != null) {
                resultMap.put("tx", transaction);
                responseModel.setData(resultMap);
                
                return responseModel;
            }
        }catch (Exception e) {
        }
        
        //address
        try {
            Address address = blockchainService.getAddressById(value, addressCount, System.currentTimeMillis()/1000);
            if(address != null) {
                resultMap.put("address", address);
                responseModel.setData(resultMap);
                
                return responseModel;
            }
        }catch (Exception e) {
        }
        responseModel.setCode(CodeMsg.NO_SEARCH_RECORD);
        
        return responseModel;
    }
}
