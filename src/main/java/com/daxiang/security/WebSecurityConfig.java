package com.daxiang.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by jiangyitao.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${frontend}")
    private String frontend;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        HttpMethod permitHttpMethod = HttpMethod.OPTIONS;
        String[] permitAntPatterns = new String[]{
                "/",
                "/user/login",
                "/upload/**",
                "/" + frontend + "/**",
                // 以下为agent调用的接口
                "/springboot-admin/**",
                "/action/resetBasicAction",
                "/upload/file/*",
                "/project/list",
                "/mobile/list",
                "/agentExtJar/lastUploadTimeList",
                "/mobile/save",
                "/browser/save",
                "/driver/downloadUrl",
                "/deviceTestTask/**"
        };

        http.authorizeRequests()
                .antMatchers(permitHttpMethod).permitAll()
                .antMatchers(permitAntPatterns).permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        jwtTokenFilter.setShouldNotFilterHttpMethod(permitHttpMethod);
        jwtTokenFilter.setShouldNotFilterAntPatterns(new HashSet<>(Arrays.asList(permitAntPatterns)));

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
