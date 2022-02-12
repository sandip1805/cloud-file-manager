package com.inventiverhino.cloudfilemanager.model;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloudFile {
    private String bucketName;
    private String fileName;
    private Optional<Map<String, String>> optionalMetaData;
    private InputStream inputStream;
}
