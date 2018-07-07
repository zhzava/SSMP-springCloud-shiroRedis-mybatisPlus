package com.bosi.itms.shiro.service;

import com.baomidou.mybatisplus.service.IService;
import com.bosi.itms.shiro.entity.InfoRole;
import com.bosi.itms.shiro.entity.InfoUser;

import java.util.List;

/**
 * Created by zhz on 2018/7/2.
 */
public interface InfoRoleService extends IService<InfoRole> {

    List<InfoRole> selectRoleByUser(InfoUser infoUser) throws Exception;
}
