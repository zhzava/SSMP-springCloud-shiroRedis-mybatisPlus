package com.bosi.itms.shiro.controller;

import com.alibaba.fastjson.JSONObject;
import com.bosi.itms.shiro.entity.InfoUser;
import com.bosi.itms.shiro.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by zhz on 2018/7/4.
 * 登录控制器
 */
@Controller
@Api("登录")
public class ShiroController {

    //注入缓存管理器
    @Autowired
    private RedisCacheManager cacheManager;

    /**
     * 登录方法
     * @param userInfo
     * @return
     */
    @ApiOperation(value = "ajax登录",notes="登录")
    @RequestMapping(value = "/ajaxLogin", method = RequestMethod.POST)
    @ResponseBody
    public R ajaxLogin(InfoUser userInfo) {
        JSONObject jsonObject = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userInfo.getUserId(), userInfo.getUserPwd());
        try {
            subject.login(token);
            //登录成功缓存再保存用户名和sessionId，用于做唯一登录
            Cache<String, Deque<Serializable>> cache = cacheManager.getCache("shiro_redis_cache");
            Session session = subject.getSession();
            Serializable sessionId = session.getId();
            Deque<Serializable> deque = cache.get(userInfo.getUserId());
            if(deque == null){
                deque = new LinkedList<>();
            }
            if(!deque.contains(sessionId)){
                deque.push(sessionId);
                cache.put(userInfo.getUserId(), deque);
                jsonObject.put("token", subject.getSession().getId());
                jsonObject.put("msg", "登录成功");
                return R.ok(jsonObject);
            }else{
                return R.error("请不要重复登录");
            }
        } catch (IncorrectCredentialsException e) {
            return R.error("密码错误");
        } catch (LockedAccountException e) {
            return R.error("登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            return R.error("该用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(jsonObject.toString());
    }

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return
     */
    @RequestMapping(value = "/unauth")
    @ResponseBody
    public Object unauth() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 1000000);
        map.put("msg", "未登录");
        map.put("type","fail");
        return map;
    }
}
