package com.daxiang.model.enums;

import java.util.stream.Stream;

/**
 * Created by jiangyitao.
 */
public enum UploadDir {

    TMP(-1, "upload/tmp"),
    IMG(1, "upload/img"),
    VIDEO(2, "upload/video"),
    APP(3, "upload/app"),
    DRIVER(4, "upload/driver"),
    LOG(5, "upload/log"),
    AGENT_EXT_JAR(6, "upload/agent_ext_jar");

    public int fileType;
    public String path;

    UploadDir(int fileType, String path) {
        this.fileType = fileType;
        this.path = path;
    }

    public static UploadDir get(int fileType) {
        return Stream.of(UploadDir.values())
                .filter(uploadDir -> uploadDir.fileType == fileType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknow fileType=" + fileType));
    }

}