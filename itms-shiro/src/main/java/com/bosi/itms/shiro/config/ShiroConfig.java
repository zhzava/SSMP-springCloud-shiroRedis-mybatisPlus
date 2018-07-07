package com.bosi.itms.shiro.config;

import com.bosi.itms.shiro.fileter.KickoutFilter;
import com.bosi.itms.shiro.fileter.SessionExpiredFilter;
import com.bosi.itms.shiro.fileter.ShiroLogoutFilter;
import com.bosi.itms.shiro.listener.MySessionListener;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.Filter;
import java.util.*;

/**
 * Created by zhz on 2018/7/3.
 * shiro配置
 */
@Configuration
public class ShiroConfig {

    @Value("${shiro.session.timeout}")
    private String sessionTime;

    @Value("${shiro.online.max}")
    private String onlineMax;

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        //注意过滤器配置顺序 不能颠倒
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/logout", "logout,kickout");
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/ajaxLogin", "anon");

        //配置swagger相关链接
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**/**", "anon");
        filterChainDefinitionMap.put("/swagger/**","anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/webjars/**/**", "anon");

        //配置自定义的踢出链接
        filterChainDefinitionMap.put("/kickout", "anon");

        //放开consul健康检查的链接，如果拦截了，shiro默认会做登录操作，导致redis多了很多无意义记录
        filterChainDefinitionMap.put("/health", "anon");
        filterChainDefinitionMap.put("/actuator/health", "anon");

        //过滤器会根据顺序依次执行，位置的先后根据具体逻辑设置
        filterChainDefinitionMap.put("/**", "sessionexp,kickout,authc");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/unauth");

        Map<String,Filter> filterMap = new HashMap<String,Filter>();
        //设置自定义登出过滤器
        filterMap.put("logout",shiroLogoutFilter());
        //设置踢出过滤器，限制同一帐号同时在线的个数。
        filterMap.put("kickout", kickoutFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        //设置session过期过滤器
        filterMap.put("sessionexp", sessionExpiredFilter());

        shiroFilterFactoryBean.setFilters(filterMap);
        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 登出过滤器
     * @return
     */
    public ShiroLogoutFilter shiroLogoutFilter(){
        ShiroLogoutFilter shiroLogoutFilter = new ShiroLogoutFilter();
        shiroLogoutFilter.setCacheManager(cacheManager());
        return shiroLogoutFilter;
    }

    public SessionExpiredFilter sessionExpiredFilter (){
        SessionExpiredFilter sessionExpiredFilter = new SessionExpiredFilter();
        return sessionExpiredFilter;
    }

    /**
     * 限制同一账号登录同时登录人数控制
     * @return
     */
    public KickoutFilter kickoutFilter (){
        KickoutFilter kickoutFilter = new KickoutFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutFilter.setSessionManager(sessionManager());
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickoutFilter.setCacheManager(cacheManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutFilter.setMaxSession(Integer.valueOf(onlineMax));
        //被踢出后重定向到的地址；
        kickoutFilter.setKickoutUrl("/kickout");
        return kickoutFilter;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     *
     * @return
     */
    //@Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        //加载自定义加密类
        HashedCredentialsMatcher hashedCredentialsMatcher = new ShiroLimitHashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("");//md5为散列算法:这里使用内置MD5算法，此处设空;
        //hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    //自定义sessionManager
    @Bean
    public SessionManager sessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        //shiro session超时时间
        mySessionManager.setGlobalSessionTimeout(Integer.valueOf(sessionTime)*1000L);
        mySessionManager.setSessionDAO(redisSessionDAO());
        mySessionManager.setSessionListeners(listeners());
        return mySessionManager;
    }

    /**
     * 自定义session监听，监听过期处理，主要是删除redis中的缓存
     * @return
     */
    public Collection<SessionListener> listeners() {
        Collection<SessionListener> listeners = new ArrayList();
        MySessionListener listener = new MySessionListener();
        listener.setRedisSessionDAO(redisSessionDAO());
        listener.setCacheManager(cacheManager());
        listeners.add(listener);
        return listeners;
    }

    /**
     * 配置shiro redisManager
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "redis.shiro")
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        //redisManager.setHost("192.168.0.201");
        //redisManager.setPort(6379);
        //redisManager.setExpire(120000);// 配置缓存过期时间
        //redisManager.setTimeout(3000);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * <p>
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
//      Custom your redis key prefix for session management, if you doesn't define this parameter,
//      shiro-redis will use 'shiro_redis_session:' as default prefix
//      redisSessionDAO.setKeyPrefix("");
        return redisSessionDAO;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 注册全局异常处理
     * @return
     */
    @Bean(name = "exceptionHandler")
    public HandlerExceptionResolver handlerExceptionResolver() {
        return new MyExceptionHandler();
    }
}
