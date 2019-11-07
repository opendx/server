package com.daxiang.service;

import com.daxiang.model.Response;
import com.daxiang.utils.UUIDUtil;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

    @Value("${web.upload-img-path}")
    private String uploadImgPath;
    @Value("${web.upload-video-path}")
    private String uploadVideoPath;
    @Value("${web.upload-apk-path}")
    private String uploadApkPath;
    @Value("${web.upload-ipa-path}")
    private String uploadIpaPath;
    @Value("${web.upload-other-path}")
    private String uploadOtherPath;

    public Response uploadFile(MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return Response.fail("文件不能为空");
        }

        String newFileName = UUIDUtil.getUUID() + "." + StringUtils.unqualify(file.getOriginalFilename());
        try {
            if (newFileName.endsWith(".jpg") || newFileName.endsWith(".png") || newFileName.endsWith(".tiff")) {
                // 不用绝对路径会报错
                file.transferTo(new File(new File(uploadImgPath).getAbsolutePath() + File.separator + newFileName));
            } else if (newFileName.endsWith(".mp4")) {
                file.transferTo(new File(new File(uploadVideoPath).getAbsolutePath() + File.separator + newFileName));
            } else if (newFileName.endsWith(".apk")) {
                file.transferTo(new File(new File(uploadApkPath).getAbsolutePath() + File.separator + newFileName));
            } else if (newFileName.endsWith(".ipa")) {
                file.transferTo(new File(new File(uploadIpaPath).getAbsolutePath() + File.separator + newFileName));
            } else {
                file.transferTo(new File(new File(uploadOtherPath).getAbsolutePath() + File.separator + newFileName));
            }
        } catch (IOException e) {
            log.error("transfer err", e);
            return Response.fail(e.getMessage());
        }

        String downloadURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + newFileName;
        return Response.success("上传成功", ImmutableMap.of("downloadURL", downloadURL));
    }
}
