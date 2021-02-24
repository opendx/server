package com.daxiang.service;

import com.daxiang.agent.AgentClient;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.AppMapper;
import com.daxiang.mbg.po.App;
import com.daxiang.mbg.po.AppExample;
import com.daxiang.mbg.po.User;
import com.daxiang.model.*;
import com.daxiang.model.enums.UploadDir;
import com.daxiang.model.vo.AgentVo;
import com.daxiang.model.vo.AppVo;
import com.daxiang.security.SecurityUtil;
import com.daxiang.utils.HttpServletUtil;
import com.github.pagehelper.Page;
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
    private AppMapper appMapper;
    @Autowired
    private AgentService agentService;
    @Autowired
    private AgentClient agentClient;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    public void upload(App app, MultipartFile file) {
        UploadFile uploadFile = fileService.upload(file, UploadDir.APP);

        app.setFilePath(uploadFile.getFilePath());
        app.setUploadTime(new Date());
        app.setUploadorUid(SecurityUtil.getCurrentUserId());

        int insertCount = appMapper.insertSelective(app);
        if (insertCount != 1) {
            throw new ServerException("上传失败");
        }
    }

    public void deleteAndClearRelatedRes(Integer appId) {
        if (appId == null) {
            throw new ServerException("appId不能为空");
        }

        App app = appMapper.selectByPrimaryKey(appId);
        if (app == null) {
            throw new ServerException("app不存在");
        }

        int deleteCount = appMapper.deleteByPrimaryKey(appId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后再试");
        }

        fileService.deleteQuietly(app.getFilePath());
    }

    public void update(App app) {
        int updateCount = appMapper.updateByPrimaryKey(app);
        if (updateCount != 1) {
            throw new ServerException("更新失败，请稍后重试");
        }
    }

    public PagedData<AppVo> list(App query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "upload_time desc";
        }

        List<AppVo> appVos = getAppVos(query, orderBy);
        return new PagedData<>(appVos, page.getTotal());
    }

    private List<AppVo> convertAppsToAppVos(List<App> apps) {
        if (CollectionUtils.isEmpty(apps)) {
            return new ArrayList<>();
        }

        List<Integer> uploadorUids = apps.stream()
                .map(App::getUploadorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(uploadorUids);

        List<AppVo> appVos = apps.stream().map(app -> {
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

        return appVos;
    }

    public List<AppVo> getAppVos(App query, String orderBy) {
        List<App> apps = getApps(query, orderBy);
        return convertAppsToAppVos(apps);
    }

    public List<App> getApps(App query, String orderBy) {
        AppExample example = new AppExample();

        if (query != null) {
            AppExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (query.getPlatform() != null) {
                criteria.andPlatformEqualTo(query.getPlatform());
            }
            if (query.getProjectId() != null) {
                criteria.andProjectIdEqualTo(query.getProjectId());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return appMapper.selectByExample(example);
    }

    public void aaptDumpBadging(Integer appId) {
        if (appId == null) {
            throw new ServerException("appId不能为空");
        }

        App app = appMapper.selectByPrimaryKey(appId);
        if (app == null) {
            throw new ServerException("app不存在");
        }
        if (app.getPlatform() != Platform.ANDROID) {
            throw new ServerException("只有Android平台才能执行aapt dump");
        }

        List<AgentVo> onlineAgents = agentService.getOnlineAgentsWithoutDevices();
        if (CollectionUtils.isEmpty(onlineAgents)) {
            throw new ServerException("暂无在线的agent，无法执行aapt dump");
        }

        Optional<AgentVo> agentVo = onlineAgents.stream().filter(AgentVo::getIsConfigAapt).findAny();
        if (!agentVo.isPresent()) {
            throw new ServerException("暂无配置了aapt的agent，无法执行aapt dump");
        }

        AgentVo agent = agentVo.get();
        Response agentResponse = agentClient.aaptDumpBadging(agent.getIp(), agent.getPort(),
                HttpServletUtil.getStaticResourceUrl(app.getFilePath()));
        if (!agentResponse.isSuccess()) {
            throw new ServerException(agentResponse.getMsg());
        }

        String dumpInfo = (String) agentResponse.getData();
        if (StringUtils.isEmpty(dumpInfo)) {
            throw new ServerException("aapt dump信息为空");
        }

        log.info("app: {}, dumpInfo: {}", app.getName(), dumpInfo);

        String version = org.apache.commons.lang3.StringUtils.substringBetween(dumpInfo, "versionName='", "'");
        String packageName = org.apache.commons.lang3.StringUtils.substringBetween(dumpInfo, "package: name='", "'");
        String launchActivity = org.apache.commons.lang3.StringUtils.substringBetween(dumpInfo, "launchable-activity: name='", "'");

        app.setVersion(version);
        app.setPackageName(packageName);
        app.setLaunchActivity(launchActivity);

        int updateCount = appMapper.updateByPrimaryKeySelective(app);
        if (updateCount != 1) {
            throw new ServerException("更新失败，请稍后重试");
        }
    }

}
