package com.wsq.providercore.controller;

import com.wsq.providerapi.dto.UserDto;
import com.wsq.providerapi.feignInterface.UserInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserInterface{
    @Value("${server.port}")
    private String port;

    @Override
    public UserDto getUserById(@RequestParam("id")long id) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setUserName(port);
        System.out.println("请求进来了");
        try {
            Thread.sleep(20000);
        } catch (Exception e) {}
        return dto;
    }
}
