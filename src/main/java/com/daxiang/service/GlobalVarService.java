package com.daxiang.service;

import com.daxiang.dao.GlobalVarDao;
import com.daxiang.mbg.po.GlobalVarExample;
import com.daxiang.mbg.po.User;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.mapper.GlobalVarMapper;
import com.daxiang.mbg.po.GlobalVar;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.vo.GlobalVarVo;
import com.daxiang.model.Page;
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

    public Response add(GlobalVar globalVar) {
        globalVar.setCreateTime(new Date());
        globalVar.setCreatorUid(SecurityUtil.getCurrentUserId());

        int insertRow;
        try {
            insertRow = globalVarMapper.insertSelective(globalVar);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加GlobalVar成功") : Response.fail("添加GlobalVar失败，请稍后重试");
    }

    public Response addBatch(List<GlobalVar> globalVars) {
        Integer currentUserId = SecurityUtil.getCurrentUserId();
        Date now = new Date();

        globalVars.forEach(globalVar -> {
            globalVar.setCreateTime(now);
            globalVar.setCreatorUid(currentUserId);
        });

        int insertRow;
        try {
            insertRow = globalVarDao.insertBatch(globalVars);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == globalVars.size() ? Response.success("添加成功") : Response.fail("添加失败，请稍后重试");
    }

    public Response delete(Integer globalVarId) {
        if (globalVarId == null) {
            return Response.fail("globalVarId不能为空");
        }

        // todo 检查全局变量是否被action使用
        int deleteRow = globalVarMapper.deleteByPrimaryKey(globalVarId);
        return deleteRow == 1 ? Response.success("删除成功") : Response.fail("删除失败,请稍后重试");
    }

    public Response update(GlobalVar globalVar) {
        // todo 检查全局变量是否被action使用

        int updateRow;
        try {
            updateRow = globalVarMapper.updateByPrimaryKeyWithBLOBs(globalVar);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return updateRow == 1 ? Response.success("更新GlobalVar成功") : Response.fail("更新GlobalVar失败,请稍后重试");
    }

    public Response list(GlobalVar globalVar, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<GlobalVar> globalVars = selectByGlobalVar(globalVar);
        List<GlobalVarVo> globalVarVos = convertGlobalVarsToGlobalVarVos(globalVars);

        if (needPaging) {
            long total = Page.getTotal(globalVars);
            return Response.success(Page.build(globalVarVos, total));
        } else {
            return Response.success(globalVarVos);
        }
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

        return globalVars.stream().map(globalVar -> {
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
    }

    private List<GlobalVar> selectByGlobalVar(GlobalVar globalVar) {
        GlobalVarExample example = new GlobalVarExample();
        GlobalVarExample.Criteria criteria = example.createCriteria();

        if (globalVar != null) {
            if (globalVar.getId() != null) {
                criteria.andIdEqualTo(globalVar.getId());
            }
            if (globalVar.getType() != null) {
                criteria.andTypeEqualTo(globalVar.getType());
            }
            if (globalVar.getProjectId() != null) {
                criteria.andProjectIdEqualTo(globalVar.getProjectId());
            }
            if (globalVar.getCategoryId() != null) {
                criteria.andCategoryIdEqualTo(globalVar.getCategoryId());
            }
            if (!StringUtils.isEmpty(globalVar.getName())) {
                criteria.andNameEqualTo(globalVar.getName());
            }
        }
        example.setOrderByClause("create_time desc");

        return globalVarMapper.selectByExampleWithBLOBs(example);
    }

    public List<GlobalVar> getGlobalVarsByEnvironmentId(Integer envId) {
        if (envId == null) {
            return new ArrayList<>();
        }

        return globalVarDao.selectByEnvironmentId(envId);
    }

    public List<GlobalVar> getGlobalVarsByProjectIdAndEnv(Integer projectId, Integer env) {
        if (projectId == null || env == null) {
            return new ArrayList<>();
        }

        GlobalVar query = new GlobalVar();
        query.setProjectId(projectId);
        List<GlobalVar> globalVars = selectByGlobalVar(query);

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
