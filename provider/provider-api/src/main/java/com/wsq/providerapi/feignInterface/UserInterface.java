package com.wsq.providerapi.feignInterface;

import com.wsq.providerapi.dto.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider")
@RequestMapping("user")
public interface UserInterface {

    @RequestMapping("getUserById")
    UserDto getUserById(@RequestParam("id")long id);
}
