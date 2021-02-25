package com.daxiang.service;

import com.daxiang.agent.AgentClient;
import com.daxiang.dao.AgentExtJarDao;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.AgentExtJarMapper;
import com.daxiang.mbg.po.AgentExtJar;
import com.daxiang.mbg.po.AgentExtJarExample;
import com.daxiang.mbg.po.User;
import com.daxiang.model.*;
import com.daxiang.model.enums.UploadDir;
import com.daxiang.model.vo.AgentExtJarVo;
import com.daxiang.model.vo.AgentVo;
import com.daxiang.security.SecurityUtil;
import com.daxiang.utils.HttpServletUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Service
public class AgentExtJarService {

    @Autowired
    private AgentExtJarMapper agentExtJarMapper;
    @Autowired
    private AgentExtJarDao agentExtJarDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private AgentClient agentClient;

    @Transactional
    public void upload(MultipartFile file) {
        if (file == null) {
            throw new ServerException("file不能为空");
        }

        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            throw new ServerException("文件名不能为空");
        }

        // eg.spring-boot-2.1.4.RELEASE.jar
        Matcher matcher = Pattern.compile("(.+)-([0-9].*)\\.jar").matcher(filename);
        String jarName = null;
        String jarVersion = null;
        while (matcher.find()) {
            jarName = matcher.group(1); // spring-boot
            jarVersion = matcher.group(2); // 2.1.4.RELEASE
        }

        if (StringUtils.isEmpty(jarName) || StringUtils.isEmpty(jarVersion)) {
            throw new ServerException(filename + "格式错误，正确格式示例: commons-io-2.6.jar");
        }

        if (fileService.exist(UploadDir.AGENT_EXT_JAR, filename)) {
            throw new ServerException(filename + "已存在");
        }

        AgentExtJar agentExtJar = new AgentExtJar();
        agentExtJar.setName(jarName);
        agentExtJar.setVersion(jarVersion);
        agentExtJar.setUploadorUid(SecurityUtil.getCurrentUserId());
        agentExtJar.setUploadTime(new Date());

        UploadFile uploadFile = fileService.upload(file, UploadDir.AGENT_EXT_JAR, false);

        agentExtJar.setFilePath(uploadFile.getFilePath());
        agentExtJar.setFileSize(uploadFile.getFile().length());

        try {
            String md5 = DigestUtils.md5DigestAsHex(FileUtils.readFileToByteArray(uploadFile.getFile()));
            agentExtJar.setMd5(md5);
        } catch (IOException e) {
            log.error("read file err, file={}", agentExtJar.getFilePath(), e);
            fileService.deleteQuietly(agentExtJar.getFilePath());
            throw new ServerException(e.getMessage());
        }

        int insertRow;
        try {
            insertRow = agentExtJarMapper.insertSelective(agentExtJar);
        } catch (Exception e) {
            fileService.deleteQuietly(agentExtJar.getFilePath());
            if (e instanceof DuplicateKeyException) {
                throw new ServerException(filename + "已存在");
            }
            throw new ServerException(e);
        }

        if (insertRow != 1) {
            fileService.deleteQuietly(agentExtJar.getFilePath());
            throw new ServerException(filename + "添加失败，请稍后重试");
        }

        List<AgentVo> onlineAgents = agentService.getOnlineAgentsWithoutDevices();
        if (CollectionUtils.isEmpty(onlineAgents)) {
            return;
        }

        boolean anyAgentLoadJarSuccess = false;
        String jarUrl = HttpServletUtil.getStaticResourceUrl(agentExtJar.getFilePath());
        // 分发jar到在线的agent
        for (AgentVo onlineAgent : onlineAgents) {
            Response response = agentClient.loadJar(onlineAgent.getIp(), onlineAgent.getPort(), jarUrl);
            if (response.isSuccess()) {
                anyAgentLoadJarSuccess = true;
            } else {
                log.warn("agent({}) load jar({}) fail, response={}", onlineAgent.getIp(), agentExtJar.getFilePath(), response);
            }
        }

        // 只要有1个agent加载成功，就认为这个jar是合法的
        if (!anyAgentLoadJarSuccess) {
            fileService.deleteQuietly(agentExtJar.getFilePath());
            throw new ServerException("agent加载" + filename + "失败，请检查文件是否合法");
        }
    }

    public void delete(Integer id) {
        if (id == null) {
            throw new ServerException("id不能为空");
        }

        AgentExtJar agentExtJar = agentExtJarMapper.selectByPrimaryKey(id);
        if (agentExtJar == null) {
            throw new ServerException("记录不存在");
        }

        int deleteRow = agentExtJarMapper.deleteByPrimaryKey(id);
        if (deleteRow != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }

        fileService.deleteQuietly(agentExtJar.getFilePath());
    }

    public PagedData<AgentExtJarVo> list(AgentExtJar query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "id desc";
        }

        List<AgentExtJarVo> agentExtJarVos = getAgentExtJarVos(query, orderBy);
        return new PagedData<>(agentExtJarVos, page.getTotal());
    }

    public List<AgentExtJarVo> getAgentExtJarVos(AgentExtJar query, String orderBy) {
        List<AgentExtJar> agentExtJars = getAgentExtJars(query, orderBy);
        return convertAgentExtJarsToAgentExtJarVos(agentExtJars);
    }

    public List<AgentExtJar> getAgentExtJars(AgentExtJar query, String orderBy) {
        AgentExtJarExample example = new AgentExtJarExample();

        if (query != null) {
            AgentExtJarExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (!StringUtils.isEmpty(query.getMd5())) {
                criteria.andMd5EqualTo(query.getMd5());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameLike("%" + query.getName() + "%");
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return agentExtJarMapper.selectByExample(example);
    }

    private List<AgentExtJarVo> convertAgentExtJarsToAgentExtJarVos(List<AgentExtJar> agentExtJars) {
        if (CollectionUtils.isEmpty(agentExtJars)) {
            return new ArrayList<>();
        }

        List<Integer> uploadorUids = agentExtJars.stream()
                .map(AgentExtJar::getUploadorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(uploadorUids);

        List<AgentExtJarVo> agentExtJarVos = agentExtJars.stream().map(agentExtJar -> {
            AgentExtJarVo agentExtJarVo = new AgentExtJarVo();
            BeanUtils.copyProperties(agentExtJar, agentExtJarVo);

            User user = userMap.get(agentExtJar.getUploadorUid());
            if (user != null) {
                agentExtJarVo.setUploadorNickName(user.getNickName());
            }

            return agentExtJarVo;
        }).collect(Collectors.toList());

        return agentExtJarVos;
    }

    public List<AgentExtJarVo> getLastUploadTimeList() {
        List<AgentExtJar> agentExtJars = agentExtJarDao.selectLastUploadTimeList();
        return agentExtJars.stream().map(agentExtJar -> {
            AgentExtJarVo agentExtJarVo = new AgentExtJarVo();
            BeanUtils.copyProperties(agentExtJar, agentExtJarVo);
            return agentExtJarVo;
        }).collect(Collectors.toList());
    }
}
