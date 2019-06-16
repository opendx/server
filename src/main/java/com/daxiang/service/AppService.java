package com.daxiang.service;

import com.daxiang.mbg.mapper.AppMapper;
import com.daxiang.mbg.po.App;
import com.daxiang.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created by jiangyitao.
 */
@Service
public class AppService extends BaseService {

    @Autowired
    private UploadService uploadService;
    @Autowired
    private AppMapper appMapper;

    public Response upload(App app, MultipartFile file, HttpServletRequest request) {
        Response response = uploadService.uploadFile(file, request);
        if(!response.isSuccess()) {
            return response;
        }

        String downloadUrl = ((Map<String, String>) (response.getData())).get("downloadURL");
        app.setDownloadUrl(downloadUrl);

        app.setUploadTime(new Date());
        app.setUploadorUid(getUid());

        int insertRow = appMapper.insertSelective(app);
        return insertRow == 1 ? Response.success("上传成功") : Response.fail("上传失败");
    }
}
