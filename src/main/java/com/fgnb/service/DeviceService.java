package com.fgnb.service;

import com.github.pagehelper.PageHelper;
import com.fgnb.mbg.mapper.DeviceMapper;
import com.fgnb.mbg.po.Device;
import com.fgnb.mbg.po.DeviceExample;
import com.fgnb.model.Page;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class DeviceService extends BaseService {

    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 保存手机信息
     *
     * @param device
     * @return
     */
    public Response save(Device device) {
        //数据库是否有该手机
        Device dbDevice = deviceMapper.selectByPrimaryKey(device.getId());
        int saveRow;
        if (dbDevice == null) {
            //首次接入的device
            saveRow = deviceMapper.insertSelective(device);
        } else {
            //更新Device
            saveRow = deviceMapper.updateByPrimaryKeySelective(device);
        }
        if (saveRow != 1) {
            return Response.fail("保存失败，请稍后重试");
        }
        return Response.success("保存成功");
    }

    /**
     * 查询设备列表
     *
     * @param device
     * @param pageRequest
     * @return
     */
    public Response list(Device device, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Device> devices = selectByDevice(device);
        if (needPaging) {
            long total = Page.getTotal(devices);
            return Response.success(Page.build(devices,total));
        } else {
            return Response.success(devices);
        }
    }

    public List<Device> selectByDevice(Device device) {
        if(device == null) {
            device = new Device();
        }

        DeviceExample deviceExample = new DeviceExample();
        DeviceExample.Criteria criteria = deviceExample.createCriteria();

        if(!StringUtils.isEmpty(device.getId())) {
            criteria.andIdEqualTo(device.getId());
        }
        if(!StringUtils.isEmpty(device.getName())) {
            criteria.andNameEqualTo(device.getName());
        }
        if(!StringUtils.isEmpty(device.getAgentIp())) {
            criteria.andAgentIpEqualTo(device.getAgentIp());
        }
        if(device.getAgentPort() != null) {
            criteria.andAgentPortEqualTo(device.getAgentPort());
        }
        if(device.getPlatform() != null) {
            criteria.andPlatformEqualTo(device.getPlatform());
        }
        if(device.getStatus() != null) {
            criteria.andStatusEqualTo(device.getStatus());
        }

        deviceExample.setOrderByClause("status desc,create_time desc");
        return deviceMapper.selectByExample(deviceExample);
    }

    /**
     * 开始控制手机
     *
     * @param deviceId
     * @return
     */
    public Response start(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return Response.fail("设备id不能为空");
        }

        Device device = deviceMapper.selectByPrimaryKey(deviceId);
        if (device == null) {
            return Response.fail("手机不存在");
        }

        if (device.getStatus() != Device.IDLE_STATUS) {
            return Response.fail("手机未闲置");
        }

        return Response.success("ok");
    }

    public Response getOnlineDevices(Integer platform) {
        DeviceExample deviceExample = new DeviceExample();
        DeviceExample.Criteria criteria = deviceExample.createCriteria();
        criteria.andStatusNotEqualTo(Device.OFFLINE_STATUS);
        if(platform != null) {
            criteria.andPlatformEqualTo(platform);
        }
        return Response.success(deviceMapper.selectByExample(deviceExample));
    }

    public List<Device> getOnlineDevicesByAgentIp(String agentIp) {
        DeviceExample deviceExample = new DeviceExample();
        deviceExample.createCriteria().andAgentIpEqualTo(agentIp).andStatusNotEqualTo(Device.OFFLINE_STATUS);
        return deviceMapper.selectByExample(deviceExample);
    }

    public int updateByAgentIp(Device device,String agentIp){
        DeviceExample deviceExample = new DeviceExample();
        deviceExample.createCriteria().andAgentIpEqualTo(agentIp);
        return deviceMapper.updateByExampleSelective(device, deviceExample);
    }
}
