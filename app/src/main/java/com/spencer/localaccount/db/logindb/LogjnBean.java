package com.spencer.localaccount.db.logindb;

/**
 * Created by hasee on 2019/3/26.
 */

public class LogjnBean {
    private String account;//账户名
    private String password;//密码
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
