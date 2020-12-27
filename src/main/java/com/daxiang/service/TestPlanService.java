package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.mbg.po.TestPlan;
import com.daxiang.mbg.po.TestPlanExample;
import com.daxiang.mbg.po.User;
import com.daxiang.model.PagedData;
import com.daxiang.model.PageRequest;
import com.daxiang.dao.TestPlanDao;
import com.daxiang.model.vo.TestPlanVo;
import com.daxiang.security.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.daxiang.mbg.mapper.TestPlanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Service
public class TestPlanService {

    private static final ThreadPoolTaskScheduler TASK_SCHEDULER;
    private static final Map<Integer, ScheduledFuture> TEST_PLAN_SCHEDULED_FUTURE_MAP = new ConcurrentHashMap<>();

    static {
        TASK_SCHEDULER = new ThreadPoolTaskScheduler();
        TASK_SCHEDULER.initialize();
    }

    @Autowired
    private TestPlanMapper testPlanMapper;
    @Autowired
    private TestPlanDao testPlanDao;
    @Autowired
    private TestTaskService testTaskService;
    @Autowired
    private UserService userService;

    @Transactional
    public void add(TestPlan testPlan) {
        if (testPlan.getEnableSchedule() == TestPlan.ENABLE_SCHEDULE
                && StringUtils.isEmpty(testPlan.getCronExpression())) {
            // 若开启定时任务，表达式不能为空
            throw new ServerException("cron表达式不能为空");
        }

        testPlan.setCreateTime(new Date());
        testPlan.setCreatorUid(SecurityUtil.getCurrentUserId());

        try {
            int insertCount = testPlanMapper.insertSelective(testPlan);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(testPlan.getName() + "已存在");
        }

        if (testPlan.getEnableSchedule() == TestPlan.ENABLE_SCHEDULE) {
            addOrUpdateScheduleTask(testPlan);
        }
    }

    @Transactional
    public void delete(Integer testPlanId) {
        int deleteCount = testPlanMapper.deleteByPrimaryKey(testPlanId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }
        cancelScheduleTask(testPlanId);
    }

    @Transactional
    public void update(TestPlan testPlan) {
        if (testPlan.getEnableSchedule() == TestPlan.ENABLE_SCHEDULE
                && StringUtils.isEmpty(testPlan.getCronExpression())) {
            // 若开启定时任务，表达式不能为空
            throw new ServerException("cron表达式不能为空");
        }

        try {
            int updateCount = testPlanMapper.updateByPrimaryKeyWithBLOBs(testPlan);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(testPlan.getName() + "已存在");
        }

        if (testPlan.getEnableSchedule() == TestPlan.ENABLE_SCHEDULE) {
            addOrUpdateScheduleTask(testPlan);
        } else {
            cancelScheduleTask(testPlan.getId());
        }
    }

    public PagedData<TestPlanVo> list(TestPlan query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<TestPlanVo> testPlanVos = getTestPlanVos(query, orderBy);
        return new PagedData<>(testPlanVos, page.getTotal());
    }

    private List<TestPlanVo> convertTestPlansToTestPlanVos(List<TestPlan> testPlans) {
        if (CollectionUtils.isEmpty(testPlans)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = testPlans.stream()
                .map(TestPlan::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

        List<TestPlanVo> testPlanVos = testPlans.stream().map(testPlan -> {
            TestPlanVo testPlanVo = new TestPlanVo();
            BeanUtils.copyProperties(testPlan, testPlanVo);

            if (testPlan.getCreatorUid() != null) {
                User user = userMap.get(testPlan.getCreatorUid());
                if (user != null) {
                    testPlanVo.setCreatorNickName(user.getNickName());
                }
            }

            return testPlanVo;
        }).collect(Collectors.toList());

        return testPlanVos;
    }

    public List<TestPlanVo> getTestPlanVos(TestPlan query, String orderBy) {
        List<TestPlan> testPlans = getTestPlans(query, orderBy);
        return convertTestPlansToTestPlanVos(testPlans);
    }

    public List<TestPlan> getTestPlans(TestPlan query) {
        return getTestPlans(query, null);
    }

    public List<TestPlan> getTestPlans(TestPlan query, String orderBy) {
        TestPlanExample example = new TestPlanExample();

        if (query != null) {
            TestPlanExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (query.getProjectId() != null) {
                criteria.andProjectIdEqualTo(query.getProjectId());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameEqualTo(query.getName());
            }
            if (query.getRunMode() != null) {
                criteria.andRunModeEqualTo(query.getRunMode());
            }
            if (query.getEnableSchedule() != null) {
                criteria.andEnableScheduleEqualTo(query.getEnableSchedule());
            }
            if (query.getEnvironmentId() != null) {
                criteria.andEnvironmentIdEqualTo(query.getEnvironmentId());
            }
            if (query.getEnableRecordVideo() != null) {
                criteria.andEnableRecordVideoEqualTo(query.getEnableRecordVideo());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return testPlanMapper.selectByExampleWithBLOBs(example);
    }

    public void scheduleEnabledTasks() {
        TestPlan query = new TestPlan();
        query.setEnableSchedule(TestPlan.ENABLE_SCHEDULE);

        List<TestPlan> testPlans = getTestPlans(query);
        testPlans.forEach(this::addOrUpdateScheduleTask);
    }

    private synchronized void addOrUpdateScheduleTask(TestPlan testPlan) {
        ScheduledFuture future = TEST_PLAN_SCHEDULED_FUTURE_MAP.get(testPlan.getId());
        if (future != null) {
            // 取消上一次设置的定时任务
            log.info("cancel schedule, testPlan: {}", testPlan.getName());
            future.cancel(true);
        }

        log.info("add schedule, testPlan: {}", testPlan.getName());
        CronTrigger cronTrigger = new CronTrigger(testPlan.getCronExpression());
        future = TASK_SCHEDULER.schedule(() -> testTaskService.commit(testPlan.getId(), testPlan.getCreatorUid()), cronTrigger);
        TEST_PLAN_SCHEDULED_FUTURE_MAP.put(testPlan.getId(), future);
    }

    private synchronized void cancelScheduleTask(Integer testPlanId) {
        if (testPlanId == null) {
            throw new ServerException("testPlanId不能为空");
        }

        ScheduledFuture future = TEST_PLAN_SCHEDULED_FUTURE_MAP.get(testPlanId);
        if (future != null) {
            log.info("cancel schedule, testPlanId: {}", testPlanId);
            future.cancel(true);
            TEST_PLAN_SCHEDULED_FUTURE_MAP.remove(testPlanId);
        }
    }

    public TestPlan getTestPlanById(Integer testPlanId) {
        return testPlanMapper.selectByPrimaryKey(testPlanId);
    }

    public List<TestPlan> getTestPlansByTestSuiteId(Integer testSuiteId) {
        if (testSuiteId == null) {
            throw new ServerException("testSuiteId不能为空");
        }
        return testPlanDao.selectByTestSuiteId(testSuiteId);
    }

    public List<TestPlan> getTestPlansByActionId(Integer actionId) {
        return testPlanDao.selectByActionId(actionId);
    }

    public List<TestPlan> getTestPlansByEnvironmentId(Integer envId) {
        if (envId == null) {
            throw new ServerException("envId不能为空");
        }

        TestPlan query = new TestPlan();
        query.setEnvironmentId(envId);
        return getTestPlans(query);
    }
}