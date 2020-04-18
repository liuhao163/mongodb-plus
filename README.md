
## 背景

我负责的一个项目采用了MongoDB作为数据源，同时使用了springboot生态顺利成章的使用了**org.springframework.boot:spring-boot-starter-data-mongodb**。开始配置采用下面的链接方式

```java
spring.data.mongodb.uri=mongodb://127.0.0.1:27017/faceguard?retryWrites=true
```

这种方案我们遇到了俩个问题：

1. 很难对连接池进行定制化的设计，连连接数都设置不了。
2. 更为重要的我们对mongodb的server加了4层的负载，而这种链接方式不自持心跳导致我们的链接空闲一段时间内会自己断掉；

## 所以开发了MongoConfig的配置，application.properties的配置

```java
mongo.database=faceguardprivatedemo
mongo.host=10.96.91.142
mongo.port=27017
mongo.client.option.connectionsPerHost=20
mongo.client.option.minConnectionsPerHost=20
mongo.client.option.connectTimeout=6000
mongo.client.option.maxWaitTime=6000
mongo.client.option.socketTimeout=0
```