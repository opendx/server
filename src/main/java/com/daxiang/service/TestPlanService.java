package com.daxiang.service;

import com.daxiang.mbg.po.TestPlan;
import com.daxiang.mbg.po.TestPlanExample;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.dao.TestPlanDao;
import com.daxiang.model.UserCache;
import com.daxiang.model.vo.TestPlanVo;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.mapper.TestPlanMapper;
import com.daxiang.model.Response;
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
    private TestPlanDao testPlanDao;

    public Response add(TestPlan testPlan) {
        testPlan.setCreateTime(new Date());
        testPlan.setCreatorUid(getUid());

        int insertRow;
        try {
            insertRow = testPlanMapper.insertSelective(testPlan);
        } catch (DuplicateKeyException e) {
            return Response.fail("重复命名");
        }
        return insertRow == 1 ? Response.success("添加TestPlan成功") : Response.fail("添加TestPlan败，请稍后重试");
    }

    public Response delete(Integer testPlanId) {
        if (testPlanId == null) {
            return Response.fail("测试计划id不能为空");
        }

        int deleteRow = testPlanMapper.deleteByPrimaryKey(testPlanId);
        return deleteRow == 1 ? Response.success("删除TestPlan成功") : Response.fail("删除TestPlan失败，请稍后重试");
    }

    public Response update(TestPlan testPlan) {
        if (testPlan.getId() == null) {
            return Response.fail("测试计划id不能为空");
        }

        int updateRow;
        try {
            updateRow = testPlanMapper.updateByPrimaryKeyWithBLOBs(testPlan);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return updateRow == 1 ? Response.success("更新TestPlan成功") : Response.fail("更新TestPlan失败，请稍后重试");
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
            return Response.success(Page.build(testPlanVos, total));
        } else {
            return Response.success(testPlanVos);
        }
    }

    public List<TestPlan> selectByTestPlan(TestPlan testPlan) {
        if (testPlan == null) {
            testPlan = new TestPlan();
        }

        TestPlanExample testPlanExample = new TestPlanExample();
        TestPlanExample.Criteria criteria = testPlanExample.createCriteria();

        if (testPlan.getId() != null) {
            criteria.andIdEqualTo(testPlan.getId());
        }
        if (testPlan.getProjectId() != null) {
            criteria.andProjectIdEqualTo(testPlan.getProjectId());
        }
        if (!StringUtils.isEmpty(testPlan.getName())) {
            criteria.andNameEqualTo(testPlan.getName());
        }
        if (testPlan.getRunMode() != null) {
            criteria.andRunModeEqualTo(testPlan.getRunMode());
        }
        testPlanExample.setOrderByClause("create_time desc");

        return testPlanMapper.selectByExampleWithBLOBs(testPlanExample);
    }

    public TestPlan selectByPrimaryKey(Integer testPlanId) {
        return testPlanMapper.selectByPrimaryKey(testPlanId);
    }

    public List<TestPlan> selectByTestSuiteId(Integer testSuiteId) {
        return testPlanDao.selectByTestSuiteId(testSuiteId);
    }
}