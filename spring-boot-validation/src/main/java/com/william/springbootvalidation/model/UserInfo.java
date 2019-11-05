package com.william.springbootvalidation.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: williamdream
 */

public class UserInfo {

    public interface Default{}

    public interface UserUpdate{}


    @NotNull(message = "userid不能为空",groups = UserUpdate.class)
    private int userid;

    @NotBlank(message = "用户名不能为空",groups = Default.class)
    @Length(min=2,max=20,message = "用户名长度{min}-{max}之间",groups = Default.class)
    private String name;

    @Min(message = "用户年龄不能小于18岁",value = 18,groups = Default.class)
    @NotNull(message = "用户名年龄为空",groups = Default.class)
    private int age;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
