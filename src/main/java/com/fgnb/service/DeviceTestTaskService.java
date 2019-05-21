package com.fgnb.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fgnb.mbg.mapper.DeviceTestTaskMapper;
import com.fgnb.mbg.mapper.TestTaskMapper;
import com.fgnb.mbg.po.DeviceTestTask;
import com.fgnb.mbg.po.DeviceTestTaskExample;
import com.fgnb.mbg.po.TestTask;
import com.fgnb.model.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.vo.Testcase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Service
public class DeviceTestTaskService {

    @Autowired
    private DeviceTestTaskMapper deviceTestTaskMapper;
    @Autowired
    private TestTaskMapper testTaskMapper;

    public Response update(DeviceTestTask deviceTestTask) {
        if (deviceTestTask.getId() == null) {
            return Response.fail("testTaskDevice不能为空");
        }

        int insertRow = deviceTestTaskMapper.updateByPrimaryKeySelective(deviceTestTask);
        if (insertRow != 1) {
            return Response.fail("更新失败，请稍后重试");
        }

        //todo 统计改为定时任务
        //每个设备测试完成，需要检查是否所有设备都测试完成
        if (deviceTestTask.getStatus() != null && deviceTestTask.getStatus() == DeviceTestTask.FINISHED_STATUS) {
            deviceTestTask = deviceTestTaskMapper.selectByPrimaryKey(deviceTestTask.getId());
            DeviceTestTaskExample deviceTestTaskExample = new DeviceTestTaskExample();
            deviceTestTaskExample.createCriteria().andTestTaskIdEqualTo(deviceTestTask.getTestTaskId());
            List<DeviceTestTask> deviceTestTasks = deviceTestTaskMapper.selectByExampleWithBLOBs(deviceTestTaskExample);

            long unFinishedTask = deviceTestTasks.stream().filter(task -> task.getStatus() != DeviceTestTask.FINISHED_STATUS).count();
            if (unFinishedTask == 0) { // 本次测试，所有设备都完成了
                // 统计测试结果数据
                List<Testcase> testcases = deviceTestTasks.stream().flatMap(task -> task.getTestcases().stream()).collect(Collectors.toList());
                long passCount = testcases.stream().filter(testcase -> testcase.getStatus() == Testcase.PASS_STATUS).count();
                long failCount = testcases.stream().filter(testcase -> testcase.getStatus() == Testcase.FAIL_STATUS).count();
                long skipCount = testcases.stream().filter(testcase -> testcase.getStatus() == Testcase.SKIP_STATUS).count();

                TestTask testTask = new TestTask();
                testTask.setId(deviceTestTask.getTestTaskId());
                testTask.setStatus(TestTask.FINISHED_STATUS);
                testTask.setFinishTime(new Date());
                testTask.setPassCaseCount((int) passCount);
                testTask.setFailCaseCount((int) failCount);
                testTask.setSkipCaseCount((int) skipCount);

                testTaskMapper.updateByPrimaryKeySelective(testTask);
            }
        }

        return Response.success("更新成功");
    }

    public Response list(DeviceTestTask deviceTestTask, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<DeviceTestTask> deviceTestTasks = selectByDeviceTestTask(deviceTestTask);

        if (needPaging) {
            long total = Page.getTotal(deviceTestTasks);
            return Response.success(Page.build(deviceTestTasks,total));
        } else {
            return Response.success(deviceTestTasks);
        }
    }

    public List<DeviceTestTask> selectByDeviceTestTask(DeviceTestTask deviceTestTask) {
        if(deviceTestTask == null) {
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

    public Response findUnStartTestTasksByDeviceIds(String[] deviceIds) {
        List<String> deviceIdList = Arrays.asList(deviceIds);

        DeviceTestTaskExample deviceTestTaskExample = new DeviceTestTaskExample();
        DeviceTestTaskExample.Criteria criteria = deviceTestTaskExample.createCriteria();

        criteria.andDeviceIdIn(deviceIdList).andStatusEqualTo(DeviceTestTask.UNSTART_STATUS);

        return Response.success(deviceTestTaskMapper.selectByExampleWithBLOBs(deviceTestTaskExample));
    }

    public Response updateTestcase(Integer deviceTestTaskId, Testcase testcase) {
        if (deviceTestTaskId == null) {
            return Response.fail("deviceTestTaskId不能为空");
        }

        DeviceTestTask deviceTestTask = deviceTestTaskMapper.selectByPrimaryKey(deviceTestTaskId);
        if (deviceTestTask == null) {
            return Response.fail("DeviceTestTask不存在");
        } else {
            //更新testcase运行结果
            List<Testcase> testcases = deviceTestTask.getTestcases();
            for (Testcase tc : testcases) {
                if (tc.getId() == testcase.getId()) {
                    if (testcase.getStatus() != null) {
                        tc.setStatus(testcase.getStatus());
                    }
                    if (testcase.getStartTime() != null) {
                        tc.setStartTime(testcase.getStartTime());
                    }
                    if (testcase.getEndTime() != null) {
                        tc.setEndTime(testcase.getEndTime());
                    }
                    if (!StringUtils.isEmpty(testcase.getFailInfo())) {
                        tc.setFailInfo(testcase.getFailInfo());
                    }
                    if (!StringUtils.isEmpty(testcase.getFailImgUrl())) {
                        tc.setFailImgUrl(testcase.getFailImgUrl());
                    }
                    if (!StringUtils.isEmpty(testcase.getVideoUrl())) {
                        tc.setVideoUrl(testcase.getVideoUrl());
                    }
                    break;
                }
            }
            int updateRow = deviceTestTaskMapper.updateByPrimaryKeySelective(deviceTestTask);
            if (updateRow == 1) {
                return Response.success("更新成功");
            } else {
                return Response.fail("更新失败");
            }
        }
    }
}
