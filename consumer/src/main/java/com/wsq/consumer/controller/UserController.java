package com.wsq.consumer.controller;

import com.wsq.providerapi.dto.UserDto;
import com.wsq.providerapi.feignInterface.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class UserController {
    @Autowired
    private UserInterface userInterface;

    @RequestMapping("test")
    public UserDto test(@RequestParam("id") long id){
//        return userInterface.getUserById(id);
        return new UserDto(1, "111222333");
    }
}
