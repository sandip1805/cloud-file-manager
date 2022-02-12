package com.inventiverhino.cloudfilemanager.api;

import java.io.IOException;
import java.util.Optional;

import com.inventiverhino.cloudfilemanager.model.CloudFile;
import com.inventiverhino.cloudfilemanager.services.CloudFileManagerService;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cloud/filemanager")
@RequiredArgsConstructor
public class CloudFileManagerAPI {

    private final CloudFileManagerService cloudFileManagerService;

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {
        CloudFile cloudFile = CloudFile
                .builder()
                .fileName(file.getOriginalFilename())
                .optionalMetaData(Optional.empty())
                .bucketName("cloud-file-manager")
                .inputStream(file.getInputStream()).build();
        cloudFileManagerService.upload(cloudFile);
        return ResponseEntity.ok("File Uploaded Successfully");
    }

}