package com.daxiang.service;

import com.daxiang.agent.AgentApi;
import com.daxiang.mbg.mapper.DeviceMapper;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.vo.DeviceVo;
import com.github.pagehelper.PageHelper;
import com.daxiang.mbg.po.Device;
import com.daxiang.mbg.po.DeviceExample;
import com.daxiang.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private AgentApi agentApi;

    /**
     * 保存设备信息
     *
     * @param device
     * @return
     */
    public Response save(Device device) {
        Device dbDevice = deviceMapper.selectByPrimaryKey(device.getId());
        int saveRow;
        if (dbDevice == null) {
            // 首次接入的device
            saveRow = deviceMapper.insertSelective(device);
        } else {
            // 更新Device
            saveRow = deviceMapper.updateByPrimaryKeySelective(device);
        }
        return saveRow == 1 ? Response.success("保存成功") : Response.fail("保存失败，请稍后重试");
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
        List<DeviceVo> deviceVos = devices.stream().map(d -> {
            DeviceVo deviceVo = new DeviceVo();
            BeanUtils.copyProperties(d, deviceVo);
            return deviceVo;
        }).collect(Collectors.toList());

        if (needPaging) {
            long total = Page.getTotal(devices);
            return Response.success(Page.build(deviceVos, total));
        } else {
            return Response.success(deviceVos);
        }
    }

    public List<Device> selectByDevice(Device device) {
        DeviceExample example = new DeviceExample();
        DeviceExample.Criteria criteria = example.createCriteria();

        if (device != null) {
            if (!StringUtils.isEmpty(device.getId())) {
                criteria.andIdEqualTo(device.getId());
            }
            if (!StringUtils.isEmpty(device.getName())) {
                criteria.andNameEqualTo(device.getName());
            }
            if (!StringUtils.isEmpty(device.getAgentIp())) {
                criteria.andAgentIpEqualTo(device.getAgentIp());
            }
            if (device.getAgentPort() != null) {
                criteria.andAgentPortEqualTo(device.getAgentPort());
            }
            if (device.getPlatform() != null) {
                criteria.andPlatformEqualTo(device.getPlatform());
            }
            if (device.getStatus() != null) {
                criteria.andStatusEqualTo(device.getStatus());
            }
        }
        example.setOrderByClause("status desc,create_time desc");

        return deviceMapper.selectByExample(example);
    }

    /**
     * 前端点击"立即使用"
     *
     * @param deviceId
     * @return
     */
    public Response start(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return Response.fail("设备id不能为空");
        }

        Device dbDevice = deviceMapper.selectByPrimaryKey(deviceId);
        if (dbDevice == null) {
            return Response.fail("设备不存在");
        }

        // 有时server被强制关闭，导致agent下设备的状态无法同步到server
        // 可能会出现数据库里的设备状态与实际不一致的情况
        // 在此通过agent获取最新的设备状态
        Device agentDevice = null;
        try {
            agentDevice = agentApi.getDeviceStatus(dbDevice.getAgentIp(), dbDevice.getAgentPort(), dbDevice.getId()).getData();
        } catch (Exception ign) {
            // agent可能已经关闭
        }

        if (agentDevice == null) {
            if (dbDevice.getStatus() != Device.OFFLINE_STATUS) { // 数据库记录的不是离线，变为离线
                dbDevice.setStatus(Device.OFFLINE_STATUS);
                deviceMapper.updateByPrimaryKeySelective(dbDevice);
            }
            return Response.fail("设备不在线");
        } else {
            if (agentDevice.getStatus() == Device.IDLE_STATUS) {
                return Response.success(agentDevice);
            } else {
                // 同步最新状态
                deviceMapper.updateByPrimaryKeySelective(agentDevice);
                return Response.fail("设备未闲置");
            }
        }
    }

    public Response getOnlineDevices(Integer platform) {
        DeviceExample deviceExample = new DeviceExample();
        DeviceExample.Criteria criteria = deviceExample.createCriteria();
        criteria.andStatusNotEqualTo(Device.OFFLINE_STATUS);
        if (platform != null) {
            criteria.andPlatformEqualTo(platform);
        }
        return Response.success(deviceMapper.selectByExample(deviceExample));
    }

    public List<Device> getOnlineDevicesByAgentIps(List<String> agentIps) {
        if (CollectionUtils.isEmpty(agentIps)) {
            return Collections.EMPTY_LIST;
        }

        DeviceExample example = new DeviceExample();
        example.createCriteria()
                .andAgentIpIn(agentIps)
                .andStatusNotEqualTo(Device.OFFLINE_STATUS);

        return deviceMapper.selectByExample(example);
    }

    public void agentOffline(String agentIp) {
        Device device = new Device();
        device.setStatus(Device.OFFLINE_STATUS);

        DeviceExample deviceExample = new DeviceExample();
        deviceExample.createCriteria().andAgentIpEqualTo(agentIp);

        deviceMapper.updateByExampleSelective(device, deviceExample);
    }

}
