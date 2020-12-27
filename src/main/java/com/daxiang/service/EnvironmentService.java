package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.EnvironmentMapper;
import com.daxiang.mbg.po.*;
import com.daxiang.model.PagedData;
import com.daxiang.model.PageRequest;
import com.daxiang.model.environment.EnvironmentValue;
import com.daxiang.model.vo.EnvironmentVo;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
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
public class EnvironmentService {

    @Autowired
    private EnvironmentMapper environmentMapper;
    @Autowired
    private ActionService actionService;
    @Autowired
    private GlobalVarService globalVarService;
    @Autowired
    private TestPlanService testPlanService;
    @Autowired
    private UserService userService;

    public void add(Environment environment) {
        environment.setCreateTime(new Date());
        environment.setCreatorUid(SecurityUtil.getCurrentUserId());

        try {
            int insertCount = environmentMapper.insertSelective(environment);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(environment.getName() + "已存在");
        }
    }

    public void delete(Integer environmentId) {
        check(environmentId);

        int deleteCount = environmentMapper.deleteByPrimaryKey(environmentId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }
    }

    public void update(Environment environment) {
        try {
            int updateCount = environmentMapper.updateByPrimaryKeySelective(environment);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(environment.getName() + "已存在");
        }
    }

    public PagedData<EnvironmentVo> list(Environment query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<EnvironmentVo> environmentVos = getEnvironmentVos(query, orderBy);
        return new PagedData<>(environmentVos, page.getTotal());
    }

    private List<EnvironmentVo> convertEnvironmentsToEnvironmentVos(List<Environment> environments) {
        if (CollectionUtils.isEmpty(environments)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = environments.stream()
                .map(Environment::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

        List<EnvironmentVo> environmentVos = environments.stream().map(env -> {
            EnvironmentVo environmentVo = new EnvironmentVo();
            BeanUtils.copyProperties(env, environmentVo);

            if (env.getCreatorUid() != null) {
                User user = userMap.get(env.getCreatorUid());
                if (user != null) {
                    environmentVo.setCreatorNickName(user.getNickName());
                }
            }

            return environmentVo;
        }).collect(Collectors.toList());

        return environmentVos;
    }

    public List<EnvironmentVo> getEnvironmentVos(Environment query, String orderBy) {
        List<Environment> environments = getEnvironments(query, orderBy);
        return convertEnvironmentsToEnvironmentVos(environments);
    }

    public List<Environment> getEnvironments(Environment query, String orderBy) {
        EnvironmentExample example = new EnvironmentExample();

        if (query != null) {
            EnvironmentExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameEqualTo(query.getName());
            }
            if (query.getProjectId() != null) {
                criteria.andProjectIdEqualTo(query.getProjectId());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return environmentMapper.selectByExample(example);
    }

    /**
     * 检查env没有被action.localVar globalVar testplan使用
     *
     * @param envId
     */
    private void check(Integer envId) {
        // 检查env是否被action localVars使用
        List<Action> actions = actionService.getActionsByLocalVarsEnvironmentId(envId);
        if (!CollectionUtils.isEmpty(actions)) {
            String actionNames = actions.stream().map(Action::getName).collect(Collectors.joining("、"));
            throw new ServerException("actions: " + actionNames + ", 正在使用此环境");
        }

        // 检查env是否被globalVar使用
        List<GlobalVar> globalVars = globalVarService.getGlobalVarsByEnvironmentId(envId);
        if (!CollectionUtils.isEmpty(globalVars)) {
            String globalVarNames = globalVars.stream().map(GlobalVar::getName).collect(Collectors.joining("、"));
            throw new ServerException("globalVars: " + globalVarNames + ", 正在使用此环境");
        }

        // 检查env是否被testplan使用
        List<TestPlan> testPlans = testPlanService.getTestPlansByEnvironmentId(envId);
        if (!CollectionUtils.isEmpty(testPlans)) {
            String testPlanNames = testPlans.stream().map(TestPlan::getName).collect(Collectors.joining("、"));
            throw new ServerException("testPlans: " + testPlanNames + ", 正在使用此环境");
        }
    }

    /**
     * 在environmentValues中找到与envId匹配的value
     */
    public String getValueInEnvironmentValues(List<EnvironmentValue> environmentValues, Integer envId) {
        String defaultValue = null;

        for (EnvironmentValue env : environmentValues) {
            Integer environmentId = env.getEnvironmentId();
            if (envId.equals(environmentId)) {
                return env.getValue();
            } else if (environmentId == EnvironmentValue.DEFAULT_ENVIRONMENT_ID) {
                defaultValue = env.getValue();
            }
        }

        return defaultValue;
    }

    public String getEnvironmentNameById(Integer envId) {
        if (envId == EnvironmentValue.DEFAULT_ENVIRONMENT_ID) {
            return "默认";
        }

        Environment environment = environmentMapper.selectByPrimaryKey(envId);
        if (environment != null) {
            return environment.getName();
        }

        return "";
    }
}
