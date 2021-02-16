package com.daxiang.service;

import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.DeviceTestTaskMapper;
import com.daxiang.mbg.po.DeviceTestTaskExample;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.action.Step;
import com.daxiang.model.dto.Testcase;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.daxiang.mbg.po.DeviceTestTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Service
public class DeviceTestTaskService {

    @Autowired
    private DeviceTestTaskMapper deviceTestTaskMapper;
    @Autowired
    private FileService fileService;

    public void update(DeviceTestTask deviceTestTask) {
        int updateCount = deviceTestTaskMapper.updateByPrimaryKeySelective(deviceTestTask);
        if (updateCount != 1) {
            throw new ServerException("更新失败，请稍后重试");
        }
    }

    public PagedData<DeviceTestTask> list(DeviceTestTask query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        List<DeviceTestTask> deviceTestTasks = getDeviceTestTasks(query, orderBy);
        return new PagedData<>(deviceTestTasks, page.getTotal());
    }

    public List<DeviceTestTask> getDeviceTestTasks(DeviceTestTask query) {
        return getDeviceTestTasks(query, null);
    }

    public List<DeviceTestTask> getDeviceTestTasks(DeviceTestTask query, String orderBy) {
        DeviceTestTaskExample example = new DeviceTestTaskExample();

        if (query != null) {
            DeviceTestTaskExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (query.getTestTaskId() != null) {
                criteria.andTestTaskIdEqualTo(query.getTestTaskId());
            }
            if (!StringUtils.isEmpty(query.getDeviceId())) {
                criteria.andDeviceIdEqualTo(query.getDeviceId());
            }
            if (query.getStatus() != null) {
                criteria.andStatusEqualTo(query.getStatus());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return deviceTestTaskMapper.selectByExampleWithBLOBs(example);
    }

    public DeviceTestTask getFirstUnStartDeviceTestTask(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            throw new ServerException("deviceId不能为空");
        }

        DeviceTestTask query = new DeviceTestTask();
        query.setDeviceId(deviceId);
        query.setStatus(DeviceTestTask.UNSTART_STATUS);

        List<DeviceTestTask> deviceTestTasks = getDeviceTestTasks(query, "id asc limit 1");
        return CollectionUtils.isEmpty(deviceTestTasks) ? null : deviceTestTasks.get(0);
    }

    public void updateTestcase(Integer deviceTestTaskId, Testcase sourceTestcase) {
        if (deviceTestTaskId == null) {
            throw new ServerException("deviceTestTaskId不能为空");
        }

        DeviceTestTask deviceTestTask = deviceTestTaskMapper.selectByPrimaryKey(deviceTestTaskId);
        if (deviceTestTask == null) {
            throw new ServerException("deviceTestTask不存在");
        }

        deviceTestTask.getTestcases().stream()
                .filter(testcase -> testcase.getId().equals(sourceTestcase.getId()))
                .findFirst()
                .ifPresent(testcase -> {
                    // 更新testcase运行结果
                    copyTestcaseProperties(sourceTestcase, testcase);

                    if (!CollectionUtils.isEmpty(sourceTestcase.getSteps())) {
                        updateSteps(testcase.getSteps(), sourceTestcase.getSteps().get(0));
                    } else if (!CollectionUtils.isEmpty(sourceTestcase.getSetUp())) {
                        updateSteps(testcase.getSetUp(), sourceTestcase.getSetUp().get(0));
                    } else if (!CollectionUtils.isEmpty(sourceTestcase.getTearDown())) {
                        updateSteps(testcase.getTearDown(), sourceTestcase.getTearDown().get(0));
                    }
                });

        int updateCount = deviceTestTaskMapper.updateByPrimaryKeySelective(deviceTestTask);
        if (updateCount != 1) {
            throw new ServerException("更新失败，请稍后重试");
        }
    }

    private void updateSteps(List<Step> steps, Step sourceStep) {
        steps.stream()
                .filter(step -> step.getNumber().equals(sourceStep.getNumber()))
                .findFirst()
                .ifPresent(step -> {
                    // 更新step运行结果
                    copyStepProperties(sourceStep, step);
                });
    }

    private void copyStepProperties(Step sourceStep, Step targetStep) {
        if (sourceStep.getStartTime() != null) {
            targetStep.setStartTime(sourceStep.getStartTime());
        }
        if (sourceStep.getEndTime() != null) {
            targetStep.setEndTime(sourceStep.getEndTime());
        }
    }

    private void copyTestcaseProperties(Testcase sourceTestcase, Testcase targetTestcase) {
        if (sourceTestcase.getStatus() != null) {
            targetTestcase.setStatus(sourceTestcase.getStatus());
        }
        if (sourceTestcase.getStartTime() != null) {
            targetTestcase.setStartTime(sourceTestcase.getStartTime());
        }
        if (sourceTestcase.getEndTime() != null) {
            targetTestcase.setEndTime(sourceTestcase.getEndTime());
        }
        if (!StringUtils.isEmpty(sourceTestcase.getFailInfo())) {
            targetTestcase.setFailInfo(sourceTestcase.getFailInfo());
        }
        if (!StringUtils.isEmpty(sourceTestcase.getFailImgPath())) {
            targetTestcase.setFailImgPath(sourceTestcase.getFailImgPath());
        }
        if (!StringUtils.isEmpty(sourceTestcase.getVideoPath())) {
            targetTestcase.setVideoPath(sourceTestcase.getVideoPath());
        }
        if (!StringUtils.isEmpty(sourceTestcase.getLogPath())) {
            targetTestcase.setLogPath(sourceTestcase.getLogPath());
        }
    }

    public void add(DeviceTestTask deviceTestTask) {
        int insertCount = deviceTestTaskMapper.insertSelective(deviceTestTask);
        if (insertCount != 1) {
            throw new ServerException("添加失败");
        }
    }

    public List<DeviceTestTask> getDeviceTestTasksByTestTaskId(Integer testTaskId) {
        DeviceTestTask query = new DeviceTestTask();
        query.setTestTaskId(testTaskId);
        return getDeviceTestTasks(query);
    }

    public List<DeviceTestTask> getDeviceTestTasksByTestTaskIds(List<Integer> testTaskIds) {
        DeviceTestTaskExample example = new DeviceTestTaskExample();
        DeviceTestTaskExample.Criteria criteria = example.createCriteria();

        criteria.andTestTaskIdIn(testTaskIds);
        return deviceTestTaskMapper.selectByExampleWithBLOBs(example);
    }

    public void deleteBatch(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }

        DeviceTestTaskExample example = new DeviceTestTaskExample();
        DeviceTestTaskExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);

