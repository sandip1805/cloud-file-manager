package com.inventiverhino.cloudfilemanager.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloudFileManagerAPIHttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void uploadShouldReturnSuccess() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new org.springframework.core.io.ClassPathResource("men-canvas-shoes.jpg"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/cloud/filemanager/", HttpMethod.POST, entity, String.class, "");

        // Expect Ok
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response.getBody(), Matchers.is("File Uploaded Successfully"));
    }

    @Test
    public void uploadShouldReturnBadRequestWhenHigherSizeFileUploaded() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new org.springframework.core.io.ClassPathResource("man-in-black-shirt.jpg"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/cloud/filemanager/", HttpMethod.POST, entity, String.class, "");

        // Expect InternalServerError
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
