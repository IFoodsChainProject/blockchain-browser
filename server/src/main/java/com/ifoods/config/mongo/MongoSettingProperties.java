package com.ifoods.config.mongo;

import java.util.Arrays;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月22日
 */
//@Component
//@Validated
public class MongoSettingProperties {

    @NotBlank
    private String database;
    
    @NotEmpty
    @Value("${spring.mongodb.custom.hosts}")
    private List<String> hosts;
    @NotEmpty
    @Value("${spring.mongodb.custom.ports}")
    private List<String> ports;
    @Value("${spring.mongodb.custom.replica-set}")
    private String replicaSet;
    @Value("${spring.mongodb.custom.username}")
    private String username;
    @Value("${spring.mongodb.custom.password}")
    private String password;
    @Value("${spring.mongodb.custom.authentication-database}")
    private String authenticationDatabase;
    @Value("${spring.mongodb.custom.connections-per-host}")
    private Integer connectionsPerHost = 2;
    @Value("${spring.mongodb.custom.min-connections-per-host}")
    private Integer minConnectionsPerHost = 10;
    
    public String getDatabase() {
        return database;
    }
    public void setDatabase(String database) {
        this.database = database;
    }
    public List<String> getHosts() {
        return hosts;
    }
    public void setHosts(List<String> hosts) {
        this.hosts = Arrays.asList(hosts.get(0).split(","));
    }
    public List<String> getPorts() {
        return ports;
    }
    public void setPorts(List<String> ports) {
        this.ports = Arrays.asList(ports.get(0).split(","));
    }
    public String getReplicaSet() {
        return replicaSet;
    }
    public void setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAuthenticationDatabase() {
        return authenticationDatabase;
    }
    public void setAuthenticationDatabase(String authenticationDatabase) {
        this.authenticationDatabase = authenticationDatabase;
    }
    public Integer getMinConnectionsPerHost() {
        return minConnectionsPerHost;
    }
    public void setMinConnectionsPerHost(Integer minConnectionsPerHost) {
        this.minConnectionsPerHost = minConnectionsPerHost;
    }
    public Integer getConnectionsPerHost() {
        return connectionsPerHost;
    }
    public void setConnectionsPerHost(Integer connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }
    public MongoSettingProperties() {
        super();
        
    }
}
