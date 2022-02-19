package com.inventiverhino.cloudfilemanager.services;

import java.io.InputStream;

import com.inventiverhino.cloudfilemanager.model.CloudFile;


public interface CloudFileManagerService {
    void upload(CloudFile cloudFile);
    InputStream download(CloudFile cloudFile);
}
