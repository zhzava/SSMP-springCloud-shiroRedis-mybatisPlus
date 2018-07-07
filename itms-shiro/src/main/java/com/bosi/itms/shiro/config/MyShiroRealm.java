package com.bosi.itms.shiro.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bosi.itms.shiro.entity.*;
import com.bosi.itms.shiro.service.InfoFrmService;
import com.bosi.itms.shiro.service.InfoRoleService;
import com.bosi.itms.shiro.service.InfoUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Deque;
import java.util.List;

/**
 * 自定义权限匹配和账号密码匹配
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private InfoUserService infoUserService;
    @Resource
    private InfoRoleService infoRoleService;
    @Resource
    private InfoFrmService infoFrmService;

    @Override
    public void setName(String name) {
        super.setName("MyShiroRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        try {
            InfoUser userInfo = (InfoUser) principals.getPrimaryPrincipal();
            List<InfoRole> roles = infoRoleService.selectRoleByUser(userInfo);

            for (InfoRole role : roles) {
                authorizationInfo.addRole(role.getRoleId());
            }
            List<InfoFrm> sysPermissions = infoFrmService.selectPermByUser(userInfo);

            for (InfoFrm perm : sysPermissions) {
                authorizationInfo.addStringPermission(perm.getFrmId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
//        System.out.println(token.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        EntityWrapper<InfoUser> ew = new EntityWrapper<>();
        ew.eq("user_id",username);
        InfoUser userInfo = infoUserService.selectOne(ew);
        if (userInfo == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户信息
                userInfo.getUserPwd(), //密码
                //ByteSource.Util.bytes(""),//salt=username+salt
                getName()  //realm name
        );

        return authenticationInfo;
    }

    /**
     * 清除所有缓存
     */
    public void clearCachedAuth(){
        this.clearCache(SecurityUtils.getSubject().getPrincipals());
        //this.clearCachedAuthenticationInfo(SecurityUtils.getSubject().getPrincipals());
    }

    /*
     * 清除当前用户的权限缓存
     */
    public void clearAuthorizationInfo(){
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
