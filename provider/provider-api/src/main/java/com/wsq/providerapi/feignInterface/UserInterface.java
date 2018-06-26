package com.wsq.providerapi.feignInterface;

import com.wsq.providerapi.dto.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("provider")
@RequestMapping("user")
public interface UserInterface {

    @RequestMapping("getUserById")
    UserDto getUserById(@PathVariable("id")long id);
}
