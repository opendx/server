package com.daxiang.service;

import com.daxiang.dao.DeviceTestTaskDao;
import com.daxiang.exception.BusinessException;
import com.daxiang.mbg.mapper.DeviceTestTaskMapper;
import com.daxiang.mbg.po.DeviceTestTaskExample;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.action.Step;
import com.daxiang.model.vo.Testcase;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.po.DeviceTestTask;
import com.daxiang.model.Page;
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
    private DeviceTestTaskDao deviceTestTaskDao;

    public Response update(DeviceTestTask deviceTestTask) {
        if (deviceTestTask.getId() == null) {
            return Response.fail("deviceTestTaskId不能为空");
        }

        int insertRow = deviceTestTaskMapper.updateByPrimaryKeySelective(deviceTestTask);
        return insertRow == 1 ? Response.success("更新成功") : Response.fail("更新失败，请稍后重试");
    }

    public Response list(DeviceTestTask deviceTestTask, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<DeviceTestTask> deviceTestTasks = selectByDeviceTestTask(deviceTestTask);

        if (needPaging) {
            long total = Page.getTotal(deviceTestTasks);
            return Response.success(Page.build(deviceTestTasks, total));
        } else {
            return Response.success(deviceTestTasks);
        }
    }

    public List<DeviceTestTask> selectByDeviceTestTask(DeviceTestTask deviceTestTask) {
        if (deviceTestTask == null) {
            deviceTestTask = new DeviceTestTask();
        }

        DeviceTestTaskExample deviceTestTaskExample = new DeviceTestTaskExample();
        DeviceTestTaskExample.Criteria criteria = deviceTestTaskExample.createCriteria();

        if (deviceTestTask.getId() != null) {
            criteria.andIdEqualTo(deviceTestTask.getId());
        }
        if (deviceTestTask.getTestTaskId() != null) {
            criteria.andTestTaskIdEqualTo(deviceTestTask.getTestTaskId());
        }
        if (!StringUtils.isEmpty(deviceTestTask.getDeviceId())) {
            criteria.andDeviceIdEqualTo(deviceTestTask.getDeviceId());
        }
        if (deviceTestTask.getStatus() != null) {
            criteria.andStatusEqualTo(deviceTestTask.getStatus());
        }

        return deviceTestTaskMapper.selectByExampleWithBLOBs(deviceTestTaskExample);
    }

    public Response findFirstUnStartDeviceTestTask(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return Response.fail("deviceId不能为空");
        }

        DeviceTestTaskExample deviceTestTaskExample = new DeviceTestTaskExample();
        DeviceTestTaskExample.Criteria criteria = deviceTestTaskExample.createCriteria();
        criteria.andDeviceIdEqualTo(deviceId).andStatusEqualTo(DeviceTestTask.UNSTART_STATUS);
        deviceTestTaskExample.setOrderByClause("id asc limit 1");

        List<DeviceTestTask> deviceTestTasks = deviceTestTaskMapper.selectByExampleWithBLOBs(deviceTestTaskExample);
        return CollectionUtils.isEmpty(deviceTestTasks) ? Response.success() : Response.success(deviceTestTasks.get(0));
    }

    public Response updateTestcase(Integer deviceTestTaskId, Testcase sourceTestcase) {
        if (deviceTestTaskId == null) {
            return Response.fail("deviceTestTaskId不能为空");
        }

        DeviceTestTask deviceTestTask = deviceTestTaskMapper.selectByPrimaryKey(deviceTestTaskId);
        if (deviceTestTask == null) {
            return Response.fail("DeviceTestTask不存在");
        }

        List<Testcase> testcases = deviceTestTask.getTestcases();
        testcases.stream()
                .filter(targetTestcase -> targetTestcase.getId().equals(sourceTestcase.getId()))
                .forEach(targetTestcase -> {
                    // 更新testcase运行结果
                    copyTestcaseProperties(sourceTestcase, targetTestcase);
                    List<Step> sourceSteps = sourceTestcase.getSteps();
                    if (!CollectionUtils.isEmpty(sourceSteps)) {
                        // 每次agent只会传1个要更新的步骤
                        Step sourceStep = sourceTestcase.getSteps().stream().findFirst().get();
                        List<Step> steps = targetTestcase.getSteps();
                        steps.stream()
                                .filter(targetStep -> targetStep.getNumber().equals(sourceStep.getNumber()))
                                .forEach(targetStep -> {
                                    // 更新step运行结果
                                    copyStepProperties(sourceStep, targetStep);
                                });
                    }
                });

        int updateRow = deviceTestTaskMapper.updateByPrimaryKeySelective(deviceTestTask);
        return updateRow == 1 ? Response.success("更新成功") : Response.fail("更新失败");
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
        if (!StringUtils.isEmpty(sourceTestcase.getFailImgUrl())) {
            targetTestcase.setFailImgUrl(sourceTestcase.getFailImgUrl());
        }
        if (!StringUtils.isEmpty(sourceTestcase.getVideoUrl())) {
            targetTestcase.setVideoUrl(sourceTestcase.getVideoUrl());
        }
    }

    public int insertSelective(DeviceTestTask deviceTestTask) {
        return deviceTestTaskMapper.insertSelective(deviceTestTask);
    }

    public List<DeviceTestTask> findByTestTaskId(Integer testTaskId) {
        DeviceTestTask deviceTestTask = new DeviceTestTask();
        deviceTestTask.setTestTaskId(testTaskId);
        return selectByDeviceTestTask(deviceTestTask);
    }

    public int deleteInBatch(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException("批量删除deviceTestTask, ids不能为空");
        }

        return deviceTestTaskDao.deleteInBatch(ids);
    }

    public Response delete(Integer deviceTestTaskId) {
        if (deviceTestTaskId == null) {
            return Response.fail("deviceTestTaskId不能为空");
        }

        DeviceTestTask deviceTestTask = deviceTestTaskMapper.selectByPrimaryKey(deviceTestTaskId);
        if (deviceTestTask == null) {
            return Response.fail("设备测试任务不存在");
        }

        if (!canDelete(deviceTestTask.getStatus())) {
            return Response.fail("执行过的测试任务不能删除");
        }

        int deleteRow = deviceTestTaskMapper.deleteByPrimaryKey(deviceTestTaskId);
        return deleteRow == 1 ? Response.success("删除成功") : Response.fail("删除失败，请稍后重试");
    }

    public boolean canDelete(Integer status) {
        return status == DeviceTestTask.UNSTART_STATUS || status == DeviceTestTask.ERROR_STATUS;
    }
}
