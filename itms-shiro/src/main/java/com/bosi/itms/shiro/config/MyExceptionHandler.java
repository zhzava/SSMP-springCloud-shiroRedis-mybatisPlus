package com.bosi.itms.shiro.config;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhz on 2018/7/3.
 * 全局异常处理
 */
public class MyExceptionHandler implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        ModelAndView mv = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<String, Object>();
        if (ex instanceof UnauthenticatedException) {
            attributes.put("code", 1000001);
            attributes.put("msg", "token错误");
        } else if (ex instanceof UnauthorizedException) {
            attributes.put("code", 1000002);
            attributes.put("msg", "用户无权限");
        } else if ( ex instanceof AuthorizationException) {
            attributes.put("code", 1000003);
            attributes.put("msg", "用户信息已修改，请重新登录");
        } else {
            attributes.put("code", 1000004);
            attributes.put("msg", ex.getMessage());
        }
        attributes.put("type","fail");
        view.setAttributesMap(attributes);
        mv.setView(view);
        return mv;
    }
}
