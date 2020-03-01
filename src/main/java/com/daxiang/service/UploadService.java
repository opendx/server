package com.daxiang.service;

import com.daxiang.model.FileType;
import com.daxiang.model.Response;
import com.daxiang.model.UploadFile;
import com.daxiang.utils.HttpServletUtil;
import com.daxiang.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

    public Response<UploadFile> uploadFile(MultipartFile file, Integer fileType) {
        if (file == null) {
            return Response.fail("文件不能为空");
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

        String destFileName = UUIDUtil.getUUID();
        String originalFilename = file.getOriginalFilename();
        if (originalFilename.contains(".")) {
            destFileName = destFileName + "." + StringUtils.unqualify(originalFilename);
        }

        String destFilePath = uploadFilePath + "/" + destFileName;

        try {
            String destFileAbsolutePath = new File(staticLocation + destFilePath).getAbsolutePath(); // 不用绝对路径transferTo会报错
            log.info("upload fileType: {}, {} -> {}", fileType, originalFilename, destFileAbsolutePath);
            file.transferTo(new File(destFileAbsolutePath));
        } catch (IOException e) {
            log.error("transfer err", e);
            return Response.fail(e.getMessage());
        }

        UploadFile uploadFile = new UploadFile();
        uploadFile.setFilePath(destFilePath);
        uploadFile.setDownloadUrl(HttpServletUtil.getStaticResourceUrl(destFilePath));

        return Response.success("上传成功", uploadFile);
    }
}
