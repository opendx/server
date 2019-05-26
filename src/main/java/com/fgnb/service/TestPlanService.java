package com.fgnb.service;

import com.fgnb.mbg.po.*;
import com.fgnb.model.UserCache;
import com.fgnb.model.vo.TestPlanVo;
import com.github.pagehelper.PageHelper;
import com.fgnb.mbg.mapper.TestPlanMapper;
import com.fgnb.model.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
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
            int updateRow = testPlanMapper.updateByPrimaryKeyWithBLOBs(testPlan);
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
        return testPlanMapper.selectByExampleWithBLOBs(testPlanExample);
    }

    public TestPlan selectByPrimaryKey(Integer testPlanId) {
        return testPlanMapper.selectByPrimaryKey(testPlanId);
    }
}