package com.inventiverhino.cloudfilemanager.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.io.ByteArrayInputStream;

import com.inventiverhino.cloudfilemanager.model.CloudFile;
import com.inventiverhino.cloudfilemanager.services.CloudFileManagerService;

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

    private final String FILE_NAME = "testFile.txt";
    private final String BUCKET_NAME = "cloud-file-manager";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudFileManagerService cloudFileManagerService;

    @Test
    void testUpload() throws Exception {        
        byte[] bytes = "Hello".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        CloudFile cloudFile = CloudFile
                .builder()
                .fileName(FILE_NAME)
                .bucketName(BUCKET_NAME)
                .inputStream(inputStream)
                .build();
        doNothing().when(cloudFileManagerService).upload(cloudFile);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", FILE_NAME, "text/plain", bytes);
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

    @Test
    void testDownload() throws Exception {        
        doReturn(new ByteArrayInputStream("Test".getBytes())).when(cloudFileManagerService).download(any(CloudFile.class));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/cloud/filemanager").param("fileName", FILE_NAME))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(result -> assertEquals("application/octet-stream", result.getResponse().getContentType()))
                .andExpect(result -> assertEquals("Test", result.getResponse().getContentAsString()));
    }

    @Test
    void givenBadArguments_whenGetDownload_thenBadRequest() throws Exception {        
        doReturn(new ByteArrayInputStream("Test".getBytes())).when(cloudFileManagerService).download(any(CloudFile.class));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/cloud/filemanager"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(result -> assertEquals("Required request parameter 'fileName' for method parameter type String is not present", result.getResolvedException().getMessage()));
    }
}
