package com.spencer.localaccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.spencer.localaccount.db.logindb.LogjnBean;
import com.spencer.localaccount.db.logindb.LogjnDBManager;
import com.spencer.localaccount.utils.CommonUtils;
import com.spencer.localaccount.utils.GV;
import com.spencer.localaccount.utils.IntentCode;


public class LoginView extends Activity {
    private LogjnDBManager logjnDBManager;
    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private TextView btnRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        init();
        initView();
    }

    private void initView() {
        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegist = findViewById(R.id.btn_regist);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegist();
            }
        });

        final CheckBox cbPsw = findViewById(R.id.cb_psw);
        cbPsw.setChecked(false);


        if (cbPsw.isChecked()) {
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        cbPsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (cbPsw.isChecked()) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        etUserName.setText(GV.getUserName(LoginView.this));
    }

    private void init() {
        logjnDBManager = new LogjnDBManager(this);
    }

    private void login() {
        String username = String.valueOf(etUserName.getText());
        String password = String.valueOf(etPassword.getText());
        if (CommonUtils.isEmptyString(username) || CommonUtils.isEmptyString(password)) {
            CommonUtils.showToast(LoginView.this, "账户和密码不能为空");
            return;
        }
        if (!logjnDBManager.isExist(username)) {
            CommonUtils.showToast(LoginView.this, "账户不存在,请先注册后使用");
            return;
        }

        LogjnBean logjnBean = logjnDBManager.query(username);
        if (password.equals(logjnBean.getPassword())) {
            GV.setUserName(LoginView.this, username);
            gotoMain();
        } else {
            CommonUtils.showToast(LoginView.this, "密码错误");
        }
    }

    private void gotoMain() {
        Intent intent = new Intent(LoginView.this, MainView.class);
        startActivityForResult(intent, IntentCode.INTENT_REGIST);
    }

    private void gotoRegist() {
        Intent intent = new Intent(LoginView.this, RegistView.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {

        etPassword.setText("");

        super.onStop();
    }

    @Override
    protected void onPause() {

        etPassword.setText("");

        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentCode.INTENT_REGIST && resultCode == RESULT_OK) {
            etUserName.setText(GV.getUserName(LoginView.this));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
