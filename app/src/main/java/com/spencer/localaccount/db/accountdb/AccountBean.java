package com.spencer.localaccount.db.accountdb;

import android.widget.Button;

import java.io.Serializable;

/**
 * Created by hasee on 2019/3/26.
 */

public class AccountBean implements Serializable{
    private boolean operation;
    private String userName;//本APP登录名
    private String account;//账户米
    private String password;//密码
    private String description;//描述
    private String remark;//备注
    private String email;//邮箱
    private String phone;//电话号码


    public boolean isOperation() {
        return operation;
    }

    public void setOperation(boolean operation) {
        this.operation = operation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
