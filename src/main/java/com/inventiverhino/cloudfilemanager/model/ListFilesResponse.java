package com.inventiverhino.cloudfilemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListFilesResponse {
    private List<String> files;
    private int totalFiles;
}
