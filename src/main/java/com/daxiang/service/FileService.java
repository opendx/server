package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.model.FileType;
import com.daxiang.model.UploadFile;
import com.daxiang.utils.HttpServletUtil;
import com.daxiang.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Service
public class FileService {

    public static final String UPLOAD_DIR = "upload";

    public static final String TMP_DIR = UPLOAD_DIR + "/tmp";
    public static final String IMG_DIR = UPLOAD_DIR + "/img";
    public static final String VIDEO_DIR = UPLOAD_DIR + "/video";
    public static final String APP_DIR = UPLOAD_DIR + "/app";
    public static final String DRIVER_DIR = UPLOAD_DIR + "/driver";
    public static final String OTHER_FILE_DIR = UPLOAD_DIR + "/other";

    @Value("${static-location}/")
    private String staticLocation;

    public void deleteQuietly(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }

        File file = new File(staticLocation + filePath);
        boolean deleted = FileUtils.deleteQuietly(file);
        if (deleted) {
            log.info("delete {} success", file.getAbsolutePath());
        }
    }

    public void mkUploadDirIfNotExists() {
        File uploadTmpDir = new File(staticLocation + TMP_DIR);
        if (!uploadTmpDir.exists()) {
            log.info("创建tmp目录 -> {}", uploadTmpDir.getAbsolutePath());
            uploadTmpDir.mkdirs();
        }

        File uploadImgDir = new File(staticLocation + IMG_DIR);
        if (!uploadImgDir.exists()) {
            log.info("创建img目录 -> {}", uploadImgDir.getAbsolutePath());
            uploadImgDir.mkdirs();
        }

        File uploadVideoDir = new File(staticLocation + VIDEO_DIR);
        if (!uploadVideoDir.exists()) {
            log.info("创建video目录 -> {}", uploadVideoDir.getAbsolutePath());
            uploadVideoDir.mkdirs();
        }

        File uploadAppDir = new File(staticLocation + APP_DIR);
        if (!uploadAppDir.exists()) {
            log.info("创建app目录 -> {}", uploadAppDir.getAbsolutePath());
            uploadAppDir.mkdirs();
        }

        File uploadDriverDir = new File(staticLocation + DRIVER_DIR);
        if (!uploadDriverDir.exists()) {
            log.info("创建driver目录 -> {}", uploadDriverDir.getAbsolutePath());
            uploadDriverDir.mkdirs();
        }

        File uploadOtherFileDir = new File(staticLocation + OTHER_FILE_DIR);
        if (!uploadOtherFileDir.exists()) {
            log.info("创建other file目录 -> {}", uploadOtherFileDir.getAbsolutePath());
            uploadOtherFileDir.mkdirs();
        }
    }

    public UploadFile upload(MultipartFile file, Integer fileType) {
        if (file == null || fileType == null) {
            throw new ServerException("file or fileType不能为空");
        }

        String uploadFileDir;
        switch (fileType) {
            case FileType.TMP:
                uploadFileDir = TMP_DIR;
                break;
            case FileType.IMG:
                uploadFileDir = IMG_DIR;
                break;
            case FileType.VIDEO:
                uploadFileDir = VIDEO_DIR;
                break;
            case FileType.APP:
                uploadFileDir = APP_DIR;
                break;
            case FileType.DRIVER:
                uploadFileDir = DRIVER_DIR;
                break;
            default:
                uploadFileDir = OTHER_FILE_DIR;
        }

        String originalFilename = file.getOriginalFilename();
        String destFilePath = uploadFileDir + "/" + UUIDUtil.getUUIDFilename(originalFilename);

        try {
            log.info("upload fileType: {}, {} -> {}", fileType, originalFilename, destFilePath);
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(staticLocation + destFilePath));
        } catch (IOException e) {
            log.error("write {} to {} err", originalFilename, destFilePath, e);
            throw new ServerException(e.getMessage());
        }

        UploadFile uploadFile = new UploadFile();
        uploadFile.setFilePath(destFilePath);
        uploadFile.setDownloadUrl(HttpServletUtil.getStaticResourceUrl(destFilePath));

        return uploadFile;
    }

    public void moveFile(String srcFilePath, String destFilePath) throws IOException {
        File src = new File(staticLocation + srcFilePath);
        File dest = new File(staticLocation + destFilePath);
        log.info("move {} -> {}", src, dest);
        FileUtils.moveFile(src, dest);
    }

    public void clearTmpFilesBefore(int beforeDays) {
        long currentTimeMillis = System.currentTimeMillis();
        long beforeMs = TimeUnit.DAYS.toMillis(beforeDays);

        File tmpDir = new File(staticLocation + TMP_DIR);
        File[] files = tmpDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (currentTimeMillis - file.lastModified() >= beforeMs) {
                    boolean isDeleted = file.delete();
                    if (isDeleted) {
                        log.info("delete {} success", file.getAbsolutePath());
                    } else {
                        log.warn("delete {} fail", file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
