package com.daxiang.model;

import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class UploadFile {

    public static final String UPLOAD_FILE_PATH = "upload";

    public static final String IMG_PATH = UPLOAD_FILE_PATH + "/img";
    public static final String VIDEO_PATH = UPLOAD_FILE_PATH + "/video";
    public static final String APP_PATH = UPLOAD_FILE_PATH + "/app";
    public static final String DRIVER_PATH = UPLOAD_FILE_PATH + "/driver";
    public static final String OTHER_FILE_PATH = UPLOAD_FILE_PATH + "/other";

    private String filePath;
    private String downloadUrl;
}
