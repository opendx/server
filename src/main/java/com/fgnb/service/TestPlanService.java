package com.fgnb.service;

import com.fgnb.mbg.po.*;
import com.fgnb.model.UserCache;
import com.fgnb.model.vo.TestPlanVo;
import com.github.pagehelper.PageHelper;
import com.fgnb.mbg.mapper.TestPlanMapper;
import com.fgnb.mbg.mapper.TestSuiteMapper;
import com.fgnb.model.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.testplan.Before;
import com.fgnb.model.vo.TestPlanDetailInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    private TestSuiteMapper testSuiteMapper;
    @Autowired
    private ActionService actionService;

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

        List<TestPlan> testPlans = selectByTestPlan(testPlan);
        List<TestPlanVo> testPlanVos = testPlans.stream().map(p -> TestPlanVo.convert(p, UserCache.getNickNameById(p.getCreatorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用testPlans计算total
            long total = Page.getTotal(testPlans);
            return Response.success(Page.build(testPlanVos,total));
        } else {
            return Response.success(testPlanVos);
        }
    }

    public List<TestPlan> selectByTestPlan(TestPlan testPlan) {
        if(testPlan == null) {
            testPlan = new TestPlan();
        }

        TestPlanExample testPlanExample = new TestPlanExample();
        TestPlanExample.Criteria criteria = testPlanExample.createCriteria();

        if(testPlan.getId() != null) {
            criteria.andIdEqualTo(testPlan.getId());
        }
        if(testPlan.getProjectId() != null) {
            criteria.andProjectIdEqualTo(testPlan.getProjectId());
        }
        if(!StringUtils.isEmpty(testPlan.getName())) {
            criteria.andNameEqualTo(testPlan.getName());
        }

        testPlanExample.setOrderByClause("create_time desc");
        return testPlanMapper.selectByExample(testPlanExample);
    }

    /**
     * 获取测试计划包含的测试用例
     * @return actionId
     */
    public List<Action> getTestcasesByTestPlan(TestPlan testPlan) {
        TestSuiteExample testSuiteExample = new TestSuiteExample();
        testSuiteExample.createCriteria().andIdIn(testPlan.getTestSuites());
        // todo reflact
//        List<TestSuite> testSuites = testSuiteMapper.selectByExampleWithBLOBs(testSuiteExample);
//        List<Integer> testcaseIds = testSuites.stream().flatMap(testSuite -> testSuite.getTestcases().stream()).collect(Collectors.toList());
//        return actionService.findByIds(testcaseIds);
        return null;
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
            Action action = actionService.selectByPrimaryKey(before.getActionId());
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
