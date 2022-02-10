package com.inventiverhino.cloudfilemanager.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.io.ByteArrayInputStream;

import com.inventiverhino.cloudfilemanager.model.CloudFile;
import com.inventiverhino.cloudfilemanager.services.CloudFileManagerService;

import org.apache.logging.log4j.util.Strings;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CloudFileManagerAPI.class)
public class CloudFileManagerAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudFileManagerService cloudFileManagerService;

    @Test
    void testSave() throws Exception {
        String fileName = "testFile.txt";
        byte[] bytes = "Hello".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        CloudFile cloudFile = CloudFile
                .builder()
                .fileName(fileName)
                .path(Strings.EMPTY)
                .inputStream(inputStream)
                .build();
        doNothing().when(cloudFileManagerService).upload(cloudFile);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName, "text/plain", bytes);
        this.mockMvc
                .perform(MockMvcRequestBuilders.multipart("/cloud/filemanager").file(mockMultipartFile))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content().string(Matchers.containsString("File Uploaded Successfully")));
    }

    @Test
    void givenBadArguments_whenPostSave_thenBadRequest() throws Exception {
        String fileName = "testFile.txt";
        byte[] bytes = "Hello".getBytes();        
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test", fileName, "text/plain", bytes);
        this.mockMvc
                .perform(MockMvcRequestBuilders.multipart("/cloud/filemanager").file(mockMultipartFile))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(result -> assertEquals("Required request part 'file' is not present", result.getResolvedException().getMessage()));
    }
}
