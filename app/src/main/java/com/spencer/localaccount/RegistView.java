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
import android.widget.ImageButton;
import android.widget.TextView;

import com.spencer.localaccount.db.logindb.LogjnBean;
import com.spencer.localaccount.db.logindb.LogjnDBManager;
import com.spencer.localaccount.utils.CommonUtils;
import com.spencer.localaccount.utils.GV;

public class RegistView extends Activity {

    private LogjnDBManager logjnDBManager;
    private EditText et_username;
    private EditText et_password;
    private EditText et_email;
    private Button btn_regist;
    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_view);
        init();
        initView();
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        ibBack = (ImageButton) findViewById(R.id.ib_back);

        btn_regist = (Button) findViewById(R.id.btn_regist);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist();
            }
        });

        final CheckBox cbPsw = (CheckBox) findViewById(R.id.cb_psw);
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
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        logjnDBManager = new LogjnDBManager(this);
    }

    private void regist() {
        String username = String.valueOf(et_username.getText());
        String password = String.valueOf(et_password.getText());
        String email = String.valueOf(et_email.getText());
        if (CommonUtils.isEmptyString(username) || CommonUtils.isEmptyString(password)) {
            CommonUtils.showToast(RegistView.this, "账户,邮箱和密码不能为空");
            return;
        }
        if (logjnDBManager.isExist(username)) {
            CommonUtils.showToast(RegistView.this, "该账户已存在");
            return;
        }

        if (!CommonUtils.checkEmaile(email)) {
            CommonUtils.showToast(RegistView.this, "邮箱格式不正确");
            return;
        }
        CommonUtils.showToast(RegistView.this, "注册成功");
        LogjnBean logjnBean = new LogjnBean();
        logjnBean.setAccount(username);
        logjnBean.setPassword(password);
        logjnBean.setEmail(email);
        logjnDBManager.insert(logjnBean);
        GV.setUserName(this, username);

        setResult(RESULT_OK);
        finish();
    }


}
