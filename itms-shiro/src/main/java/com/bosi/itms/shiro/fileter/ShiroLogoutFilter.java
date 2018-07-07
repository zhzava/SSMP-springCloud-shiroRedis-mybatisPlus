package com.bosi.itms.shiro.fileter;

import com.bosi.itms.shiro.entity.InfoUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;

/**
 * Created by zhz on 2018/7/3.
 * 自定义登出过滤器
 */
public class ShiroLogoutFilter extends LogoutFilter {

    private Cache<String, Deque<Serializable>> cache;
    //设置Cache的key的前缀
    public void setCacheManager(RedisCacheManager cacheManager) {
        this.cache = cacheManager.getCache("");
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        //在这里执行退出系统前需要清空的数据
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        try {
            if(subject.getPrincipal()!=null){
                InfoUser userInfo = (InfoUser) subject.getPrincipal();
                //清除redis缓存
                //Cache<String, Deque<Serializable>> cache = cacheManager.getCache("shiro_redis_cache");
                Session session = subject.getSession();
                Serializable sessionId = session.getId();
                Deque<Serializable> deque = cache.get(userInfo.getUserId());
                if(deque.contains(sessionId)){
                    deque.remove(sessionId);
                    if(deque.size() == 0){
                        cache.remove(userInfo.getUserId());
                    }else{
                        cache.put(userInfo.getUserId(), deque);
                    }
                }
                subject.logout();
            }


        } catch (SessionException ise) {
            ise.printStackTrace();
        }
        issueRedirect(request, response, redirectUrl);
        //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;
    }
}
