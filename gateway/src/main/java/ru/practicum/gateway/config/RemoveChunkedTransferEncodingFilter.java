package ru.practicum.gateway.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class RemoveChunkedTransferEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        filterChain.doFilter(request, new HttpServletResponseWrapper(httpServletResponse) {
            @Override
            public void setHeader(String name, String value) {
                if (!"Transfer-Encoding".equalsIgnoreCase(name)) {
                    super.setHeader(name, value);
                }
            }

            @Override
            public void addHeader(String name, String value) {
                if (!"Transfer-Encoding".equalsIgnoreCase(name)) {
                    super.addHeader(name, value);
                }
            }
        });
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
