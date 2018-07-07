package com.bosi.itms.shiro.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bosi.itms.shiro.entity.InfoRole;
import com.bosi.itms.shiro.entity.InfoUser;
import com.bosi.itms.shiro.entity.UserInfo;

import javax.management.relation.Role;
import java.util.List;

public interface InfoUserDao extends BaseMapper<InfoUser> {

    List<InfoRole> selectRoleByUser(InfoUser infoUser);

}