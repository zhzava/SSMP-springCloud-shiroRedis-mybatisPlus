package com.bosi.itms.shiro.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bosi.itms.shiro.dao.InfoRoleDao;
import com.bosi.itms.shiro.dao.InfoUserDao;
import com.bosi.itms.shiro.entity.InfoRole;
import com.bosi.itms.shiro.entity.InfoUser;
import com.bosi.itms.shiro.service.InfoRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.relation.Role;
import java.util.List;

/**
 * Created by zhz on 2018/7/2.
 */
@Service
public class InfoRoleServiceImpl extends ServiceImpl<InfoRoleDao, InfoRole> implements InfoRoleService {
    @Override
    public List<InfoRole> selectRoleByUser(InfoUser infoUser) throws Exception{
        return baseMapper.selectRoleByUser(infoUser);
    }
}
