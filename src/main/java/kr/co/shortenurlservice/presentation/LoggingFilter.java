package kr.co.shortenurlservice.presentation;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest httpServletRequest){
            CachingRequestWrapper cachingRequestWrapper = new CachingRequestWrapper(httpServletRequest);

            String url = cachingRequestWrapper.getRequestURI();
            String method = cachingRequestWrapper.getMethod();
            String body = cachingRequestWrapper.getReader().lines().reduce("", String::concat);

            log.trace("Incoming Request: URL={}, Method={}, Body={}", url, method, body);
            filterChain.doFilter(cachingRequestWrapper, servletResponse);
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

}
