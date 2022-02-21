package com.inventiverhino.cloudfilemanager.services.impl;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.inventiverhino.cloudfilemanager.model.CloudFile;
import com.inventiverhino.cloudfilemanager.model.ListFilesResponse;
import com.inventiverhino.cloudfilemanager.services.CloudFileManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            amazonS3.putObject(cloudFile.getBucketName(), cloudFile.getFileName(), cloudFile.getInputStream(), objectMetadata);
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode()), "Request processing failed at cloud platform", e);
        } catch (SdkClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process your request", e);
        }
    }

    @Override
    public InputStream download(CloudFile cloudFile) {
        if (amazonS3.doesObjectExist(cloudFile.getBucketName(), cloudFile.getFileName())) {
            S3Object s3Object = amazonS3.getObject(cloudFile.getBucketName(), cloudFile.getFileName());
            return s3Object.getObjectContent();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested file does not exist on bucket");
        }
    }

    @Override
    public Optional<ListFilesResponse> listFiles(String bucketName, int maxKeys) {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName, "", "", "", maxKeys > 0 ? maxKeys : 100);
        ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);
        if (null != objectListing) {
            List<String> files = objectListing.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
            return Optional.of(
                    ListFilesResponse
                            .builder()
                            .files(files)
                            .totalFiles(files.size())
                            .build()
            );
        }
        return Optional.empty();
    }

}
