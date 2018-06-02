package com.ifoods.common.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ifoods.common.Constants;
import com.ifoods.model.Block;
import com.ifoods.service.blockchain.BlockchainService;

import lombok.extern.slf4j.Slf4j;

/**
 */
@Component
@Slf4j
public class BlockInfoQuartz {
    
    @Autowired
    private BlockchainService blockchainService;
    
    /**
     * 区块信息入库定时器, 只允许一个程序在运行
     */
    //@Scheduled(cron = "0/9 * * * * ?")
    @Scheduled(cron = Constants.GET_BLOCK_TIMER)
    public void getBlockInfoToMongo() {
        log.info("getBlockInfoToMongo start");
        long blockId = 0;
        long dbBlockId = 0;
        long chainBlociId = 0;
        
        try {
            dbBlockId = blockchainService.getMaxBlockIdFromDb();
            chainBlociId = blockchainService.getMaxBlockIdFromBlockchain();
            blockId = dbBlockId;
            
            for (blockId = dbBlockId+1; blockId <chainBlociId; blockId++) {
                blockchainService.findAndSave(blockId);
            }
            
        log.info("getBlockInfoToMongo finish");
        }catch (Exception e) {
            log.error("getBlockInfoToMongo fail. dbBlockId={} | chainBlociId={} | blockId={} | {}", blockId, e);
            return;
        }
    }

    /**
     * 异常监控
     * 上一区块时间距离现在超过10分钟就预警
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void monitorBlock() {
        Block block = blockchainService.getMaxBlockFromDb();
        Long diffTime = System.currentTimeMillis() - block.getBlockData().getTimestamp();
        System.out.println("============" + diffTime);
        if(diffTime < 0 || diffTime < 600) {
            log.error("=========== copy block error ===========");
            //TODO 发送邮件
        }
    }
}
