# SSMP-springCloud-shiroRedis-mybatisPlus
一个独立的用户登录管理模块

## 模块应用
用户登录管理几乎是所有网络应用中最重要的模块之一，由于公司各个业务模块相互独立，因此将用户登录提取为一个通用的子模块，
各个业务模块调用都可以通过该模块进行登录

## 应用架构介绍
使用shiro进去用户登录授权验证，通过shiro-redis存储shiro维护的session；<br>
在数据访问层使用的是mybatis，并且集成了mybatis-plus；使用plus通用的crud可以节省我们写基本增删改查的时间，提高开发效率；<br>
使用spring-cloud主要的作用是通过consul作为服务注册中心，对所有模块的api进行统一的治理，
通过feign声明接口，转发请求，使各个子模块内部api可以相互调用，也使得通用的api有更直观的展示，更方便维护和管理；

## 功能介绍
* 统一登录
* shiro动态更新用户信息，权限信息
* 用户密码加密自定义
* session过期处理
* 同一用户同时在线设置(多点登录)，超过踢出用户
* 全局异常处理，统一返回json对象