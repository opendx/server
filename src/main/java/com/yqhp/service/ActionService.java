package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.agent.AgentApi;
import com.yqhp.dao.ActionDao;
import com.yqhp.mbg.mapper.ActionMapper;
import com.yqhp.mbg.mapper.GlobalVarMapper;
import com.yqhp.mbg.mapper.ProjectMapper;
import com.yqhp.mbg.po.*;
import com.yqhp.model.Page;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.model.action.Step;
import com.yqhp.model.vo.ActionVo;
import com.yqhp.model.vo.DebuggableAction;
import com.yqhp.testngcode.ActionTreeBuilder;
import com.yqhp.testngcode.TestNGCodeConverter;
import com.yqhp.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class ActionService extends BaseService {

    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private ActionDao actionDao;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private GlobalVarMapper globalVarMapper;
    @Autowired
    private AgentApi agentApi;

    /**
     * 添加action
     *
     * @param action
     * @return
     */
    public Response add(Action action) {
        if (action.getType() == Action.TYPE_TESTCASE) {
            //测试用例 必须要有分类
            if (action.getCategoryId() == null) {
                return Response.fail("测试用例分类不能为空");
            }
        }

        action.setCreatorUid(getUid());
        action.setCreateTime(new Date());

        if (!StringUtils.isEmpty(action.getReturnValue())) {
            // 有返回值
            action.setHasReturnValue(Action.HAS_RETURN_VALUE);
        }

        try {
            int insertRow = actionMapper.insertSelective(action);
            if (insertRow != 1) {
                return Response.fail("添加失败，稍后刷新重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success(action);
    }

    /**
     * 删除action
     *
     * @param actionId
     * @return
     */
    public Response delete(Integer actionId) {
        if (actionId == null) {
            return Response.fail("actionId不能为空");
        }
        //todo 查询这个action是否被其他step引用。如果有其他step引用，提示不能删除
        int deleteRow = actionMapper.deleteByPrimaryKey(actionId);
        if (deleteRow != 1) {
            return Response.fail("删除失败，请稍后重试");
        }
        return Response.success("删除成功");
    }

    /**
     * 更新action
     *
     * @param action
     * @return
     */
    public Response update(Action action) {
        if (action.getId() == null) {
            return Response.fail("actionId不能为空");
        }

        action.setUpdateTime(new Date());
        action.setUpdatorUid(getUid());

        //是否有返回值
        if (!StringUtils.isEmpty(action.getReturnValue())) {
            action.setHasReturnValue(Action.HAS_RETURN_VALUE);
        } else {
            action.setHasReturnValue(null);
        }

        //由于action的参数，可能被其他步骤使用，类似于方法的参数改动，其他调用该方法的地方都要改动
        //为了安全起见，不更新ActionParam。设置null，插入的时候不会update这个值
        action.setParams(null);

        try {
            int updateRow = actionMapper.updateByPrimaryKeySelective(action);
            if (updateRow != 1) {
                return Response.fail("更新失败，请稍后重试!");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("更新成功");
    }

    /**
     * 查询action
     *
     * @param action
     * @param pageRequest
     * @return
     */
    public Response list(Action action, PageRequest pageRequest) {
        boolean needPageing = pageRequest.needPaging();
        //需要分页
        if (needPageing) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }
        List<ActionVo> actionVos = actionDao.selectByAction(action);
        if (needPageing) {
            return Response.success(Page.convert(new PageInfo(actionVos)));
        }
        return Response.success(actionVos);
    }

    /**
     * action步骤选择action，可供选择的action
     *
     * @param projectId
     * @return
     */
    public Response findSelectableActions(Integer projectId) {
        if (projectId == null) {
            return Response.fail("projectId不能为空");
        }
        Project project = projectMapper.selectByPrimaryKey(projectId);
        if (project == null) {
            return Response.fail("项目不存在");
        }
        ActionExample actionExample = new ActionExample();

        //同一个项目下自定义action
        ActionExample.Criteria criteria1 = actionExample.createCriteria();
        criteria1.andProjectIdEqualTo(projectId).andTypeEqualTo(Action.TYPE_CUSTOM);

        //所有项目公用的基础action
        ActionExample.Criteria criteria2 = actionExample.createCriteria();
        criteria2.andProjectIdIsNull().andTypeEqualTo(Action.TYPE_BASE).andProjectTypeIsNull();

        //同一项目类型的基础action
        ActionExample.Criteria criteria3 = actionExample.createCriteria();
        criteria3.andProjectIdIsNull().andTypeEqualTo(Action.TYPE_BASE).andProjectTypeEqualTo(project.getType());

        actionExample.or(criteria2);
        actionExample.or(criteria3);
        actionExample.setOrderByClause("create_time desc");

        return Response.success(actionMapper.selectByExampleWithBLOBs(actionExample));
    }

    /**
     * 调试action
     *
     * @param debuggableAction
     * @return
     */
    public Response debug(DebuggableAction debuggableAction) {
        Action action = debuggableAction.getAction();
        DebuggableAction.DebugInfo debugInfo = debuggableAction.getDebugInfo();

        //没保存过的action设置个默认的actionId
        if (action.getId() == null) {
            action.setId(0);
        }

        //构建action树
        new ActionTreeBuilder(actionMapper).build(Arrays.asList(action));

        String testClassName = "DebugClass_" + UUIDUtil.getUUID();

        //该项目下的全局变量
        GlobalVarExample globalVarExample = new GlobalVarExample();
        globalVarExample.createCriteria().andProjectIdEqualTo(action.getProjectId());
        List<GlobalVar> globalVars = globalVarMapper.selectByExample(globalVarExample);

        String testNGCode;
        try {
            testNGCode = new TestNGCodeConverter()
                    .setActionTree(action)
                    .setTestClassName(testClassName)
                    .setBasePackagePath("/codetemplate")
                    .setFtlFileName("testngCode.ftl")
                    .setIsBeforeSuite(false)
                    .setProjectType(projectMapper.selectByPrimaryKey(action.getProjectId()).getType())
                    .setDeviceId(debugInfo.getDeviceId())
                    .setPort(debugInfo.getPort())
                    .setGlobalVars(globalVars)
                    .convert();
            if (StringUtils.isEmpty(testNGCode)) {
                return Response.fail("转换testng代码失败");
            }
        } catch (Exception e) {
            log.error("转换testng代码出错", e);
            return Response.fail("转换testng代码出错：" + e.getMessage());
        }

        //发送到agent执行
        Response response = agentApi.debugAction(debugInfo.getAgentIp(), debugInfo.getAgentPort(), testClassName, testNGCode);
        if (!response.isSuccess()) {
            return Response.fail(response.getMsg());
        }
        return Response.success(response.getMsg());
    }

}
