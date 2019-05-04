package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.mbg.mapper.DeviceTestTaskMapper;
import com.yqhp.mbg.mapper.TestTaskMapper;
import com.yqhp.mbg.po.DeviceTestTask;
import com.yqhp.mbg.po.DeviceTestTaskExample;
import com.yqhp.mbg.po.TestTask;
import com.yqhp.model.Page;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import com.yqhp.model.vo.Testcase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
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
    private TestTaskMapper testTaskMapper;

    public Response update(DeviceTestTask deviceTestTask) {
        if (deviceTestTask.getId() == null) {
            return Response.fail("testTaskDevice不能为空");
        }

        int insertRow = deviceTestTaskMapper.updateByPrimaryKeySelective(deviceTestTask);
        if (insertRow != 1) {
            return Response.fail("更新失败，请稍后重试");
        }

        if(deviceTestTask.getStatus() != null && deviceTestTask.getStatus() == DeviceTestTask.FINISHED_STATUS) {
            deviceTestTask = deviceTestTaskMapper.selectByPrimaryKey(deviceTestTask.getId());
            DeviceTestTaskExample deviceTestTaskExample = new DeviceTestTaskExample();
            deviceTestTaskExample.createCriteria().andTestTaskIdEqualTo(deviceTestTask.getTestTaskId());
            List<DeviceTestTask> deviceTestTasks = deviceTestTaskMapper.selectByExampleWithBLOBs(deviceTestTaskExample);

            long unFinishedTask = deviceTestTasks.stream().filter(task -> task.getStatus() != DeviceTestTask.FINISHED_STATUS).count();
            if(unFinishedTask == 0) { // 本次测试，所有设备都完成了
                // 统计测试结果数据
                int passCount = 0;
                int failCount = 0;
                int skipCount = 0;
                for(DeviceTestTask testTask: deviceTestTasks) {
                    for(Testcase testcase: testTask.getTestcases()) {
                        if(testcase.getStatus() == Testcase.PASS_STATUS) {
                            passCount = passCount + 1;
                        }else if(testcase.getStatus() == Testcase.FAIL_STATUS) {
                            failCount = failCount + 1;
                        }else if(testcase.getStatus() == Testcase.SKIP_STATUS) {
                            skipCount = skipCount + 1;
                        }
                    }
                }
                TestTask testTask = new TestTask();
                testTask.setId(deviceTestTask.getTestTaskId());
                testTask.setStatus(TestTask.FINISHED_STATUS);
                testTask.setFinishTime(new Date());
                testTask.setPassCaseCount(passCount);
                testTask.setFailCaseCount(failCount);
                testTask.setSkipCaseCount(skipCount);
                testTaskMapper.updateByPrimaryKeySelective(testTask);
            }
        }

        return Response.success("更新成功");
    }

    public Response list(DeviceTestTask deviceTestTask, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if(needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        }

        DeviceTestTaskExample deviceTestTaskExample  = new DeviceTestTaskExample();
        DeviceTestTaskExample.Criteria criteria = deviceTestTaskExample.createCriteria();
        if(deviceTestTask.getId() != null) {
            criteria.andIdEqualTo(deviceTestTask.getId());
        }
        if(deviceTestTask.getTestTaskId() != null) {
            criteria.andTestTaskIdEqualTo(deviceTestTask.getTestTaskId());
        }
        if(!StringUtils.isEmpty(deviceTestTask.getDeviceId())) {
            criteria.andDeviceIdEqualTo(deviceTestTask.getDeviceId());
        }
        if(deviceTestTask.getStatus() != null) {
            criteria.andStatusEqualTo(deviceTestTask.getStatus());
        }
        List<DeviceTestTask> deviceTestTasks = deviceTestTaskMapper.selectByExampleWithBLOBs(deviceTestTaskExample);

        if(needPaging) {
            return Response.success(Page.convert(new PageInfo(deviceTestTasks)));
        } else {
            return Response.success(deviceTestTasks);
        }
    }

    public Response findUnStartTestTasksByDeviceIds(String[] deviceIds) {
        List<String> deviceIdList = Arrays.asList(deviceIds);

        DeviceTestTaskExample deviceTestTaskExample  = new DeviceTestTaskExample();
        DeviceTestTaskExample.Criteria criteria = deviceTestTaskExample.createCriteria();

        criteria.andDeviceIdIn(deviceIdList).andStatusEqualTo(DeviceTestTask.UNSTART_STATUS);

        return Response.success(deviceTestTaskMapper.selectByExampleWithBLOBs(deviceTestTaskExample));
    }
}
