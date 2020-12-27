package com.daxiang.service;

import com.daxiang.dao.GlobalVarDao;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.po.GlobalVarExample;
import com.daxiang.mbg.po.User;
import com.daxiang.model.PagedData;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.daxiang.mbg.mapper.GlobalVarMapper;
import com.daxiang.mbg.po.GlobalVar;
import com.daxiang.model.PageRequest;
import com.daxiang.model.vo.GlobalVarVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class GlobalVarService {

    @Autowired
    private GlobalVarMapper globalVarMapper;
    @Autowired
    private GlobalVarDao globalVarDao;
    @Autowired
    private UserService userService;
    @Autowired
    private EnvironmentService environmentService;

    public void add(GlobalVar globalVar) {
        globalVar.setCreateTime(new Date());
        globalVar.setCreatorUid(SecurityUtil.getCurrentUserId());

        try {
            int insertCount = globalVarMapper.insertSelective(globalVar);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(globalVar.getName() + "已存在");
        }
    }

    public void addBatch(List<GlobalVar> globalVars) {
        Integer currentUserId = SecurityUtil.getCurrentUserId();
        Date now = new Date();

        globalVars.forEach(globalVar -> {
            globalVar.setCreateTime(now);
            globalVar.setCreatorUid(currentUserId);
        });

        try {
            int insertCount = globalVarDao.insertBatch(globalVars);
            if (insertCount != globalVars.size()) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException("命名冲突");
        }
    }

    public void delete(Integer globalVarId) {
        if (globalVarId == null) {
            throw new ServerException("globalVarId不能为空");
        }

        // todo 检查全局变量是否被action使用
        int deleteCount = globalVarMapper.deleteByPrimaryKey(globalVarId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }
    }

    public void update(GlobalVar globalVar) {
        // todo 检查全局变量是否被action使用
        try {
            int updateCount = globalVarMapper.updateByPrimaryKeyWithBLOBs(globalVar);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(globalVar.getName() + "已存在");
        }
    }

    public PagedData<GlobalVarVo> list(GlobalVar query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<GlobalVarVo> globalVarVos = getGlobalVarVos(query, orderBy);
        return new PagedData<>(globalVarVos, page.getTotal());
    }

    private List<GlobalVarVo> convertGlobalVarsToGlobalVarVos(List<GlobalVar> globalVars) {
        if (CollectionUtils.isEmpty(globalVars)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = globalVars.stream()
                .map(GlobalVar::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

        List<GlobalVarVo> globalVarVos = globalVars.stream().map(globalVar -> {
            GlobalVarVo globalVarVo = new GlobalVarVo();
            BeanUtils.copyProperties(globalVar, globalVarVo);

            if (globalVar.getCreatorUid() != null) {
                User user = userMap.get(globalVar.getCreatorUid());
                if (user != null) {
                    globalVarVo.setCreatorNickName(user.getNickName());
                }
            }

            return globalVarVo;
        }).collect(Collectors.toList());

        return globalVarVos;
    }

    public List<GlobalVarVo> getGlobalVarVos(GlobalVar query, String orderBy) {
        List<GlobalVar> globalVars = getGlobalVars(query, orderBy);
        return convertGlobalVarsToGlobalVarVos(globalVars);
    }

    public List<GlobalVar> getGlobalVars(GlobalVar query) {
        return getGlobalVars(query, null);
    }

    public List<GlobalVar> getGlobalVars(GlobalVar query, String orderBy) {
        GlobalVarExample example = new GlobalVarExample();

        if (query != null) {
            GlobalVarExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (query.getType() != null) {
                criteria.andTypeEqualTo(query.getType());
            }
            if (query.getProjectId() != null) {
                criteria.andProjectIdEqualTo(query.getProjectId());
            }
            if (query.getCategoryId() != null) {
                criteria.andCategoryIdEqualTo(query.getCategoryId());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameEqualTo(query.getName());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return globalVarMapper.selectByExampleWithBLOBs(example);
    }

    public List<GlobalVar> getGlobalVarsByEnvironmentId(Integer envId) {
        if (envId == null) {
            throw new ServerException("envId不能为空");
        }

        return globalVarDao.selectByEnvironmentId(envId);
    }

    public List<GlobalVar> getGlobalVarsByProjectIdAndEnv(Integer projectId, Integer env) {
        if (projectId == null || env == null) {
            throw new ServerException("projectId or env不能为空");
        }

        GlobalVar query = new GlobalVar();
        query.setProjectId(projectId);
        List<GlobalVar> globalVars = getGlobalVars(query);

        globalVars.forEach(globalVar -> {
            String value = environmentService.getValueInEnvironmentValues(globalVar.getEnvironmentValues(), env);
            globalVar.setValue(value);
        });

        return globalVars;
    }

    public List<GlobalVar> getGlobalVarsByCategoryIds(List<Integer> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return new ArrayList<>();
        }

        GlobalVarExample example = new GlobalVarExample();
        GlobalVarExample.Criteria criteria = example.createCriteria();

        criteria.andCategoryIdIn(categoryIds);
        return globalVarMapper.selectByExample(example);
    }
}
