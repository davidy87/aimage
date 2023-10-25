package com.aimage.util.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.aimage.enums.SessionAttribute.*;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(LOGIN_USER.getName()) == null) {
            log.info("Unauthorized user request = {}", session);
            response.sendRedirect("/?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }

}
