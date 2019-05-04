package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.mbg.mapper.DeviceTestTaskMapper;
import com.yqhp.mbg.po.DeviceTestTask;
import com.yqhp.mbg.po.DeviceTestTaskExample;
import com.yqhp.model.Page;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Service
public class DeviceTestTaskService {

    @Autowired
    private DeviceTestTaskMapper deviceTestTaskMapper;

    public Response update(DeviceTestTask deviceTestTask) {
        if (deviceTestTask.getId() == null) {
            return Response.fail("testTaskDevice不能为空");
        }

        int insertRow = deviceTestTaskMapper.insertSelective(deviceTestTask);
        if (insertRow != 1) {
            return Response.fail("更新失败，请稍后重试");
        } else {
            return Response.success("更新成功");
        }
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
