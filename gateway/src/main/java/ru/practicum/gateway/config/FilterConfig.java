package ru.practicum.gateway.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<RemoveChunkedTransferEncodingFilter> loggingFilter(){
        FilterRegistrationBean<RemoveChunkedTransferEncodingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RemoveChunkedTransferEncodingFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<TracingFilter> tracingFilter() {
        FilterRegistrationBean<TracingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TracingFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
