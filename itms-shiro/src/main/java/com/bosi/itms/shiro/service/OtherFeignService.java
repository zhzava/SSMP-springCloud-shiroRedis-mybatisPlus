package com.bosi.itms.shiro.service;

import com.bosi.itms.model.Test;
import com.bosi.itms.shiro.feign.OtherFeign;
import com.bosi.itms.vo.ResponseVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OtherFeignService {

    @Resource
    private OtherFeign otherFeign;

    public ResponseVo<Test> getTestInfo(Long testId) {
        return otherFeign.getTestInfo(testId);
    }

}
