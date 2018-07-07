package com.bosi.itms.shiro.service;

import com.baomidou.mybatisplus.service.IService;
import com.bosi.itms.shiro.entity.InfoFrm;
import com.bosi.itms.shiro.entity.InfoUser;

import java.util.List;

/**
 * Created by zhz on 2018/7/2.
 */
public interface InfoFrmService extends IService<InfoFrm> {
    List<InfoFrm> selectPermByUser(InfoUser infoUser) throws Exception;
}
