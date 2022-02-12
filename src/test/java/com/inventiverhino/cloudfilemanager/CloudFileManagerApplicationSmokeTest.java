package com.inventiverhino.cloudfilemanager;

import com.inventiverhino.cloudfilemanager.api.CloudFileManagerAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CloudFileManagerApplicationSmokeTest {

    @Autowired
    private CloudFileManagerAPI cloudFileManagerAPI;

    @Test
    public void contextLoads() {
        assertThat(cloudFileManagerAPI).isNotNull();
    }
    
}
