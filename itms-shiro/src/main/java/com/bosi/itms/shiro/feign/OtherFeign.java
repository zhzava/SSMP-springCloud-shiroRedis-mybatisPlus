package com.bosi.itms.shiro.feign;

import com.bosi.itms.api.OtherApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("${provider}")
public interface OtherFeign extends OtherApi {

}