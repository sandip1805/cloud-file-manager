package com.inventiverhino.cloudfilemanager.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "s3")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class S3Properties {
    private String accessKey;
    private String secretKey;
    private String region;
}
