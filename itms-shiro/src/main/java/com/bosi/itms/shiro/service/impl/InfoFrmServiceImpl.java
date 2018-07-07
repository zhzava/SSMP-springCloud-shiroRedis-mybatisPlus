package com.bosi.itms.shiro.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bosi.itms.shiro.dao.InfoFrmDao;
import com.bosi.itms.shiro.dao.InfoUserDao;
import com.bosi.itms.shiro.entity.InfoFrm;
import com.bosi.itms.shiro.entity.InfoUser;
import com.bosi.itms.shiro.service.InfoFrmService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhz on 2018/7/2.
 */
@Service
public class InfoFrmServiceImpl extends ServiceImpl<InfoFrmDao, InfoFrm> implements InfoFrmService {

    @Override
    public List<InfoFrm> selectPermByUser(InfoUser infoUser) throws Exception {
        return baseMapper.selectPermByUser(infoUser);
    }
}
