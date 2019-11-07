package com.daxiang.service;

import com.daxiang.dao.DriverDao;
import com.daxiang.mbg.mapper.DriverMapper;
import com.daxiang.mbg.po.Driver;
import com.daxiang.mbg.po.DriverExample;
import com.daxiang.model.Page;
import com.daxiang.model.Response;
import com.daxiang.model.UserCache;
import com.daxiang.model.vo.DriverUrl;
import com.daxiang.model.vo.DriverVo;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class DriverService extends BaseService {

    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private DriverDao driverDao;

    public Response add(Driver driver) {
        driver.setCreateTime(new Date());
        driver.setCreatorUid(getUid());

        int insertRow;
        try {
            insertRow = driverMapper.insertSelective(driver);
        } catch (DuplicateKeyException e) {
            return Response.fail("版本已存在");
        }

        return insertRow == 1 ? Response.success("添加成功") : Response.fail("添加失败，请稍后重试");
    }

    public Response delete(Integer driverId) {
        if (driverId == null) {
            return Response.fail("driverId不能为空");
        }

        int deleteRow = driverMapper.deleteByPrimaryKey(driverId);
        return deleteRow == 1 ? Response.success("删除成功") : Response.fail("删除失败，请稍后重试");
    }

    public Response update(Driver driver) {
        if (driver.getId() == null) {
            return Response.fail("driverId不能为空");
        }

        int updateRow;
        try {
            updateRow = driverMapper.updateByPrimaryKeyWithBLOBs(driver);
        } catch (DuplicateKeyException e) {
            return Response.fail("版本已存在");
        }

        return updateRow == 1 ? Response.success("更新成功") : Response.fail("更新失败,请稍后重试");
    }

    public Response list(Driver driver) {
        List<Driver> drivers = selectByDriver(driver);
        List<DriverVo> driverVos = drivers.stream()
                .map(d -> DriverVo.convert(d, UserCache.getNickNameById(d.getCreatorUid()))).collect(Collectors.toList());
        // java8 stream会导致PageHelper total计算错误
        // 所以这里用drivers计算total
        long total = Page.getTotal(drivers);
        return Response.success(Page.build(driverVos, total));
    }

    public List<Driver> selectByDriver(Driver driver) {
        if (driver == null) {
            driver = new Driver();
        }

        DriverExample example = new DriverExample();
        DriverExample.Criteria criteria = example.createCriteria();

        if (driver.getId() != null) {
            criteria.andIdEqualTo(driver.getId());
        }
        if (driver.getType() != null) {
            criteria.andTypeEqualTo(driver.getType());
        }
        if (!StringUtils.isEmpty(driver.getVersion())) {
            criteria.andVersionEqualTo(driver.getVersion());
        }

        example.setOrderByClause("create_time desc");
        return driverMapper.selectByExampleWithBLOBs(example);
    }

    public Response getDownloadUrl(Integer type, String deviceId, Integer platform) {
        if (type == null || StringUtils.isEmpty(deviceId) || platform == null) {
            return Response.fail("非法参数");
        }

        List<Driver> drivers = driverDao.selectByTypeAndDeviceId(type, deviceId);
        if (!CollectionUtils.isEmpty(drivers)) {
            // 如果同一个手机对应了多个driver，取第一个
            Driver driver = drivers.get(0);
            List<DriverUrl> urls = driver.getUrls();
            if (!CollectionUtils.isEmpty(urls)) {
                Optional<DriverUrl> driverUrl = urls.stream().filter(url -> url.getPlatform() == platform).findFirst();
                if (driverUrl.isPresent()) {
                    String downloadUrl = driverUrl.get().getDownloadUrl();
                    if (!StringUtils.isEmpty(downloadUrl)) {
                        return Response.success(ImmutableMap.of("downloadUrl", downloadUrl));
                    }
                }
            }
        }

        return Response.success();
    }
}
