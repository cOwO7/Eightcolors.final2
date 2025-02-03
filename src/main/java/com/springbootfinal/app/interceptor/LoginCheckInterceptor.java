package com.springbootfinal.app.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("##### LoginCheckInterceptor - preHandle() #####");
        HttpSession session = request.getSession();

        if (session.getAttribute("isLogin") == null) {
            session.setAttribute("loginMsg", "로그인이 필요한 서비스");

            // JavaScript를 이용해 alert 창을 띄운 후 로그인 페이지로 이동
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('로그인이 필요한 서비스입니다. 로그인 페이지로 이동합니다.');");
            out.println("location.href='/login';");
            out.println("</script>");
            out.flush();

            return false; // 중요! 여기서 false를 반환해야 이후 코드가 실행되지 않음
        }

        session.removeAttribute("loginMsg");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        log.info("##### LoginCheckInterceptor - postHandle #####");
        response.setHeader("Cache-Control", "no-cache");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        log.info("##### LoginCheckInterceptor - afterCompletion() #####");
    }
}
