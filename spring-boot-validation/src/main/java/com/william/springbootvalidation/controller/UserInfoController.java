package com.william.springbootvalidation.controller;

import com.william.springbootvalidation.model.UserInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: chaiz
 * @Date: 2019/10/28 10:46
 * @Description:
 */
@RequestMapping("/user")
@RestController
public class UserInfoController {

    @PostMapping("/add")
    public String addUser(@RequestBody @Validated({UserInfo.Default.class})UserInfo userInfo){
        System.out.println("名字"+userInfo.getName());
        System.out.println("新增");
        return "";
    }

    @PostMapping("/update")
    public String updateUser(@RequestBody @Validated({UserInfo.Default.class, UserInfo.UserUpdate.class})UserInfo userInfo){
        System.out.println("修改");
        return "";
    }

}
