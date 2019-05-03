package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.dao.TestPlanDao;
import com.yqhp.mbg.mapper.ActionMapper;
import com.yqhp.mbg.mapper.TestPlanMapper;
import com.yqhp.mbg.mapper.TestSuiteMapper;
import com.yqhp.mbg.po.Action;
import com.yqhp.mbg.po.TestPlan;
import com.yqhp.mbg.po.TestSuite;
import com.yqhp.mbg.po.TestSuiteExample;
import com.yqhp.model.Page;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.model.testplan.Before;
import com.yqhp.model.vo.TestPlanDetailInfo;
import com.yqhp.model.vo.TestPlanVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class TestPlanService extends BaseService {

    @Autowired
    private TestPlanMapper testPlanMapper;
    @Autowired
    private TestPlanDao testPlanDao;
    @Autowired
    private TestSuiteMapper testSuiteMapper;
    @Autowired
    private ActionService actionService;
    @Autowired
    private ActionMapper actionMapper;

    public Response add(TestPlan testPlan) {
        testPlan.setCreateTime(new Date());
        testPlan.setCreatorUid(getUid());

        try {
            int insertRow = testPlanMapper.insertSelective(testPlan);
            if (insertRow != 1) {
                return Response.fail("添加测试计划失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("重复命名");
        }

        return Response.success("添加测试计划成功");
    }

    public Response delete(Integer testPlanId) {
        if (testPlanId == null) {
            return Response.fail("测试计划id不能为空");
        }

        int deleteRow = testPlanMapper.deleteByPrimaryKey(testPlanId);

        if (deleteRow != 1) {
            return Response.fail("删除测试计划失败，请稍后重试");
        } else {
            return Response.success("删除成功");
        }
    }

    public Response update(TestPlan testPlan) {
        if (testPlan.getId() == null) {
            return Response.fail("测试计划id不能为空");
        }

        try {
            int updateRow = testPlanMapper.updateByPrimaryKeySelective(testPlan);
            if (updateRow != 1) {
                return Response.fail("更新测试计划失败");
            }
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }

        return Response.success("更新测试计划成功");
    }

    public Response list(TestPlan testPlan, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();

        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<TestPlanVo> testPlanVos = testPlanDao.selectByTestPlan(testPlan);

        if (needPaging) {
            return Response.success(Page.convert(new PageInfo(testPlanVos)));
        } else {
            return Response.success(testPlanVos);
        }
    }

    /**
     * 获取测试计划包含的测试用例
     * @return actionId
     */
    public List<Action> getTestcasesByTestPlan(TestPlan testPlan) {
        TestSuiteExample testSuiteExample = new TestSuiteExample();
        testSuiteExample.createCriteria().andIdIn(testPlan.getTestSuites());
        List<TestSuite> testSuites = testSuiteMapper.selectByExampleWithBLOBs(testSuiteExample);
        List<Integer> testcaseIds = testSuites.stream().flatMap(testSuite -> testSuite.getTestcases().stream()).collect(Collectors.toList());
        return actionService.findByIds(testcaseIds);
    }

    public Response getDetailInfo(Integer testPlanId) {
        if(testPlanId == null) {
            return Response.fail("测试计划不能为空");
        }

        TestPlan testPlan = testPlanMapper.selectByPrimaryKey(testPlanId);
        if(testPlan == null) {
            return Response.fail("测试计划不存在");
        }

        TestPlanDetailInfo testPlanDetailInfo = new TestPlanDetailInfo();
        BeanUtils.copyProperties(testPlan,testPlanDetailInfo);

        testPlan.getBefores().forEach(before -> {
            Action action = actionMapper.selectByPrimaryKey(before.getActionId());
            if(before.getType() == Before.BEFORE_METHOD_TYPE) {
                testPlanDetailInfo.setBeforeMethodName(action.getName());
            }else if(before.getType() == Before.BEFORE_SUITE_TYPE) {
                testPlanDetailInfo.setBeforeSuiteName(action.getName());
            }
        });

        testPlanDetailInfo.setTestcases(getTestcasesByTestPlan(testPlan));

        return Response.success(testPlanDetailInfo);
    }
}
