package com.ericliu.mongodb.plus;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: liuhaoeric
 * Create time: 2020/04/18
 * Description:
 */
@ConfigurationProperties(prefix = "mongo.client.option")
public class MongoClientOptionProperties {
    private int connectionsPerHost = 20;
    private int minConnectionsPerHost = 20;
    private int connectTimeout = 6000;
    private int maxWaitTime = 6000;
    private int socketTimeout = 0;
    private int heartbeatFrequency = 0;

    public int getConnectionsPerHost() {
        return connectionsPerHost;
    }

    public MongoClientOptionProperties setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
        return this;
    }

    public int getMinConnectionsPerHost() {
        return minConnectionsPerHost;
    }

    public MongoClientOptionProperties setMinConnectionsPerHost(int minConnectionsPerHost) {
        this.minConnectionsPerHost = minConnectionsPerHost;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public MongoClientOptionProperties setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public MongoClientOptionProperties setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public MongoClientOptionProperties setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public int getHeartbeatFrequency() {
        return heartbeatFrequency;
    }

    public MongoClientOptionProperties setHeartbeatFrequency(int heartbeatFrequency) {
        this.heartbeatFrequency = heartbeatFrequency;
        return this;
    }
}
