package com.bosi.itms.shiro.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bosi.itms.shiro.dao.InfoUserDao;
import com.bosi.itms.shiro.dao.UserInfoDao;
import com.bosi.itms.shiro.entity.InfoUser;
import com.bosi.itms.shiro.entity.UserInfo;
import com.bosi.itms.shiro.service.InfoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhz on 2018/7/2.
 */
@Service
public class InfoUserServiceImpl extends ServiceImpl<InfoUserDao, InfoUser> implements InfoUserService {

}
