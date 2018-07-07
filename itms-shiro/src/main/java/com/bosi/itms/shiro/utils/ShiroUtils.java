package com.bosi.itms.shiro.utils;

import com.bosi.itms.shiro.config.MyShiroRealm;
import com.bosi.itms.shiro.entity.InfoUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * Created by zhz on 2018/7/5.
 */
public class ShiroUtils {
    /**
     * 更新保存在shiro中的用户信息，信息如果有变化，权限也会被重新更新
     */
    public static void reloadAuthorizing() {

        //重新修改权限后清楚缓存，调用doGetAuthorizationInfo重新取角色的权限信息
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        //MyShiroRealm shiroRealm = (MyShiroRealm)rsm.getRealms().iterator().next();
        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        InfoUser shiroUser = (InfoUser)subject.getPrincipal();
        //第一个参数为用户名,第二个参数为realmName,test想要操作权限的用户
        shiroUser.setUserName("新管理1");
        SimplePrincipalCollection principals = new SimplePrincipalCollection(shiroUser,realmName);
        subject.runAs(principals);
    }

    /**
     * 清空所有关联认证
     */
    public void clearAllCachedAuthorizationInfo2(MyShiroRealm myRealm,String sessionId) {
        Cache<Object, AuthorizationInfo> cache = myRealm.getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                System.out.println(key + ":" + key.toString());
                cache.remove(key);
            }
        }
        cache.remove(sessionId);
    }
}
