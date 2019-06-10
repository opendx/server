package com.daxiang.controller;

import com.daxiang.model.Response;
import com.daxiang.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @Value("${web.upload-img-path}")
    private String uploadImgPath;
    @Value("${web.upload-video-path}")
    private String uploadVideoPath;

    @PostMapping("/file")
    public Response uploadFile(MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return Response.fail("文件不能为空");
        }

        String newFileName;
        try {
            newFileName = UUIDUtil.getUUID() + "." + StringUtils.unqualify(file.getOriginalFilename());
            if (newFileName.endsWith(".jpg") || newFileName.endsWith(".png")) {
                //不用绝对路径会报错
                file.transferTo(new File(new File(uploadImgPath).getAbsolutePath() + File.separator + newFileName));
            } else if (newFileName.endsWith(".mp4")) {
                file.transferTo(new File(new File(uploadVideoPath).getAbsolutePath() + File.separator + newFileName));
            } else {
                return Response.fail("暂不支持该格式文件上传");
            }
        } catch (IOException e) {
            log.error("transfer err", e);
            return Response.fail(e.getMessage());
        }

        try {
            String downloadURL = request.getScheme() + "://" + InetAddress.getLocalHost().getHostAddress() + ":" + request.getServerPort() + "/" + newFileName;
            Map data = new HashMap();
            data.put("downloadURL", downloadURL);
            return Response.success("上传成功", data);
        } catch (UnknownHostException e) {
            log.error("UnknownHost", e);
            return Response.fail(e.getMessage());
        }
    }
}
