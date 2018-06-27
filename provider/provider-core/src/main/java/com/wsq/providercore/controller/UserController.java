package com.wsq.providercore.controller;

import com.wsq.providerapi.dto.UserDto;
import com.wsq.providerapi.feignInterface.UserInterface;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserInterface{
    @Override
    public UserDto getUserById(@RequestParam("id")long id) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setUserName("wang");
        System.out.println("11111111111111111111");
        return dto;
    }
}
