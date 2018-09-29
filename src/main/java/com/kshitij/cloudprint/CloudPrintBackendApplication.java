package com.kshitij.cloudprint;

import com.kshitij.cloudprint.configuration.CloudPrintProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        CloudPrintProperties.class
})
@SpringBootApplication
public class CloudPrintBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudPrintBackendApplication.class, args);
    }
}
