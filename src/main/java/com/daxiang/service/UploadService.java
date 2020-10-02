package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.model.FileType;
import com.daxiang.model.UploadFile;
import com.daxiang.utils.HttpServletUtil;
import com.daxiang.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class UploadService {

    @Value("${static-location}/")
    private String staticLocation;

    public UploadFile upload(MultipartFile file, Integer fileType) {
        if (file == null || fileType == null) {
            throw new ServerException("file or fileType不能为空");
        }

        String uploadFilePath;
        switch (fileType) {
            case FileType.IMG:
                uploadFilePath = UploadFile.IMG_PATH;
                break;
            case FileType.VIDEO:
                uploadFilePath = UploadFile.VIDEO_PATH;
                break;
            case FileType.APP:
                uploadFilePath = UploadFile.APP_PATH;
                break;
            case FileType.DRIVER:
                uploadFilePath = UploadFile.DRIVER_PATH;
                break;
            default:
                uploadFilePath = UploadFile.OTHER_FILE_PATH;
        }

        String originalFilename = file.getOriginalFilename();
        String destFilename = UUIDUtil.getUUIDFilename(originalFilename);
        String destFilePath = uploadFilePath + "/" + destFilename;

        try {
            String destFileAbsolutePath = new File(staticLocation + destFilePath).getAbsolutePath(); // 不用绝对路径transferTo会报错
            log.info("upload fileType: {}, {} -> {}", fileType, originalFilename, destFileAbsolutePath);
            file.transferTo(new File(destFileAbsolutePath));
        } catch (IOException e) {
            log.error("transfer err", e);
            throw new ServerException(e.getMessage());
        }

        UploadFile uploadFile = new UploadFile();
        uploadFile.setFilePath(destFilePath);
        uploadFile.setDownloadUrl(HttpServletUtil.getStaticResourceUrl(destFilePath));

        return uploadFile;
    }
}
