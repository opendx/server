package com.yqhp.service;

import com.yqhp.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiangyitao.
 */
@Slf4j
public class BaseService {
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 从请求头获取token，解析出用户id
     *
     * @return
     * @throws Exception
     */
    protected Integer getUid() {
        return 1;
//        String token = httpServletRequest.getHeader("token");
//        return Integer.parseInt(TokenUtil.parse(token));
    }
}
