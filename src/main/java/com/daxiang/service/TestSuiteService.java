package com.daxiang.service;

import com.daxiang.mbg.po.*;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.mapper.TestSuiteMapper;
import com.daxiang.model.Page;
import com.daxiang.model.vo.TestSuiteVo;
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
public class TestSuiteService {

    @Autowired
    private TestSuiteMapper testSuiteMapper;
    @Autowired
    private ActionService actionService;
    @Autowired
    private TestPlanService testPlanService;
    @Autowired
    private UserService userService;

    public Response add(TestSuite testSuite) {
        testSuite.setCreateTime(new Date());
        testSuite.setCreatorUid(SecurityUtil.getCurrentUserId());

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
        List<TestSuiteVo> testSuiteVos = convertTestSuitesToTestSuiteVos(testSuites);

        if (needPaging) {
            long total = Page.getTotal(testSuites);
            return Response.success(Page.build(testSuiteVos, total));
        } else {
            return Response.success(testSuiteVos);
        }
    }

    public List<TestSuiteVo> convertTestSuitesToTestSuiteVos(List<TestSuite> testSuites) {
        if (CollectionUtils.isEmpty(testSuites)) {
            return Collections.EMPTY_LIST;
        }

        List<Integer> creatorUids = testSuites.stream()
                .map(TestSuite::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByUserIds(creatorUids);

        return testSuites.stream().map(testSuite -> {
            TestSuiteVo testSuiteVo = new TestSuiteVo();
            BeanUtils.copyProperties(testSuite, testSuiteVo);

            if (testSuite.getCreatorUid() != null) {
                User user = userMap.get(testSuite.getCreatorUid());
                if (user != null) {
                    testSuiteVo.setCreatorNickName(user.getNickName());
                }
            }

            return testSuiteVo;
        }).collect(Collectors.toList());
    }

    public List<TestSuite> selectByTestSuite(TestSuite testSuite) {
        TestSuiteExample example = new TestSuiteExample();
        TestSuiteExample.Criteria criteria = example.createCriteria();

        if (testSuite != null) {
            if (testSuite.getId() != null) {
                criteria.andIdEqualTo(testSuite.getId());
            }
            if (testSuite.getProjectId() != null) {
                criteria.andProjectIdEqualTo(testSuite.getProjectId());
            }
            if (!StringUtils.isEmpty(testSuite.getName())) {
                criteria.andNameEqualTo(testSuite.getName());
            }
        }
        example.setOrderByClause("create_time desc");

        return testSuiteMapper.selectByExample(example);
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
