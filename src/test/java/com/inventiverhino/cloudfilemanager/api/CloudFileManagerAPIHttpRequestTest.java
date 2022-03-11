package com.inventiverhino.cloudfilemanager.api;

import com.inventiverhino.cloudfilemanager.model.ListFilesResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloudFileManagerAPIHttpRequestTest {

    private static final String FILE_NAME = "men-canvas-shoes.jpg";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void uploadShouldReturnSuccess() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new org.springframework.core.io.ClassPathResource(FILE_NAME));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + port + "/cloud/filemanager/", HttpMethod.POST, entity, String.class, "");

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

        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + port + "/cloud/filemanager/", HttpMethod.POST, entity, String.class, "");

        // Expect InternalServerError
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void downloadShouldReturnSuccess() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port).path("/cloud/filemanager/")
                .queryParam("fileName", FILE_NAME).build().toUri();
        ResponseEntity<Resource> response = testRestTemplate.getForEntity(uri, Resource.class);

        // Expect Ok
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response.getBody().getFilename(), Matchers.is(FILE_NAME));
    }

    @Test
    public void listFilesShouldReturnSuccess() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("http://localhost:" + port)
                .path("/cloud/filemanager/listFiles")
                .build()
                .toUri();
        ResponseEntity<ListFilesResponse> response = testRestTemplate.getForEntity(uri, ListFilesResponse.class);

        // Expect Ok
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response.getBody().getTotalFiles(), Matchers.is(1));
    }
}
