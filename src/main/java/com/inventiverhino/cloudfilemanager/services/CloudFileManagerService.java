package com.inventiverhino.cloudfilemanager.services;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.inventiverhino.cloudfilemanager.model.CloudFile;
import com.inventiverhino.cloudfilemanager.model.ListFilesResponse;


public interface CloudFileManagerService {
    void upload(CloudFile cloudFile);
    InputStream download(CloudFile cloudFile);
    Optional<ListFilesResponse> listFiles(String bucketName, int maxKeys);
}
