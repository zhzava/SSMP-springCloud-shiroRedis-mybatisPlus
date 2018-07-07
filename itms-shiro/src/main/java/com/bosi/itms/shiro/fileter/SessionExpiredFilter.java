package com.bosi.itms.shiro.fileter;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.crazycake.shiro.RedisSessionDAO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Deque;

/**
 * Created by zhz on 2018/7/7.
 * session过期处理，主要是为了自定义控制过期之后的返回提示
 */
public class SessionExpiredFilter extends FormAuthenticationFilter {

    private RedisSessionDAO sessionDao;

    private Cache<String, Deque<Serializable>> cache;

    public void setRedisSessionDAO(RedisSessionDAO dao){
        this.sessionDao = dao;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                // 放行 allow them to see the login page ;)
                return true;
            }
        } else {
            HttpServletRequest httpRequest = WebUtils.toHttp(request);

            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
                //ajax的sesson处理
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.sendError(403);
                httpServletResponse.setHeader("content-type", "text/html;charset=UTF-8");
                httpServletResponse.getWriter().write("session过期");
                return false;

            } else {
                saveRequestAndRedirectToLogin(request, response);
            }

            return false;
        }
    }
}
