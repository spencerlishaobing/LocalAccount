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
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView btn_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        init();
        initView();
    }

    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_regist = findViewById(R.id.btn_regist);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegist();
            }
        });

        final CheckBox cbPsw = findViewById(R.id.cb_psw);
        cbPsw.setChecked(false);


        if (cbPsw.isChecked()) {
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        cbPsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (cbPsw.isChecked()) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        et_username.setText(GV.getUserName(LoginView.this));
    }

    private void init() {
        logjnDBManager = new LogjnDBManager(this);
    }

    private void login() {
        String username = String.valueOf(et_username.getText());
        String password = String.valueOf(et_password.getText());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentCode.INTENT_REGIST && resultCode == RESULT_OK) {
            et_username.setText(GV.getUserName(LoginView.this));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
