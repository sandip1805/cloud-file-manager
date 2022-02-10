package com.inventiverhino.cloudfilemanager.api;

import java.io.IOException;

import com.inventiverhino.cloudfilemanager.model.CloudFile;
import com.inventiverhino.cloudfilemanager.services.CloudFileManagerService;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> save(@RequestParam MultipartFile file) throws IOException {
        CloudFile cloudFile = CloudFile
                .builder()
                .fileName(file.getOriginalFilename())
                .path(Strings.EMPTY)
                .inputStream(file.getInputStream()).build();
        cloudFileManagerService.upload(cloudFile);
        return ResponseEntity.ok("File Uploaded Successfully");
    }

}