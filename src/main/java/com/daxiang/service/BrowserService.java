package com.daxiang.service;

import com.daxiang.agent.AgentClient;
import com.daxiang.exception.ServerException;
import com.daxiang.mbg.mapper.BrowserMapper;
import com.daxiang.mbg.po.Browser;
import com.daxiang.mbg.po.BrowserExample;
import com.daxiang.model.PagedData;
import com.daxiang.model.PageRequest;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
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
public class BrowserService {

    @Autowired
    private BrowserMapper browserMapper;
    @Autowired
    private AgentClient agentClient;

    public void save(Browser browser) {
        Browser dbBrowser = browserMapper.selectByPrimaryKey(browser.getId());

        int saveCount;
        if (dbBrowser == null) {
            browser.setCreateTime(new Date());
            saveCount = browserMapper.insertSelective(browser);
        } else {
            saveCount = browserMapper.updateByPrimaryKeySelective(browser);
        }

        if (saveCount != 1) {
            throw new ServerException("保存失败，请稍后重试");
        }
    }

    public PagedData<Browser> list(Browser query, String orderBy, PageRequest pageRequest) {
        Page page = PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());

        if (StringUtils.isEmpty(orderBy)) {
            orderBy = "status desc,create_time desc";
        }

        List<Browser> browsers = getBrowsers(query, orderBy);
        return new PagedData<>(browsers, page.getTotal());
    }

    public List<Browser> getBrowsers(Browser query, String orderBy) {
        BrowserExample example = new BrowserExample();

        if (query != null) {
            BrowserExample.Criteria criteria = example.createCriteria();

            if (!StringUtils.isEmpty(query.getId())) {
                criteria.andIdEqualTo(query.getId());
            }
            if (!StringUtils.isEmpty(query.getType())) {
                criteria.andTypeEqualTo(query.getType());
            }
            if (!StringUtils.isEmpty(query.getVersion())) {
                criteria.andVersionEqualTo(query.getVersion());
            }
            if (query.getPlatform() != null) {
                criteria.andPlatformEqualTo(query.getPlatform());
            }
            if (!StringUtils.isEmpty(query.getAgentIp())) {
                criteria.andAgentIpEqualTo(query.getAgentIp());
            }
            if (query.getStatus() != null) {
                criteria.andStatusEqualTo(query.getStatus());
            }
        }

        if (!StringUtils.isEmpty(orderBy)) {
            example.setOrderByClause(orderBy);
        }

        return browserMapper.selectByExample(example);
    }

    public Browser start(String browserId) {
        if (StringUtils.isEmpty(browserId)) {
            throw new ServerException("浏览器id不能为空");
        }

        Browser dbBrowser = browserMapper.selectByPrimaryKey(browserId);
        if (dbBrowser == null) {
            throw new ServerException("浏览器不存在");
        }

        // 有时server被强制关闭，导致数据库浏览器状态与实际不一致
        // 在此通过agent获取最新的浏览器状态
        Browser agentBrowser = null;
        try {
            agentBrowser = agentClient.getBrowser(dbBrowser.getAgentIp(), dbBrowser.getAgentPort(), dbBrowser.getId()).getData();
        } catch (Exception ign) {
            // agent可能已经关闭
        }

        if (agentBrowser == null) {
            if (dbBrowser.getStatus() != Browser.OFFLINE_STATUS) { // 数据库记录的不是离线，变为离线
                dbBrowser.setStatus(Browser.OFFLINE_STATUS);
                browserMapper.updateByPrimaryKeySelective(dbBrowser);
            }
            throw new ServerException("浏览器不在线");
        } else {
            if (agentBrowser.getStatus() == Browser.IDLE_STATUS) {
                return agentBrowser;
            } else {
                // 同步最新状态
                browserMapper.updateByPrimaryKeySelective(agentBrowser);
                throw new ServerException("浏览器未闲置");
            }
        }
    }

    public List<Browser> getOnlineBrowsers() {
        BrowserExample example = new BrowserExample();
        BrowserExample.Criteria criteria = example.createCriteria();

        criteria.andStatusNotEqualTo(Browser.OFFLINE_STATUS);
        return browserMapper.selectByExample(example);
    }

    public List<Browser> getOnlineBrowsersByAgentIps(List<String> agentIps) {
        if (CollectionUtils.isEmpty(agentIps)) {
            return new ArrayList<>();
        }

        BrowserExample example = new BrowserExample();
        example.createCriteria()
                .andAgentIpIn(agentIps)
                .andStatusNotEqualTo(Browser.OFFLINE_STATUS);

        return browserMapper.selectByExample(example);
    }

    public void agentOffline(String agentIp) {
        Browser browser = new Browser();
        browser.setStatus(Browser.OFFLINE_STATUS);

        BrowserExample example = new BrowserExample();
        example.createCriteria().andAgentIpEqualTo(agentIp);

        browserMapper.updateByExampleSelective(browser, example);
    }

    public List<Browser> getBrowsersByIds(Set<String> browserIds) {
        if (CollectionUtils.isEmpty(browserIds)) {
            return new ArrayList<>();
        }

        BrowserExample example = new BrowserExample();
        BrowserExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(new ArrayList<>(browserIds));
        return browserMapper.selectByExample(example);
    }

    public Map<String, Browser> getBrowserMapByBrowserIds(Set<String> browserIds) {
        List<Browser> browsers = getBrowsersByIds(browserIds);
        return browsers.stream().collect(Collectors.toMap(Browser::getId, Function.identity(), (k1, k2) -> k1));
    }
}
