package com.daxiang.service;

import com.daxiang.dao.TestSuiteDao;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.po.*;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.daxiang.mbg.mapper.TestSuiteMapper;
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
    private TestSuiteDao testSuiteDao;
    @Autowired
    private TestSuiteMapper testSuiteMapper;
    @Autowired
    private TestPlanService testPlanService;
    @Autowired
    private UserService userService;

    public void add(TestSuite testSuite) {
        testSuite.setCreateTime(new Date());
        testSuite.setCreatorUid(SecurityUtil.getCurrentUserId());

        try {
            int insertCount = testSuiteMapper.insertSelective(testSuite);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(testSuite.getName() + "已存在");
        }
    }

    public void delete(Integer testSuiteId) {
        // 检查该测试集是否被testplan使用
        List<TestPlan> testPlans = testPlanService.getTestPlansByTestSuiteId(testSuiteId);
        if (!CollectionUtils.isEmpty(testPlans)) {
            String testPlanNames = testPlans.stream().map(TestPlan::getName).collect(Collectors.joining("、"));
            throw new ServerException("测试计划: " + testPlanNames + "，正在使用，无法删除");
        }

        int deleteCount = testSuiteMapper.deleteByPrimaryKey(testSuiteId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }
    }

    public void update(TestSuite testSuite) {
        try {
            int updateCount = testSuiteMapper.updateByPrimaryKeySelective(testSuite);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(testSuite.getName() + "已存在");
        }
    }

    public PagedData<TestSuiteVo> list(TestSuite query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<TestSuiteVo> testSuiteVos = getTestSuiteVos(query, orderBy);
        return new PagedData<>(testSuiteVos, page.getTotal());
    }

    private List<TestSuiteVo> convertTestSuitesToTestSuiteVos(List<TestSuite> testSuites) {
        if (CollectionUtils.isEmpty(testSuites)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = testSuites.stream()
                .map(TestSuite::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

        List<TestSuiteVo> testSuiteVos = testSuites.stream().map(testSuite -> {
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

        return testSuiteVos;
    }

    public List<TestSuiteVo> getTestSuiteVos(TestSuite query, String orderBy) {
        List<TestSuite> testSuites = getTestSuites(query, orderBy);
        return convertTestSuitesToTestSuiteVos(testSuites);
    }

    public List<TestSuite> getTestSuites(TestSuite query, String orderBy) {
        TestSuiteExample example = new TestSuiteExample();

        if (query != null) {
            TestSuiteExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (query.getProjectId() != null) {
                criteria.andProjectIdEqualTo(query.getProjectId());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameEqualTo(query.getName());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return testSuiteMapper.selectByExampleWithBLOBs(example);
    }

    public List<TestSuite> getTestSuitesByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        TestSuiteExample example = new TestSuiteExample();
        TestSuiteExample.Criteria criteria = example.createCriteria();

        criteria.andIdIn(ids);
        return testSuiteMapper.selectByExampleWithBLOBs(example);
    }

    public List<TestSuite> getTestSuitesByActionId(Integer actionId) {
        return testSuiteDao.selectByActionId(actionId);
    }

}
