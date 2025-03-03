package org.hyeong.gooinprochatapi.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {

        String mongoUri = "mongodb://gooinprochatdbuser:gooinprochatdbuser@localhost:27017/gooinprochatdb?authSource=gooinprochatdb";

        MongoClient mongoClient = MongoClients.create(mongoUri);
        return new MongoTemplate(mongoClient, "gooinprochatdb");
    }
}
