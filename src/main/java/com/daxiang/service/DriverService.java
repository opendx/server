package com.daxiang.service;

import com.daxiang.dao.DriverDao;
import com.daxiang.mbg.mapper.DriverMapper;
import com.daxiang.mbg.po.Driver;
import com.daxiang.mbg.po.DriverExample;
import com.daxiang.mbg.po.User;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.dto.DriverFile;
import com.daxiang.model.vo.DriverVo;
import com.daxiang.security.SecurityUtil;
import com.daxiang.utils.HttpServletUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
public class DriverService {

    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private UserService userService;

    public Response add(Driver driver) {
        driver.setCreateTime(new Date());
        driver.setCreatorUid(SecurityUtil.getCurrentUserId());

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
        int updateRow;
        try {
            updateRow = driverMapper.updateByPrimaryKeyWithBLOBs(driver);
        } catch (DuplicateKeyException e) {
            return Response.fail("版本已存在");
        }

        return updateRow == 1 ? Response.success("更新成功") : Response.fail("更新失败，请稍后重试");
    }

    public Response list(Driver driver, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Driver> drivers = selectByDriver(driver);
        List<DriverVo> driverVos = convertDriversToDriverVos(drivers);

        if (needPaging) {
            long total = Page.getTotal(drivers);
            return Response.success(Page.build(driverVos, total));
        } else {
            return Response.success(driverVos);
        }
    }

    private List<DriverVo> convertDriversToDriverVos(List<Driver> drivers) {
        if (CollectionUtils.isEmpty(drivers)) {
            return new ArrayList<>();
        }

        List<Integer> creatorUids = drivers.stream()
                .map(Driver::getCreatorUid)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.getUserMapByIds(creatorUids);

        return drivers.stream().map(driver -> {
            DriverVo driverVo = new DriverVo();
            BeanUtils.copyProperties(driver, driverVo);

            if (driver.getCreatorUid() != null) {
                User user = userMap.get(driver.getCreatorUid());
                if (user != null) {
                    driverVo.setCreatorNickName(user.getNickName());
                }
            }

            return driverVo;
        }).collect(Collectors.toList());
    }

    private List<Driver> selectByDriver(Driver driver) {
        DriverExample example = new DriverExample();
        DriverExample.Criteria criteria = example.createCriteria();

        if (driver != null) {
            if (driver.getId() != null) {
                criteria.andIdEqualTo(driver.getId());
            }
            if (driver.getType() != null) {
                criteria.andTypeEqualTo(driver.getType());
            }
            if (!StringUtils.isEmpty(driver.getVersion())) {
                criteria.andVersionEqualTo(driver.getVersion());
            }
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
            // 如果同一个device对应了多个driver，取第一个
            List<DriverFile> driverFiles = drivers.get(0).getFiles();
            if (!CollectionUtils.isEmpty(driverFiles)) {
                Optional<DriverFile> driverFile = driverFiles.stream().filter(f -> platform.equals(f.getPlatform())).findFirst();
                if (driverFile.isPresent()) {
                    String filePath = driverFile.get().getFilePath();
                    if (!StringUtils.isEmpty(filePath)) {
                        return Response.success(ImmutableMap.of("downloadUrl", HttpServletUtil.getStaticResourceUrl(filePath)));
                    }
                }
            }
        }

        return Response.success();
    }
}
