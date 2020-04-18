package com.ericliu.mongodb.plus;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengkai
 * @date 2019-02-21
 */
@Configuration
@EnableConfigurationProperties(MongoClientOptionProperties.class)
public class MongoConfig {

    @Value("${mongo.database}")
    private String database;
    @Value("${mongo.host}")
    private String host;
    @Value("${mongo.port}")
    private int port;
    @Value("${mongo.username:}")
    private String userName;
    @Value("${mongo.password:}")
    private String password;

    @Bean
    public GridFSBucket getGridFSBuckets(MongoDbFactory mongoDbFactory) {
        MongoDatabase db = mongoDbFactory.getDb();
        return GridFSBuckets.create(db);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);

        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        try {
            mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
        } catch (NoSuchBeanDefinitionException ignore) {
        }

        // Don't save _class to mongo
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return mappingConverter;
    }


    // 覆盖默认的MongoDbFactory
    @Bean
    MongoDbFactory mongoDbFactory(MongoClientOptionProperties mongoClientOptionProperties) {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(mongoClientOptionProperties.getConnectionsPerHost());
        builder.minConnectionsPerHost(mongoClientOptionProperties.getMinConnectionsPerHost());
        builder.connectTimeout(mongoClientOptionProperties.getConnectTimeout());
        builder.maxWaitTime(mongoClientOptionProperties.getMaxWaitTime());
        builder.socketTimeout(mongoClientOptionProperties.getSocketTimeout());
        if (mongoClientOptionProperties.getHeartbeatFrequency() > 0) {
            builder.heartbeatFrequency(mongoClientOptionProperties.getHeartbeatFrequency());
        }
        MongoClientOptions mongoClientOptions = builder.build();

//        // MongoDB地址列表
        List<ServerAddress> serverAddresses = new ArrayList<>();
        ServerAddress serverAddress = new ServerAddress(host, port);
        serverAddresses.add(serverAddress);

        // 连接认证
        MongoClient mongoClient = null;
        if (!Strings.isEmpty(userName)) {
            mongoClient = new MongoClient(serverAddresses, MongoCredential.createCredential(
                    userName,
                    database,
                    password.toCharArray()), mongoClientOptions);
        } else {
            mongoClient = new MongoClient(serverAddresses, mongoClientOptions);
        }

        //创建客户端和Factory
        return new SimpleMongoDbFactory(mongoClient, database);
    }

    @Bean
    MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }
}
