package com.daxiang.service;

import com.daxiang.agent.AgentApi;
import com.daxiang.mbg.mapper.AppMapper;
import com.daxiang.mbg.po.App;
import com.daxiang.mbg.po.AppExample;
import com.daxiang.mbg.po.User;
import com.daxiang.model.*;
import com.daxiang.model.vo.AgentVo;
import com.daxiang.model.vo.AppVo;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class AppService {

    @Autowired
    private UploadService uploadService;
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private AgentService agentService;
    @Autowired
    private AgentApi agentApi;
    @Autowired
    private UserService userService;

    public Response upload(App app, MultipartFile file) {
        Response response = uploadService.uploadFile(file, FileType.APP);
        if (!response.isSuccess()) {
            return response;
        }

        String downloadUrl = ((Map<String, String>) (response.getData())).get("downloadURL");
        app.setDownloadUrl(downloadUrl);
        app.setUploadTime(new Date());
        app.setUploadorUid(SecurityUtil.getCurrentUserId());

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
        List<AppVo> appVos = convertAppsToAppVos(apps);

        if (needPaging) {
            long total = Page.getTotal(apps);
            return Response.success(Page.build(appVos, total));
        } else {
            return Response.success(appVos);
        }
    }

    private List<AppVo> convertAppsToAppVos(List<App> apps) {
        if (CollectionUtils.isEmpty(apps)) {
            return Collections.EMPTY_LIST;
        }

        List<Integer> uploadorUids = apps.stream()
                .map(App::getUploadorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByUserIds(uploadorUids);

        return apps.stream().map(app -> {
            AppVo appVo = new AppVo();
            BeanUtils.copyProperties(app, appVo);

            if (app.getUploadorUid() != null) {
                User user = userMap.get(app.getUploadorUid());
                if (user != null) {
                    appVo.setUploadorNickName(user.getNickName());
                }
            }

            return appVo;
        }).collect(Collectors.toList());
    }

    public List<App> selectByApp(App app) {
        AppExample example = new AppExample();
        AppExample.Criteria criteria = example.createCriteria();

        if (app != null) {
            if (app.getId() != null) {
                criteria.andIdEqualTo(app.getId());
            }
            if (app.getPlatform() != null) {
                criteria.andPlatformEqualTo(app.getPlatform());
            }
            if (app.getProjectId() != null) {
                criteria.andProjectIdEqualTo(app.getProjectId());
            }
        }
        example.setOrderByClause("upload_time desc");

        return appMapper.selectByExample(example);
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

        List<AgentVo> onlineAgents = agentService.getOnlineAgentsWithoutDevices();
        if (CollectionUtils.isEmpty(onlineAgents)) {
            return Response.fail("暂无在线的agent，无法执行aapt dump");
        }

        Optional<AgentVo> agentVo = onlineAgents.stream().filter(AgentVo::getIsConfigAapt).findAny();
        if (!agentVo.isPresent()) {
            return Response.fail("暂无配置了aapt的agent，无法执行aapt dump");
        }

        Response agentResponse = agentApi.aaptDumpBadging(agentVo.get().getIp(), agentVo.get().getPort(), app.getDownloadUrl());
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
        int updateRow = appMapper.updateByPrimaryKey(app);
        return updateRow == 1 ? Response.success("更新成功") : Response.fail("更新失败，请稍后重试");
    }
}
