package com.daxiang.service;

import com.daxiang.mbg.po.TestPlan;
import com.daxiang.mbg.po.TestSuite;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.mbg.po.Action;
import com.daxiang.mbg.po.TestSuiteExample;
import com.daxiang.model.UserCache;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.mapper.TestSuiteMapper;
import com.daxiang.model.Page;
import com.daxiang.model.vo.TestSuiteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class TestSuiteService extends BaseService {

    @Autowired
    private TestSuiteMapper testSuiteMapper;
    @Autowired
    private ActionService actionService;
    @Autowired
    private TestPlanService testPlanService;

    public Response add(TestSuite testSuite) {
        testSuite.setCreateTime(new Date());
        testSuite.setCreatorUid(getUid());

        int insertRow;
        try {
            insertRow = testSuiteMapper.insertSelective(testSuite);
        } catch (DuplicateKeyException e) {
            return Response.fail("命名冲突");
        }
        return insertRow == 1 ? Response.success("添加TestSuite成功") : Response.fail("添加TestSuite失败");
    }

    public Response delete(Integer testSuiteId) {
        if (testSuiteId == null) {
            return Response.fail("testSuiteId不能为空");
        }

        // 检查该测试集下是否有testcase
        Action query = new Action();
        query.setTestSuiteId(testSuiteId);
        List<Action> actions = actionService.selectByAction(query);
        if (!CollectionUtils.isEmpty(actions)) {
            return Response.fail("该测试集下有测试用例，无法删除");
        }

        // 检查该测试集是否被testplan使用
        List<TestPlan> testPlans = testPlanService.selectByTestSuiteId(testSuiteId);
        if (!CollectionUtils.isEmpty(testPlans)) {
            return Response.fail("该测试集被测试计划使用，无法删除");
        }

        int deleteRow = testSuiteMapper.deleteByPrimaryKey(testSuiteId);
        return deleteRow == 1 ? Response.success("删除TestSuite成功") : Response.fail("删除TestSuite失败，请稍后重试");
    }

    public Response list(TestSuite testSuite, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<TestSuite> testSuites = selectByTestSuite(testSuite);
        List<TestSuiteVo> testSuiteVos = testSuites.stream().map(s -> TestSuiteVo.convert(s, UserCache.getNickNameById(s.getCreatorUid()))).collect(Collectors.toList());

        if (needPaging) {
            // java8 stream会导致PageHelper total计算错误
            // 所以这里用testSuites计算total
            long total = Page.getTotal(testSuites);
            return Response.success(Page.build(testSuiteVos, total));
        } else {
            return Response.success(testSuiteVos);
        }
    }

    public List<TestSuite> selectByTestSuite(TestSuite testSuite) {
        if (testSuite == null) {
            testSuite = new TestSuite();
        }

        TestSuiteExample testSuiteExample = new TestSuiteExample();
        TestSuiteExample.Criteria criteria = testSuiteExample.createCriteria();

        if (testSuite.getId() != null) {
            criteria.andIdEqualTo(testSuite.getId());
        }
        if (testSuite.getProjectId() != null) {
            criteria.andProjectIdEqualTo(testSuite.getProjectId());
        }
        if (!StringUtils.isEmpty(testSuite.getName())) {
            criteria.andNameEqualTo(testSuite.getName());
        }
        testSuiteExample.setOrderByClause("create_time desc");

        return testSuiteMapper.selectByExample(testSuiteExample);
    }

    public List<TestSuite> selectByPrimaryKeys(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }

        TestSuiteExample example = new TestSuiteExample();
        TestSuiteExample.Criteria criteria = example.createCriteria();

        criteria.andIdIn(ids);
        return testSuiteMapper.selectByExample(example);
    }

}