        int deleteCount = deviceTestTaskMapper.deleteByExample(example);
        if (deleteCount != ids.size()) {
            throw new ServerException("删除失败");
        }
    }

    public void deleteAndClearRelatedRes(Integer deviceTestTaskId) {
        if (deviceTestTaskId == null) {
            throw new ServerException("deviceTestTaskId不能为空");
        }

        DeviceTestTask deviceTestTask = deviceTestTaskMapper.selectByPrimaryKey(deviceTestTaskId);
        if (deviceTestTask == null) {
            throw new ServerException("deviceTestTask不存在");
        }

        if (deviceTestTask.getStatus() == DeviceTestTask.RUNNING_STATUS) {
            throw new ServerException("deviceTestTask正在执行，无法删除");
        }

        int deleteCount = deviceTestTaskMapper.deleteByPrimaryKey(deviceTestTaskId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }

        clearRelatedRes(deviceTestTask);
    }

    public void clearRelatedRes(DeviceTestTask deviceTestTask) {
        List<Testcase> testcases = deviceTestTask.getTestcases();
        if (!CollectionUtils.isEmpty(testcases)) {
            testcases.forEach(testcase -> {
                fileService.deleteQuietly(testcase.getFailImgPath());
                fileService.deleteQuietly(testcase.getVideoPath());
                fileService.deleteQuietly(testcase.getLogPath());
            });
        }
    }

}
