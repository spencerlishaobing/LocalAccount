package com.spencer.localaccount;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.spencer.localaccount.db.accountdb.AccountBean;
import com.spencer.localaccount.db.accountdb.AccountDBManager;
import com.spencer.localaccount.utils.CommonUtils;
import com.spencer.localaccount.utils.GV;

//添加张账户
public class AddAccountView extends Activity {


    private AccountDBManager accountDBManager;
    private EditText et_des;
    private EditText et_account;
    private EditText et_password;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_remark;
    private TextView tv_title;

    private Button btn_add;
    private ImageButton ibBack;
    private boolean isUpdate = false;
    private AccountBean accountBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_view);
        ((TextView) findViewById(R.id.tv_title)).setText("新增账户");
        accountBean = (AccountBean) getIntent().getSerializableExtra("data");
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        if (accountBean == null) {
            accountBean = new AccountBean();
        }
        if (accountDBManager == null) {
            accountDBManager = new AccountDBManager(this);

        }

        initView();

    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        et_des = findViewById(R.id.et_des);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_remark = findViewById(R.id.et_remark);

        btn_add = findViewById(R.id.btn_add);

        if (isUpdate) {
            tv_title.setText("修改账户");
            et_des.setText(accountBean.getDescription());
            et_account.setText(accountBean.getAccount());
            et_password.setText(accountBean.getPassword());
            et_email.setText(accountBean.getEmail());
            et_phone.setText(accountBean.getPhone());
            et_remark.setText(accountBean.getRemark());

        }
        btn_add.setText("提 交");
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealData();

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
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void dealData() {


        String des = String.valueOf(et_des.getText());
        String account = String.valueOf(et_account.getText());
        String password = String.valueOf(et_password.getText());
        String email = String.valueOf(et_email.getText());
        String phone = String.valueOf(et_phone.getText());
        String remark = String.valueOf(et_remark.getText());

        if (TextUtils.isEmpty(des) || TextUtils.isEmpty(password)) {
            CommonUtils.showToast(this, "账户描述，密码为必填项");
            return;
        }

        if (TextUtils.isEmpty(account) && TextUtils.isEmpty(email)) {
            CommonUtils.showToast(this, "账户和邮箱至少填一个");
            return;
        }

        if (!TextUtils.isEmpty(email)&&!CommonUtils.checkEmaile(email)) {
            CommonUtils.showToast(this, "邮箱格式不正确");
            return;
        }

        accountBean.setAccount(account);
        accountBean.setDescription(des);
        accountBean.setPhone(phone);
        accountBean.setEmail(email);
        accountBean.setUserName(GV.getUserName(this));
        accountBean.setRemark(remark);
        accountBean.setPassword(password);


        if (isUpdate) {
            accountDBManager.update(accountBean);
        } else {
            accountDBManager.insert(accountBean);
        }
        CommonUtils.showToast(AddAccountView.this, "操作成功");
        setResult(RESULT_OK);
        finish();
    }


}
