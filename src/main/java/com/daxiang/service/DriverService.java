package com.daxiang.service;

import com.daxiang.dao.DriverDao;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.DriverMapper;
import com.daxiang.mbg.po.Driver;
import com.daxiang.mbg.po.DriverExample;
import com.daxiang.mbg.po.User;
import com.daxiang.model.PagedData;
import com.daxiang.model.PageRequest;
import com.daxiang.model.dto.DriverFile;
import com.daxiang.model.vo.DriverVo;
import com.daxiang.security.SecurityUtil;
import com.daxiang.utils.HttpServletUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
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
    @Autowired
    private FileService fileService;

    public void add(Driver driver) {
        driver.setCreateTime(new Date());
        driver.setCreatorUid(SecurityUtil.getCurrentUserId());

        try {
            int insertCount = driverMapper.insertSelective(driver);
            if (insertCount != 1) {
                throw new ServerException("添加失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(driver.getVersion() + "已存在");
        }
    }

    public void deleteAndClearRelatedRes(Integer driverId) {
        if (driverId == null) {
            throw new ServerException("driverId不能为空");
        }

        Driver driver = driverMapper.selectByPrimaryKey(driverId);
        if (driver == null) {
            throw new ServerException("driver不存在");
        }

        int deleteCount = driverMapper.deleteByPrimaryKey(driverId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }

        List<DriverFile> driverFiles = driver.getFiles();
        if (!CollectionUtils.isEmpty(driverFiles)) {
            driverFiles.forEach(driverFile -> fileService.deleteQuietly(driverFile.getFilePath()));
        }
    }

    public void update(Driver driver) {
        try {
            int updateCount = driverMapper.updateByPrimaryKeyWithBLOBs(driver);
            if (updateCount != 1) {
                throw new ServerException("更新失败，请稍后重试");
            }
        } catch (DuplicateKeyException e) {
            throw new ServerException(driver.getVersion() + "已存在");
        }
    }

    public PagedData<DriverVo> list(Driver query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "create_time desc";
        }

        List<DriverVo> driverVos = getDriverVos(query, orderBy);
        return new PagedData<>(driverVos, page.getTotal());
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

        List<DriverVo> driverVos = drivers.stream().map(driver -> {
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

        return driverVos;
    }

    public List<DriverVo> getDriverVos(Driver query, String orderBy) {
        List<Driver> drivers = getDrivers(query, orderBy);
        return convertDriversToDriverVos(drivers);
    }

    public List<Driver> getDrivers(Driver query, String orderBy) {
        DriverExample example = new DriverExample();

        if (query != null) {
            DriverExample.Criteria criteria = example.createCriteria();

            if (query.getId() != null) {
                criteria.andIdEqualTo(query.getId());
            }
            if (query.getType() != null) {
                criteria.andTypeEqualTo(query.getType());
            }
            if (!StringUtils.isEmpty(query.getVersion())) {
                criteria.andVersionEqualTo(query.getVersion());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return driverMapper.selectByExampleWithBLOBs(example);
    }

    public String getDownloadUrl(Integer type, String deviceId, Integer platform) {
        if (type == null || StringUtils.isEmpty(deviceId) || platform == null) {
            throw new ServerException("type or deviceId or platform 不能为空");
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
                        return HttpServletUtil.getStaticResourceUrl(filePath);
                    }
                }
            }
        }

        return "";
    }
}
