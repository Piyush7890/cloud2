package com.kshitij.cloudprint.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cloudprint")
public class CloudPrintProperties {
    String clientId;
    String token;
    String proxy;
    String printerId;
    String baseUrl;
}
