package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.model.UploadFile;
import com.daxiang.model.enums.UploadDir;
import com.daxiang.utils.HttpServletUtil;
import com.daxiang.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
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
        } else {
            log.warn("delete {} fail, the file is maybe not exists", file.getAbsolutePath());
        }
    }

    public void mkUploadDirIfNotExists() {
        File uploadTmpDir = new File(staticLocation + UploadDir.TMP.path);
        if (!uploadTmpDir.exists()) {
            log.info("创建tmp目录 -> {}", uploadTmpDir.getAbsolutePath());
            uploadTmpDir.mkdirs();
        }

        File uploadImgDir = new File(staticLocation + UploadDir.IMG.path);
        if (!uploadImgDir.exists()) {
            log.info("创建img目录 -> {}", uploadImgDir.getAbsolutePath());
            uploadImgDir.mkdirs();
        }

        File uploadVideoDir = new File(staticLocation + UploadDir.VIDEO.path);
        if (!uploadVideoDir.exists()) {
            log.info("创建video目录 -> {}", uploadVideoDir.getAbsolutePath());
            uploadVideoDir.mkdirs();
        }

        File uploadAppDir = new File(staticLocation + UploadDir.APP.path);
        if (!uploadAppDir.exists()) {
            log.info("创建app目录 -> {}", uploadAppDir.getAbsolutePath());
            uploadAppDir.mkdirs();
        }

        File uploadDriverDir = new File(staticLocation + UploadDir.DRIVER.path);
        if (!uploadDriverDir.exists()) {
            log.info("创建driver目录 -> {}", uploadDriverDir.getAbsolutePath());
            uploadDriverDir.mkdirs();
        }

        File uploadLogDir = new File(staticLocation + UploadDir.LOG.path);
        if (!uploadDriverDir.exists()) {
            log.info("创建log目录 -> {}", uploadLogDir.getAbsolutePath());
            uploadLogDir.mkdirs();
        }

        File uploadAgentExtJarDir = new File(staticLocation + UploadDir.AGENT_EXT_JAR.path);
        if (!uploadAgentExtJarDir.exists()) {
            log.info("创建agent ext jar目录 -> {}", uploadAgentExtJarDir.getAbsolutePath());
            uploadAgentExtJarDir.mkdirs();
        }
    }

    public UploadFile upload(MultipartFile file, UploadDir uploadDir) {
        return upload(file, uploadDir, true);
    }

    public UploadFile upload(MultipartFile file, UploadDir uploadDir, boolean renameFile) {
        if (file == null || uploadDir == null) {
            throw new ServerException("file or uploadDir不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String destFilePath = renameFile ? uploadDir.path + "/" + UUIDUtil.getUUIDFilename(originalFilename)
                : uploadDir.path + "/" + originalFilename;
        File destFile = new File(staticLocation + destFilePath);

        try {
            log.info("upload {} -> {}", originalFilename, destFilePath);
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
        } catch (IOException e) {
            log.error("write {} to {} err", originalFilename, destFilePath, e);
            throw new ServerException(e.getMessage());
        }

        UploadFile uploadFile = new UploadFile();
        uploadFile.setFile(destFile);
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

    public int clearTmpFilesBefore(int beforeDays) {
        int deletedTmpFilesCount = 0;

        File[] files = new File(staticLocation + UploadDir.TMP.path).listFiles();
        if (ArrayUtils.isEmpty(files)) {
            return deletedTmpFilesCount;
        }

        long currentTimeMillis = System.currentTimeMillis();
        long beforeMs = TimeUnit.DAYS.toMillis(beforeDays);

        for (File file : files) {
            if (currentTimeMillis - file.lastModified() >= beforeMs) {
                boolean isDeleted = file.delete();
                if (isDeleted) {
                    deletedTmpFilesCount++;
                    log.info("delete {} success", file.getAbsolutePath());
                } else {
                    log.warn("delete {} fail", file.getAbsolutePath());
                }
            }
        }

        return deletedTmpFilesCount;
    }

    public boolean exist(UploadDir uploadDir, String filename) {
        return new File(staticLocation + uploadDir.path, filename).exists();
    }
}
