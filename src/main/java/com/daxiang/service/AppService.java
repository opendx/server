package com.daxiang.service;

import com.daxiang.agent.AgentApi;
import com.daxiang.mbg.mapper.AppMapper;
import com.daxiang.mbg.po.App;
import com.daxiang.mbg.po.AppExample;
import com.daxiang.model.*;
import com.daxiang.model.vo.AgentVo;
import com.daxiang.model.vo.AppVo;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class AppService extends BaseService {

    @Autowired
    private UploadService uploadService;
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private AgentService agentService;
    @Autowired
    private AgentApi agentApi;

    public Response upload(App app, MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return Response.fail("上传文件不能为空");
        }
        if (app.getPlatform() == Platform.ANDROID) {
            if (!file.getOriginalFilename().endsWith(".apk")) {
                return Response.fail("Android必须为apk文件");
            }
        }

        Response response = uploadService.uploadFile(file, request);
        if (!response.isSuccess()) {
            return response;
        }

        String downloadUrl = ((Map<String, String>) (response.getData())).get("downloadURL");
        app.setDownloadUrl(downloadUrl);
        app.setUploadTime(new Date());
        app.setUploadorUid(getUid());

        int insertRow = appMapper.insertSelective(app);
        return insertRow == 1 ? Response.success("上传成功") : Response.fail("上传失败");
    }

    public Response delete(Integer appId) {
        if (appId == null) {
            return Response.fail("appId不能为空");
        }

        int delRow = appMapper.deleteByPrimaryKey(appId);
        return delRow == 1 ? Response.success("删除成功") : Response.fail("删除失败，请稍后再试");
    }

    public Response list(App app, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<App> apps = selectByApp(app);
        List<AppVo> appVos = apps.stream().map(a -> AppVo.convert(a, UserCache.getNickNameById(a.getUploadorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用apps计算total
            long total = Page.getTotal(apps);
            return Response.success(Page.build(appVos, total));
        } else {
            return Response.success(appVos);
        }
    }

    public List<App> selectByApp(App app) {
        if (app == null) {
            app = new App();
        }

        AppExample appExample = new AppExample();
        AppExample.Criteria criteria = appExample.createCriteria();

        if (app.getId() != null) {
            criteria.andIdEqualTo(app.getId());
        }
        if (app.getPlatform() != null) {
            criteria.andPlatformEqualTo(app.getPlatform());
        }
        if (app.getProjectId() != null) {
            criteria.andProjectIdEqualTo(app.getProjectId());
        }
        appExample.setOrderByClause("upload_time desc");

        return appMapper.selectByExample(appExample);
    }

    public Response aaptDumpBadging(Integer appId) {
        if (appId == null) {
            return Response.fail("appId不能为空");
        }

        App app = appMapper.selectByPrimaryKey(appId);
        if (app == null) {
            return Response.fail("app不存在");
        }
        if (app.getPlatform() != Platform.ANDROID) {
            return Response.fail("只有Android平台才能执行aapt dump");
        }

        List<AgentVo> onlineAgents = agentService.getOnlineAgent();
        if (CollectionUtils.isEmpty(onlineAgents)) {
            return Response.fail("暂无在线的agent，无法执行aapt dump");
        }
        // 任意一个在线的agent
        AgentVo agentVo = onlineAgents.stream().findAny().get();
        Response agentResponse = agentApi.aaptDumpBadging(agentVo.getIp(), agentVo.getPort(), app.getDownloadUrl());
        if (!agentResponse.isSuccess()) {
            return agentResponse;
        }

        String dumpInfo = (String) agentResponse.getData();
        if (StringUtils.isEmpty(dumpInfo)) {
            return Response.fail("aapt dump信息为空");
        }

        log.info("appId: {} => {}", appId, dumpInfo);

        String version = org.apache.commons.lang3.StringUtils.substringBetween(dumpInfo, "versionName='", "'");
        String packageName = org.apache.commons.lang3.StringUtils.substringBetween(dumpInfo, "package: name='", "'");
        String launchActivity = org.apache.commons.lang3.StringUtils.substringBetween(dumpInfo, "launchable-activity: name='", "'");

        app.setVersion(version);
        app.setPackageName(packageName);
        app.setLaunchActivity(launchActivity);

        int updateRow = appMapper.updateByPrimaryKeySelective(app);
        return updateRow == 1 ? Response.success("获取成功") : Response.fail("获取失败，请稍后重试");
    }

    public Response update(App app) {
        if (app.getId() == null) {
            return Response.fail("appId不能为空");
        }

        int updateRow = appMapper.updateByPrimaryKey(app);
        return updateRow == 1 ? Response.success("更新成功") : Response.fail("更新失败，请稍后重试");
    }
}
