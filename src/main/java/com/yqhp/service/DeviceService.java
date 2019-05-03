package com.yqhp.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqhp.dao.DeviceDao;
import com.yqhp.mbg.mapper.DeviceMapper;
import com.yqhp.mbg.po.Device;
import com.yqhp.mbg.po.DeviceExample;
import com.yqhp.model.Page;
import com.yqhp.model.PageRequest;
import com.yqhp.model.Response;
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

    @Autowired
    private DeviceDao deviceDao;

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
        //是否要分页
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Device> devices = deviceDao.selectByDevice(device);
        if (needPaging) {
            return Response.success(Page.convert(new PageInfo(devices)));
        }
        return Response.success(devices);
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

    public Response getOnlineDevices(Integer type) {
        DeviceExample deviceExample = new DeviceExample();
        DeviceExample.Criteria criteria = deviceExample.createCriteria();
        criteria.andStatusNotEqualTo(Device.OFFLINE_STATUS);
        if(type != null) {
            criteria.andTypeEqualTo(type);
        }

        return Response.success(deviceMapper.selectByExample(deviceExample));
    }
}
