package com.kshitij.cloudprint.configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.stereotype.Component;

@Component
public class MongoConfig extends AbstractMongoConfiguration {
    @Value("${spring.data.mongodb.uri}")
    String hostUri;
    @Value("${spring.data.mongodb.database}")
    String databaseName;

    @Override
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(hostUri);
        return new MongoClient(uri);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public GridFSBucket gridFSBucket() {
        return GridFSBuckets.create(mongoClient().getDatabase(databaseName), "file");
    }
}
