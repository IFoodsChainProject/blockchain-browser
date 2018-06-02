package com.ifoods.config.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月22日
 */
//@Configuration
@Slf4j
public class MongoDBFactory {

    //注入配置实体
    @Autowired
    private MongoSettingProperties mongoSettingProperties;
    
    
    //覆盖默认的MongoDbFactory
    @Bean
    public MongoDbFactory mongoDbFactory() {
        //客户端配置（连接数、副本集群验证）
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(mongoSettingProperties.getConnectionsPerHost());
        builder.minConnectionsPerHost(mongoSettingProperties.getMinConnectionsPerHost());
        if (mongoSettingProperties.getReplicaSet() != null) {
            builder.requiredReplicaSetName(mongoSettingProperties.getReplicaSet());
        }
        MongoClientOptions mongoClientOptions = builder.build();
    
        // MongoDB地址列表
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String host : mongoSettingProperties.getHosts()) {
            Integer index = mongoSettingProperties.getHosts().indexOf(host);
            Integer port = Integer.valueOf(mongoSettingProperties.getPorts().get(index));
    
            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddresses.add(serverAddress);
        }
        log.info("serverAddresses:{}", serverAddresses.toString());
    
        // 连接认证
        List<MongoCredential> mongoCredentialList = new ArrayList<>();
        if (mongoSettingProperties.getUsername() != null) {
            mongoCredentialList.add(MongoCredential.createScramSha1Credential(
                    mongoSettingProperties.getUsername(),
                    mongoSettingProperties.getAuthenticationDatabase() != null ? mongoSettingProperties.getAuthenticationDatabase() : mongoSettingProperties.getDatabase(),
                            mongoSettingProperties.getPassword().toCharArray()));
        }
        log.info("mongoCredentialList:{}", mongoCredentialList.toString());
        //创建客户端和Factory
        MongoClient mongoClient = new MongoClient(serverAddresses, mongoCredentialList, mongoClientOptions);
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, mongoSettingProperties.getDatabase());
        return mongoDbFactory;
    }
    
    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }
    
}
