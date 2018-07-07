package com.bosi.itms.shiro.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bosi.itms.shiro.entity.InfoFrm;
import com.bosi.itms.shiro.entity.InfoUser;

import java.util.List;

public interface InfoFrmDao extends BaseMapper<InfoFrm> {
    List<InfoFrm> selectPermByUser(InfoUser infoUser);
}