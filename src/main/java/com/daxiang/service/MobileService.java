package com.daxiang.service;

import com.daxiang.agent.AgentClient;
import com.daxiang.mbg.mapper.MobileMapper;
import com.daxiang.mbg.po.Mobile;
import com.daxiang.mbg.po.MobileExample;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.vo.MobileVo;
import com.github.pagehelper.PageHelper;
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
public class MobileService {

    @Autowired
    private MobileMapper mobileMapper;
    @Autowired
    private AgentClient agentClient;

    public Response save(Mobile mobile) {
        Mobile dbMobile = mobileMapper.selectByPrimaryKey(mobile.getId());
        int saveRow;
        if (dbMobile == null) {
            // 首次接入的mobile
            saveRow = mobileMapper.insertSelective(mobile);
        } else {
            // 更新Mobile
            saveRow = mobileMapper.updateByPrimaryKeySelective(mobile);
        }
        return saveRow == 1 ? Response.success("保存成功") : Response.fail("保存失败，请稍后重试");
    }

    public Response list(Mobile mobile, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Mobile> mobiles = selectByMobile(mobile);
        List<MobileVo> mobileVos = mobiles.stream().map(d -> {
            MobileVo mobileVo = new MobileVo();
            BeanUtils.copyProperties(d, mobileVo);
            return mobileVo;
        }).collect(Collectors.toList());

        if (needPaging) {
            long total = Page.getTotal(mobiles);
            return Response.success(Page.build(mobileVos, total));
        } else {
            return Response.success(mobileVos);
        }
    }

    public List<Mobile> selectByMobile(Mobile mobile) {
        MobileExample example = new MobileExample();
        MobileExample.Criteria criteria = example.createCriteria();

        if (mobile != null) {
            if (!StringUtils.isEmpty(mobile.getId())) {
                criteria.andIdEqualTo(mobile.getId());
            }
            if (!StringUtils.isEmpty(mobile.getName())) {
                criteria.andNameEqualTo(mobile.getName());
            }
            if (mobile.getEmulator() != null) {
                criteria.andEmulatorEqualTo(mobile.getEmulator());
            }
            if (!StringUtils.isEmpty(mobile.getAgentIp())) {
                criteria.andAgentIpEqualTo(mobile.getAgentIp());
            }
            if (mobile.getAgentPort() != null) {
                criteria.andAgentPortEqualTo(mobile.getAgentPort());
            }
            if (mobile.getPlatform() != null) {
                criteria.andPlatformEqualTo(mobile.getPlatform());
            }
            if (mobile.getStatus() != null) {
                criteria.andStatusEqualTo(mobile.getStatus());
            }
        }
        example.setOrderByClause("status desc,create_time desc");

        return mobileMapper.selectByExample(example);
    }

    /**
     * 前端点击"立即使用"
     *
     * @param mobileId
     * @return
     */
    public Response start(String mobileId) {
        if (StringUtils.isEmpty(mobileId)) {
            return Response.fail("mobileId不能为空");
        }

        Mobile dbMobile = mobileMapper.selectByPrimaryKey(mobileId);
        if (dbMobile == null) {
            return Response.fail("mobile不存在");
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
            return Response.fail("mobile不在线");
        } else {
            if (agentMobile.getStatus() == Mobile.IDLE_STATUS) {
                return Response.success(agentMobile);
            } else {
                // 同步最新状态
                mobileMapper.updateByPrimaryKeySelective(agentMobile);
                return Response.fail("mobile未闲置");
            }
        }
    }

    public Response getOnlineMobiles(Integer platform) {
        MobileExample mobileExample = new MobileExample();
        MobileExample.Criteria criteria = mobileExample.createCriteria();
        criteria.andStatusNotEqualTo(Mobile.OFFLINE_STATUS);
        if (platform != null) {
            criteria.andPlatformEqualTo(platform);
        }
        return Response.success(mobileMapper.selectByExample(mobileExample));
    }

    public List<Mobile> getOnlineMobilesByAgentIps(List<String> agentIps) {
        if (CollectionUtils.isEmpty(agentIps)) {
            return Collections.EMPTY_LIST;
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

        MobileExample mobileExample = new MobileExample();
        mobileExample.createCriteria().andAgentIpEqualTo(agentIp);

        mobileMapper.updateByExampleSelective(mobile, mobileExample);
    }

}
