package com.inventiverhino.cloudfilemanager.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.inventiverhino.cloudfilemanager.model.CloudFile;
import com.inventiverhino.cloudfilemanager.services.CloudFileManagerService;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultCloudFileManagerService implements CloudFileManagerService {
    
    private final AmazonS3 amazonS3;

    @Override
    public void upload(CloudFile cloudFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        cloudFile.getOptionalMetaData().ifPresent(map -> {            
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            amazonS3.putObject(cloudFile.getPath(), cloudFile.getFileName(), cloudFile.getInputStream(), objectMetadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }        
    }

}
