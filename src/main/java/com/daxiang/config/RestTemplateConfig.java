package com.daxiang.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by jiangyitao.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
        restTemplate.getInterceptors().add(new LoggingClientHttpRequestInterceptor());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(600000);
        factory.setConnectTimeout(600000);
        return factory;
    }

    @Slf4j
    static class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            printRequest(request, body);
            ClientHttpResponse response = execution.execute(request, body);
            printResponse(response);
            return response;
        }

        private void printRequest(HttpRequest request, byte[] body) {
            log.info("[http-request][{}->{}] {}", request.getMethod(), request.getURI(), new String(body, Charset.forName("UTF-8")));
        }

        private void printResponse(ClientHttpResponse response) throws IOException {
            log.info("[http-response] {}", IOUtils.toString(response.getBody(), Charset.forName("UTF-8")));
        }
    }
}
