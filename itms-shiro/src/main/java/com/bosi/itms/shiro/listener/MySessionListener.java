package com.bosi.itms.shiro.listener;

import com.bosi.itms.shiro.config.MyShiroRealm;
import com.bosi.itms.shiro.entity.InfoUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;

import java.io.Serializable;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by zhz on 2018/7/6.
 * 监听session过期的事件，重写超时之后的操作，主要是为了删除redis中保存的用户登录记录
 */
public class MySessionListener extends SessionListenerAdapter {

    private RedisSessionDAO sessionDao;

    private Cache<String, Deque<Serializable>> cache;

    public void setRedisSessionDAO(RedisSessionDAO dao){
        this.sessionDao = dao;
    }

    //设置Cache的key的前缀
    public void setCacheManager(RedisCacheManager cacheManager) {
        this.cache = cacheManager.getCache("");
    }

    @Override
    public void onExpiration(Session session) {//会话过期时触发，重写会话过期的操作
        Collection<Object> keys = session.getAttributeKeys();
        Object userObjkey = null;
        for(Object obj : keys){
            if(obj.toString().equals("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY")){
                userObjkey = obj;
            }
        }
        SimplePrincipalCollection obj = (SimplePrincipalCollection) session.getAttribute(userObjkey);
        if(obj!=null){
            InfoUser infoUser = (InfoUser) obj.getPrimaryPrincipal();
            //清除缓存并退出登录
            Serializable sessionId = session.getId();
            Deque<Serializable> deque = cache.get(infoUser.getUserId());
            if(deque == null){
                deque = new LinkedList<>();
            }
            if(deque.contains(sessionId)) {
                deque.remove(sessionId);
                if(deque.size() == 0){
                    cache.remove(infoUser.getUserId());
                }else{
                    cache.put(infoUser.getUserId(), deque);
                }
            }
        }

    }
}
