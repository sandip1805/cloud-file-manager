package com.inventiverhino.cloudfilemanager;

import com.inventiverhino.cloudfilemanager.properties.S3Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(
	value = {S3Properties.class}
)
public class CloudFileManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(CloudFileManagerApplication.class, args);
	}

}
