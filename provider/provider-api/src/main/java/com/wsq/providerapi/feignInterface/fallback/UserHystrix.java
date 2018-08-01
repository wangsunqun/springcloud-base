package com.wsq.providerapi.feignInterface.fallback;

import com.wsq.providerapi.dto.UserDto;
import com.wsq.providerapi.feignInterface.UserInterface;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("fallback/user")
public class UserHystrix implements UserInterface {

    @Override
    public UserDto getUserById(long id) {
        UserDto dto = new UserDto();
        dto.setUserName("fallback");
        return dto;
    }
}
