package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.mbg.mapper.TestTaskDeviceMapper;
import com.yqhp.mbg.po.TestTaskDevice;
import com.yqhp.mbg.po.TestTaskDeviceExample;
import com.yqhp.model.Page;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jiangyitao.
 */
@Slf4j
@Service
public class TestTaskDeviceService {

    @Autowired
    private TestTaskDeviceMapper testTaskDeviceMapper;

    public Response update(TestTaskDevice testTaskDevice) {
        if (testTaskDevice.getId() == null) {
            return Response.fail("testTaskDevice不能为空");
        }

        int insertRow = testTaskDeviceMapper.insertSelective(testTaskDevice);
        if (insertRow != 1) {
            return Response.fail("更新失败，请稍后重试");
        } else {
            return Response.success("更新成功");
        }
    }

    public Response list(TestTaskDevice testTaskDevice, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if(needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        }

        TestTaskDeviceExample testTaskDeviceExample  = new TestTaskDeviceExample();
        TestTaskDeviceExample.Criteria criteria = testTaskDeviceExample.createCriteria();
        if(testTaskDevice.getId() != null) {
            criteria.andIdEqualTo(testTaskDevice.getId());
        }
        if(testTaskDevice.getTestTaskId() != null) {
            criteria.andTestTaskIdEqualTo(testTaskDevice.getTestTaskId());
        }
        if(!StringUtils.isEmpty(testTaskDevice.getDeviceId())) {
            criteria.andDeviceIdEqualTo(testTaskDevice.getDeviceId());
        }
        if(testTaskDevice.getStatus() != null) {
            criteria.andStatusEqualTo(testTaskDevice.getStatus());
        }
        List<TestTaskDevice> testTaskDevices = testTaskDeviceMapper.selectByExampleWithBLOBs(testTaskDeviceExample);

        if(needPaging) {
            return Response.success(Page.convert(new PageInfo(testTaskDevices)));
        } else {
            return Response.success(testTaskDevices);
        }
    }

    public Response findByDeviceIds(String[] deviceIds) {
        List<String> deviceIdList = Arrays.asList(deviceIds);

        TestTaskDeviceExample testTaskDeviceExample  = new TestTaskDeviceExample();
        TestTaskDeviceExample.Criteria criteria = testTaskDeviceExample.createCriteria();
        criteria.andDeviceIdIn(deviceIdList);

        return Response.success(testTaskDeviceMapper.selectByExampleWithBLOBs(testTaskDeviceExample));
    }
}
