package com.yqhp.controller;

import com.yqhp.model.Response;
import com.yqhp.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    //上传图片存放路径
    @Value("${web.upload-img-path}")
    private String uploadImgPath;
    //上传视频存放路径
    @Value("${web.upload-video-path}")
    private String uploadVideoPath;

    @PostMapping("/file")
    public Response uploadFile(MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return Response.fail("文件不能为空");
        }

        try {
            String newFileName = UUIDUtil.getUUID() + "." + StringUtils.unqualify(file.getOriginalFilename());

            if (newFileName.endsWith(".jpg") || newFileName.endsWith(".png")) {
                //不用绝对路径会报错
                file.transferTo(new File(new File(uploadImgPath).getAbsolutePath() + File.separator + newFileName));
            } else if (newFileName.endsWith(".mp4")) {
                file.transferTo(new File(new File(uploadVideoPath).getAbsolutePath() + File.separator + newFileName));
            } else {
                return Response.fail("暂不支持该格式文件上传");
            }

            //下载地址
            String downloadURL = request.getScheme() + "://" + InetAddress.getLocalHost().getHostAddress() + ":" + request.getServerPort() + "/" + newFileName;
            Map map = new HashMap();
            map.put("downloadURL", downloadURL);

            return Response.success("上传成功", map);
        } catch (Exception e) {
            log.error("上传出错", e);
            return Response.fail(e.getMessage());
        }

    }

}
