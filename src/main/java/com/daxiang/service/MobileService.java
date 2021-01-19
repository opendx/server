package com.daxiang.service;

import com.daxiang.agent.AgentClient;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.MobileMapper;
import com.daxiang.mbg.po.Mobile;
import com.daxiang.mbg.po.MobileExample;
import com.daxiang.model.PageRequest;
import com.daxiang.model.vo.MobileVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import com.daxiang.model.PagedData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jiangyitao.
 */
@Service
@Slf4j
public class MobileService {

    @Autowired
    private MobileMapper mobileMapper;
    @Autowired
    private AgentClient agentClient;
    @Autowired
    private FileService fileService;

    public void save(Mobile mobile) {
        Mobile dbMobile = mobileMapper.selectByPrimaryKey(mobile.getId());

        int saveCount;
        if (dbMobile == null) {
            // 首次接入的mobile
            saveCount = mobileMapper.insertSelective(mobile);
        } else {
            // 更新Mobile
            saveCount = mobileMapper.updateByPrimaryKeySelective(mobile);
        }

        if (saveCount != 1) {
            throw new ServerException("保存失败，请稍后重试");
        }
    }

    public void deleteAndClearRelatedRes(String mobileId) {
        if (mobileId == null) {
            throw new ServerException("mobileId不能为空");
        }

        Mobile mobile = mobileMapper.selectByPrimaryKey(mobileId);
        if (mobile == null) {
            throw new ServerException("mobile不存在");
        }

        int deleteCount = mobileMapper.deleteByPrimaryKey(mobileId);
        if (deleteCount != 1) {
            throw new ServerException("删除失败，请稍后重试");
        }

        try {
            agentClient.deleteMobile(mobile.getAgentIp(), mobile.getAgentPort(), mobileId);
        } catch (Exception ignore) {

        }

        fileService.deleteQuietly(mobile.getImgPath());
    }

    public PagedData<MobileVo> list(Mobile query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "status desc,create_time desc";
        }

        List<MobileVo> mobileVos = getMobileVos(query, orderBy);
        return new PagedData<>(mobileVos, page.getTotal());
    }

    public List<MobileVo> getMobileVos(Mobile query, String orderBy) {
        List<Mobile> mobiles = getMobiles(query, orderBy);
        List<MobileVo> mobileVos = mobiles.stream().map(mobile -> {
            MobileVo mobileVo = new MobileVo();
            BeanUtils.copyProperties(mobile, mobileVo);
            return mobileVo;
        }).collect(Collectors.toList());
        return mobileVos;
    }

    public List<Mobile> getMobiles(Mobile query, String orderBy) {
        MobileExample example = new MobileExample();

        if (query != null) {
            MobileExample.Criteria criteria = example.createCriteria();

            if (!StringUtils.isEmpty(query.getId())) {
                criteria.andIdEqualTo(query.getId());
            }
            if (!StringUtils.isEmpty(query.getName())) {
                criteria.andNameLike("%" + query.getName() + "%");
            }
            if (query.getEmulator() != null) {
                criteria.andEmulatorEqualTo(query.getEmulator());
            }
            if (!StringUtils.isEmpty(query.getSystemVersion())) {
                criteria.andSystemVersionEqualTo(query.getSystemVersion());
            }
            if (!StringUtils.isEmpty(query.getAgentIp())) {
                criteria.andAgentIpEqualTo(query.getAgentIp());
            }
            if (query.getAgentPort() != null) {
                criteria.andAgentPortEqualTo(query.getAgentPort());
            }
            if (query.getPlatform() != null) {
                criteria.andPlatformEqualTo(query.getPlatform());
            }
            if (query.getStatus() != null) {
                criteria.andStatusEqualTo(query.getStatus());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return mobileMapper.selectByExample(example);
    }

    public Mobile start(String mobileId) {
        if (StringUtils.isEmpty(mobileId)) {
            throw new ServerException("mobileId不能为空");
        }

        Mobile dbMobile = mobileMapper.selectByPrimaryKey(mobileId);
        if (dbMobile == null) {
            throw new ServerException("mobile不存在");
        }

        // 有时server被强制关闭，导致agent下mobile的状态无法同步到server
        // 可能会出现数据库里的mobile状态与实际不一致的情况
        // 在此通过agent获取最新的mobile状态
        Mobile agentMobile = null;
        try {
            agentMobile = agentClient.getMobile(dbMobile.getAgentIp(), dbMobile.getAgentPort(), dbMobile.getId()).getData();
        } catch (Exception ign) {
            // agent可能已经关闭
        }

        if (agentMobile == null) {
            if (dbMobile.getStatus() != Mobile.OFFLINE_STATUS) { // 数据库记录的不是离线，变为离线
                dbMobile.setStatus(Mobile.OFFLINE_STATUS);
                mobileMapper.updateByPrimaryKeySelective(dbMobile);
            }
            throw new ServerException("mobile不在线");
        } else {
            if (agentMobile.getStatus() == Mobile.IDLE_STATUS) {
                return agentMobile;
            } else {
                // 同步最新状态
                mobileMapper.updateByPrimaryKeySelective(agentMobile);
                throw new ServerException("mobile未闲置");
            }
        }
    }

    public List<Mobile> getOnlineMobiles(Integer platform) {
        MobileExample example = new MobileExample();
        MobileExample.Criteria criteria = example.createCriteria();

        criteria.andStatusNotEqualTo(Mobile.OFFLINE_STATUS);
        if (platform != null) {
            criteria.andPlatformEqualTo(platform);
        }
        return mobileMapper.selectByExample(example);
    }

    public List<Mobile> getOnlineMobilesByAgentIps(List<String> agentIps) {
        if (CollectionUtils.isEmpty(agentIps)) {
            return new ArrayList<>();
        }

        MobileExample example = new MobileExample();
        example.createCriteria()
                .andAgentIpIn(agentIps)
                .andStatusNotEqualTo(Mobile.OFFLINE_STATUS);

        return mobileMapper.selectByExample(example);
    }

    public void agentOffline(String agentIp) {
        Mobile mobile = new Mobile();
        mobile.setStatus(Mobile.OFFLINE_STATUS);

        MobileExample example = new MobileExample();
        example.createCriteria().andAgentIpEqualTo(agentIp);

        mobileMapper.updateByExampleSelective(mobile, example);
    }

    public List<Mobile> getMobilesByIds(Set<String> mobileIds) {
        if (CollectionUtils.isEmpty(mobileIds)) {
            return new ArrayList<>();
        }

        MobileExample example = new MobileExample();
        MobileExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(new ArrayList<>(mobileIds));
        return mobileMapper.selectByExample(example);
    }

    public Map<String, Mobile> getMobileMapByIds(Set<String> mobileIds) {
        List<Mobile> mobiles = getMobilesByIds(mobileIds);
        return mobiles.stream().collect(Collectors.toMap(Mobile::getId, Function.identity(), (k1, k2) -> k1));
    }
}
