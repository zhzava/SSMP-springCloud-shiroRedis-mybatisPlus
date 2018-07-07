package com.bosi.itms.shiro.config;

import com.bosi.itms.shiro.utils.MD5;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * Created by zhz on 2018/7/3.
 * shiro密码加密自定义
 */
public class ShiroLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        UsernamePasswordToken autoken = (UsernamePasswordToken) token;
        SimpleAuthenticationInfo sinfo = (SimpleAuthenticationInfo)info;
        //String pwdhash = new String(sinfo.getCredentialsSalt().getBytes());
        //这个CipherUtil.generatePassword是自定义的static方法，用于生成加密后的密码
        String inputCredential = new MD5().getMD5ofStr(String.valueOf(autoken.getPassword()));
        //生成的加密是大写，但mysql不区分大小写，对比会失败

        String accountCredentials = String.valueOf(getCredentials(info)).toUpperCase();
        boolean match = equals(inputCredential,accountCredentials);
        if (match) {
            //passwordRetryCache.remove(username);
        }
        return match;
    }
}
