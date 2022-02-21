package com.inventiverhino.cloudfilemanager.api;

import java.io.IOException;
import java.util.Optional;

import com.inventiverhino.cloudfilemanager.model.CloudFile;
import com.inventiverhino.cloudfilemanager.services.CloudFileManagerService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/cloud/filemanager")
@RequiredArgsConstructor
public class CloudFileManagerAPI {

    private static final String BUCKET_NAME = "cloud-file-manager";
    private final CloudFileManagerService cloudFileManagerService;

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {
        CloudFile cloudFile = CloudFile
                .builder()
                .fileName(file.getOriginalFilename())
                .optionalMetaData(Optional.empty()) // This is added empty as right now we are accepting from request param or body
                .bucketName(BUCKET_NAME)
                .inputStream(file.getInputStream()).build();
        cloudFileManagerService.upload(cloudFile);
        return ResponseEntity.ok("File Uploaded Successfully");
    }

    @GetMapping
    public ResponseEntity<Resource> download(@RequestParam String fileName) {
        if (!StringUtils.hasText(fileName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fileName is missing");
        }
        CloudFile cloudFile = CloudFile
                .builder()
                .fileName(fileName)
                .optionalMetaData(Optional.empty())
                .bucketName(BUCKET_NAME)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        
        InputStreamResource resource = new InputStreamResource(cloudFileManagerService.download(cloudFile));
        return ResponseEntity.ok()
                .headers(headers)                
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}