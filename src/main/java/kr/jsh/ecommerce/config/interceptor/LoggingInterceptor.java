package kr.jsh.ecommerce.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Request URL: {}", request.getRequestURL());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("Client IP: {}", request.getRemoteAddr());
        log.info("Request Params: {}", request.getQueryString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Response Status: {}", response.getStatus());
        if (ex != null) {
            log.error("Exception: ", ex);
        }
    }
}

