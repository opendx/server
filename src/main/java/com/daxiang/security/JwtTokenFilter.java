package com.daxiang.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by jiangyitao.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = JwtTokenUtil.getTokenFromHttpRequestHeader(request);
        if (StringUtils.isEmpty(token)) {
            authenticationEntryPoint.commence(request, response, new BadCredentialsException("token不能为空"));
            return;
        }

        String username = JwtTokenUtil.parseUsername(token);
        if (username == null) {
            authenticationEntryPoint.commence(request, response, new BadCredentialsException("无效的token"));
            return;
        }

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            authenticationEntryPoint.commence(request, response, e);
            return;
        }

        if (!userDetails.isEnabled()) {
            authenticationEntryPoint.commence(request, response, new DisabledException("账号已禁用"));
            return;
        }
        if (!userDetails.isAccountNonLocked()) {
            authenticationEntryPoint.commence(request, response, new LockedException("账户已锁定"));
            return;
        }
        if (!userDetails.isCredentialsNonExpired()) {
            authenticationEntryPoint.commence(request, response, new CredentialsExpiredException("凭证已过期"));
            return;
        }
        if (!userDetails.isAccountNonExpired()) {
            authenticationEntryPoint.commence(request, response, new AccountExpiredException("账号已过期"));
            return;
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private Set<String> shouldNotFilterAntPatterns;
    private String shouldNotFilterHttpMethod;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    /**
     * 是否需要走doFilterInternal逻辑
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (shouldNotFilterHttpMethod != null
                && shouldNotFilterHttpMethod.equals(request.getMethod())) {
            return true;
        }

        if (shouldNotFilterAntPatterns != null) {
            return shouldNotFilterAntPatterns.stream()
                    .anyMatch(p -> antPathMatcher.match(p, urlPathHelper.getRequestUri(request)));
        }

        return false;
    }

    public void setShouldNotFilterAntPatterns(Set<String> shouldNotFilterAntPatterns) {
        this.shouldNotFilterAntPatterns = shouldNotFilterAntPatterns;
    }

    public void setShouldNotFilterHttpMethod(HttpMethod shouldNotFilterHttpMethod) {
        this.shouldNotFilterHttpMethod = shouldNotFilterHttpMethod.toString();
    }
}
