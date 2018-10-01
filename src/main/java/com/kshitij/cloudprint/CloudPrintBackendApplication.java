package com.kshitij.cloudprint;

import com.kshitij.cloudprint.configuration.CloudPrintProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class CloudPrintBackendApplication {
    private static Logger logger = LoggerFactory.getLogger(CloudPrintBackendApplication.class);
    @Autowired
    CloudPrintProperties properties;
    public static void main(String[] args) {
        SpringApplication.run(CloudPrintBackendApplication.class, args);
    }

    @PostConstruct
    public void init() {
        logger.info(properties.toString());
    }
}
