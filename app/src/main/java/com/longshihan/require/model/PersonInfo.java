package com.longshihan.require.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 用户个人的配置信息
 * @author Administrator
 * @time 2016/8/1 15:27
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class PersonInfo extends BmobObject implements Serializable{
    private static final long serialVersionUID = 3898029242467207417L;
    private String name;
    private String phone;
    private String address;
    private String nick_name;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
