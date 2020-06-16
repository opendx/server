package com.daxiang.service;

import com.daxiang.agent.AgentClient;
import com.daxiang.mbg.mapper.BrowserMapper;
import com.daxiang.mbg.po.Browser;
import com.daxiang.mbg.po.BrowserExample;
import com.daxiang.model.Page;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
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

    public Response save(Browser browser) {
        Browser dbBrowser = browserMapper.selectByPrimaryKey(browser.getId());

        int saveRow;
        if (dbBrowser == null) {
            browser.setCreateTime(new Date());
            saveRow = browserMapper.insertSelective(browser);
        } else {
            saveRow = browserMapper.updateByPrimaryKeySelective(browser);
        }

        return saveRow == 1 ? Response.success("保存成功") : Response.fail("保存失败");
    }

    public Response list(Browser browser, PageRequest pageRequest) {
        boolean needPaging = pageRequest.needPaging();
        if (needPaging) {
            PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        }

        List<Browser> browsers = selectByBrowser(browser);

        if (needPaging) {
            long total = Page.getTotal(browsers);
            return Response.success(Page.build(browsers, total));
        } else {
            return Response.success(browsers);
        }
    }

    private List<Browser> selectByBrowser(Browser browser) {
        BrowserExample example = new BrowserExample();
        BrowserExample.Criteria criteria = example.createCriteria();

        if (browser != null) {
            if (!StringUtils.isEmpty(browser.getId())) {
                criteria.andIdEqualTo(browser.getId());
            }
            if (!StringUtils.isEmpty(browser.getType())) {
                criteria.andTypeEqualTo(browser.getType());
            }
            if (!StringUtils.isEmpty(browser.getVersion())) {
                criteria.andVersionEqualTo(browser.getVersion());
            }
            if (browser.getPlatform() != null) {
                criteria.andPlatformEqualTo(browser.getPlatform());
            }
            if (!StringUtils.isEmpty(browser.getAgentIp())) {
                criteria.andAgentIpEqualTo(browser.getAgentIp());
            }
            if (browser.getStatus() != null) {
                criteria.andStatusEqualTo(browser.getStatus());
            }
        }
        example.setOrderByClause("status desc,create_time desc");

        return browserMapper.selectByExample(example);
    }

    public Response start(String browserId) {
        if (StringUtils.isEmpty(browserId)) {
            return Response.fail("浏览器id不能为空");
        }

        Browser dbBrowser = browserMapper.selectByPrimaryKey(browserId);
        if (dbBrowser == null) {
            return Response.fail("浏览器不存在");
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
            return Response.fail("浏览器不在线");
        } else {
            if (agentBrowser.getStatus() == Browser.IDLE_STATUS) {
                return Response.success(agentBrowser);
            } else {
                // 同步最新状态
                browserMapper.updateByPrimaryKeySelective(agentBrowser);
                return Response.fail("浏览器未闲置");
            }
        }
    }

    public Response getOnlineBrowsers() {
        BrowserExample example = new BrowserExample();
        BrowserExample.Criteria criteria = example.createCriteria();

        criteria.andStatusNotEqualTo(Browser.OFFLINE_STATUS);
        return Response.success(browserMapper.selectByExample(example));
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

    private List<Browser> getBrowsersByIds(Set<String> browserIds) {
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
        return browsers.stream().collect(Collectors.toMap(Browser::getId, b -> b, (k1, k2) -> k1));
    }
}
