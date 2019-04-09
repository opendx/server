package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.dao.TestPlanDao;
import com.yqhp.mbg.mapper.TestPlanMapper;
import com.yqhp.mbg.po.TestPlan;
import com.yqhp.model.Page;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.model.vo.TestPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
}
