package com.daxiang.model;

import lombok.Data;

import java.io.File;

/**
 * Created by jiangyitao.
 */
@Data
public class UploadFile {
    private String filePath;
    private String downloadUrl;
    private File file;
}
