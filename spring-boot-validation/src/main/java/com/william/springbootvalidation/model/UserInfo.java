package com.william.springbootvalidation.model;

import javax.validation.constraints.NotNull;

/**
 * @Auther: williamdream
 */

public class UserInfo {

    @NotNull(message = "userid不能为空",groups = {UserUpdate.class})
    private String userid;

    @NotNull(message = "用户名不能为空",groups = {UserInster.class,UserUpdate.class})
    private String name;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
