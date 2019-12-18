package com.daxiang.service;

import com.daxiang.model.FileType;
import com.daxiang.model.Response;
import com.daxiang.utils.UUIDUtil;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class UploadService {

    @Autowired
    private HttpServletRequest request;

    @Value("${web.upload-img-path}")
    private String uploadImgPath;
    @Value("${web.upload-video-path}")
    private String uploadVideoPath;
    @Value("${web.upload-app-path}")
    private String uploadAppPath;
    @Value("${web.upload-driver-path}")
    private String uploadDriverPath;
    @Value("${web.upload-other-path}")
    private String uploadOtherPath;

    public Response uploadFile(MultipartFile file, Integer fileType) {
        if (file == null) {
            return Response.fail("文件不能为空");
        }

        String uploadPath;
        switch (fileType) {
            case FileType.IMG:
                uploadPath = uploadImgPath;
                break;
            case FileType.VIDEO:
                uploadPath = uploadVideoPath;
                break;
            case FileType.APP:
                uploadPath = uploadAppPath;
                break;
            case FileType.DRIVER:
                uploadPath = uploadDriverPath;
                break;
            default:
                uploadPath = uploadOtherPath;
        }

        String destFileName = UUIDUtil.getUUID() + "_" + file.getOriginalFilename();
        String destFilePath = new File(uploadPath).getAbsolutePath() + File.separator + destFileName; // 不用绝对路径transferTo会报错
        File destFile = new File(destFilePath);
        try {
            log.info("upload fileType: {}, {} -> {}", fileType, file.getOriginalFilename(), destFilePath);
            file.transferTo(destFile);
        } catch (IOException e) {
            log.error("transfer err", e);
            return Response.fail(e.getMessage());
        }

        String downloadURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + destFileName;
        return Response.success("上传成功", ImmutableMap.of("downloadURL", downloadURL));
    }
}
