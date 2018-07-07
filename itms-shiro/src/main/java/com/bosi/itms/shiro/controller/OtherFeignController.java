package com.bosi.itms.shiro.controller;

import com.bosi.itms.model.Test;
import com.bosi.itms.shiro.service.OtherFeignService;
import com.bosi.itms.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/myFeign")
@Api("服务消费者借口")
public class OtherFeignController {

    @Resource
    private OtherFeignService feignService;

    @ApiOperation("获取测试信息")
    @GetMapping("/getTestInfo/{testId}")
    public ResponseVo<Test> getTestInfo(@PathVariable Long testId) {
        Test test = feignService.getTestInfo(testId).getData();
        return new ResponseVo<>(test);
    }

}

