package com.bosi.itms.shiro.controller;

import com.bosi.itms.shiro.config.MyShiroRealm;
import com.bosi.itms.shiro.entity.InfoUser;
import com.bosi.itms.shiro.utils.R;
import com.bosi.itms.shiro.utils.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhz on 2018/7/3.
 * 用户控制器
 */
@RestController
@RequestMapping("/sysUser")
@Api("用户信息")
public class SysUserController {

    @GetMapping("/loginUser")
    @ApiOperation(value = "获取当前登录用户信息")
    //@RequiresRoles(value = {"admin"},logical = Logical.OR)
    @RequiresPermissions(value = {"Frm"},logical = Logical.OR)
    public R loginUser() {
        InfoUser infoUser = new InfoUser();
        infoUser = (InfoUser) SecurityUtils.getSubject().getPrincipal();
        return R.ok(infoUser);
    }

    @GetMapping("/updateInfoUser")
    @ApiOperation(value = "更新用户信息")
    //@RequiresRoles(value = {"admin"},logical = Logical.OR)
    public R updateInfoUser(InfoUser InfoUserNew) {
        InfoUser infoUser = new InfoUser();
        ShiroUtils.reloadAuthorizing();
        infoUser = (InfoUser) SecurityUtils.getSubject().getPrincipal();
        /*RealmSecurityManager rsm = (RealmSecurityManager)SecurityUtils.getSecurityManager();
        MyShiroRealm authRealm = (MyShiroRealm)rsm.getRealms().iterator().next();
        authRealm.clearCachedAuth();*/
        return R.ok(infoUser);
    }

    @GetMapping("/updatePerms")
    @ApiOperation(value = "更新权限信息")
    //@RequiresRoles(value = {"admin"},logical = Logical.OR)
    public R updatePerms() {
        InfoUser infoUser = new InfoUser();
        infoUser = (InfoUser) SecurityUtils.getSubject().getPrincipal();
        //ShiroUtils.reloadAuthorizing(infoUser.getUserId());
        RealmSecurityManager rsm = (RealmSecurityManager)SecurityUtils.getSecurityManager();
        MyShiroRealm authRealm = (MyShiroRealm)rsm.getRealms().iterator().next();
        authRealm.clearAuthorizationInfo();
        return R.ok(infoUser);
    }

}
