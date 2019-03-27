package com.spencer.localaccount.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.spencer.localaccount.R;
import com.spencer.localaccount.utils.CommonUtils;
import com.spencer.localaccount.utils.DensityUtil;
import com.spencer.localaccount.utils.Tools;

/**
 * Created by hasee on 2019/3/27.
 */

public class DialogView extends Dialog {

    private Context context;
    private String titleStr;
    private String desStr;
    private View viewContent;

    private TextView tv_des;
    private TextView tv_content;
    private String okStr = null;
    private String cancelStr = null;
    private String customStr = null;

    private float customHeight = 0; // 自定义高度
    private int backgroundResId = -1;

    private String tag;

    public DialogView(Context context, int layoutResId) {
        this(context, layoutResId, false);
    }

    public DialogView(Context context, int layoutResId, boolean showOnBottom) {
        this(context, showOnBottom);
        if (layoutResId != -1) {
            this.layoutResId = layoutResId;
        }
    }

    public DialogView(Context context) {
        this(context, false);
    }

    private boolean showOnBottom = false;

    public DialogView(Context context, boolean showOnBottom) {
        super(context, R.style.dialog);
        this.context = context;
        this.showOnBottom = showOnBottom;
        setCancelable(true);

        okStr = "确定";
        cancelStr ="取消";

        Window window = getWindow();
        if(window == null){
            return;
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (showOnBottom) {
            window.getDecorView().setPadding(0, 0, 0, 0);
        }

        window.setWindowAnimations(R.style.mystyle);
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics mDm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(mDm);
        int globalHeight = mDm.heightPixels;
        int stateBarHeight = DensityUtil.dip2px(getContext(), 20);
        try {
            stateBarHeight = Tools.getStateBarHeight(context);
        } catch (Exception e) {
            e.printStackTrace();
        }// 状态栏高度

        lp.height = globalHeight - stateBarHeight - DensityUtil.dip2px(getContext(), 100);
        if (showOnBottom) {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.BOTTOM;
        }

        getWindow().setAttributes(lp);
    }

    private boolean isDismissCustom = true;

    private boolean isDismissOK = true;

    public void dismissdialog(boolean isDismissOK) {
        this.isDismissOK = isDismissOK;
    }

    public void dismissdialogCustom(boolean isDismissCustom) {
        this.isDismissCustom = isDismissCustom;
    }

    private int layoutResId = R.layout.ui_dialogview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResId);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        RelativeLayout rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        tv_des = (TextView) findViewById(R.id.tv_des);
        if (showOnBottom && tv_des.getParent() != null && tv_des.getParent() instanceof LinearLayout) {
            try {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ((LinearLayout) tv_des.getParent()).getLayoutParams();
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (backgroundResId != -1) {
            rl_content.setBackgroundResource(backgroundResId);
        }
        View view_line1 = findViewById(R.id.view_line1);
        View view_line2 = findViewById(R.id.view_line2);
        View view_line_bottom = findViewById(R.id.view_line_bottom);
        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        Button btn_custom = (Button) findViewById(R.id.btn_custom);
        LinearLayout ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isDismissOK) {
                    dismiss();
                }
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.OnOKClickListener(v);
                }

                if(mOnDialogViewButtonClickListener != null){
                    mOnDialogViewButtonClickListener.onOKClick(v);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.OnCancelClickListener(v);
                }

                if(mOnDialogViewButtonClickListener != null){
                    mOnDialogViewButtonClickListener.onCancelClick(v);
                }
            }
        });

        btn_custom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isDismissCustom) {
                    dismiss();
                }
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.OnCustomClickListener(v);
                }
                if(mOnDialogViewButtonClickListener != null){
                    mOnDialogViewButtonClickListener.onCustomClick(v);
                }
            }
        });

        if (titleStr != null) {
            tv_title.setText(titleStr);
        }

        if (desStr != null) {
            tv_des.setVisibility(View.VISIBLE);
            tv_des.setText(desStr);
        } else {
            tv_des.setVisibility(View.GONE);
        }

        if (okStr != null) {
            btn_ok.setText(okStr);
        } else {
            btn_ok.setVisibility(View.GONE);
        }

        if (cancelStr != null) {
            btn_cancel.setText(cancelStr);
        } else {
            btn_cancel.setVisibility(View.GONE);
        }

        if (customStr != null) {
            btn_custom.setText(customStr);
            btn_custom.setVisibility(View.VISIBLE);
        } else {
            btn_custom.setVisibility(View.GONE);
        }

        //控制分割线的显示
        if (customStr == null) {
            if (okStr == null || cancelStr == null) {
                view_line1.setVisibility(View.GONE);
                view_line2.setVisibility(View.GONE);
            } else {
                view_line1.setVisibility(View.VISIBLE);
                view_line2.setVisibility(View.GONE);
            }
        } else {
            view_line1.setVisibility(okStr == null ? View.GONE : View.VISIBLE);
            view_line2.setVisibility(cancelStr == null ? View.GONE : View.VISIBLE);
        }

        if (okStr == null && cancelStr == null && customStr == null) {
            ll_btn.setVisibility(View.GONE);
            if (view_line_bottom != null) {
                view_line_bottom.setVisibility(View.GONE);
            }
        } else {
            if (view_line_bottom != null) {
                view_line_bottom.setVisibility(View.VISIBLE);
            }
            ll_btn.setVisibility(View.VISIBLE);
        }

        if (viewContent != null) {
            ViewGroup.LayoutParams rlp = null;
            if (customHeight != 0) {
                rlp = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context,customHeight)));
            } else {
                rlp = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            rl_content.addView(viewContent, rlp);
        } else {
            if (tv_content != null) {
                ScrollView scrollView = new ScrollView(context);
                scrollView.setFadingEdgeLength(0);
                ViewGroup.LayoutParams slp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                scrollView.addView(tv_content, slp);
                ViewGroup.LayoutParams rlp = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rl_content.addView(scrollView, rlp);
            }
        }
    }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setDes(String des) {
        desStr = des;
    }

    public void setDesColor(int color) {
        tv_des.setTextColor(color);
    }

    public void setBackgroundResource(int resId) {
        backgroundResId = resId;
    }

    public void setOk(String okStr) {
        this.okStr = okStr;
    }

    public void setCancel(String cancelStr) {
        this.cancelStr = cancelStr;
    }

    public void setCustomStr(String customStr) {
        this.customStr = customStr;
    }

    public void setcontent(View content, float customHeigt) {
        this.customHeight = customHeigt;
        if (content != null) {
            viewContent = content;
        }
    }

    public void setcontent(View content) {
        if (content != null) {
            viewContent = content;
        }
    }

    public void setMessage(String msg, boolean isHtml) {
        tv_content = new TextView(context);
        if (isHtml) {
            try {
                tv_content.setText(Html.fromHtml(msg));
            } catch (Exception e) {
                tv_content.setText(CommonUtils.getNotEmptyString(msg));
            }
        } else {
            tv_content.setTextColor(getContext().getResources().getColor(R.color.normalcolor));
            tv_content.setText(CommonUtils.getNotEmptyString(msg));
        }
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    }

    public void setMessage(String msg) {
        setMessage(msg, false);
    }

    public void setMessage(String msg, int gravity) {
        setMessage(msg, false);
        tv_content.setGravity(gravity);
    }

    public void setOnShowListener(OnShowListener listener) {
        super.setOnShowListener(listener);
    }

    private OnDialogClickListener mOnDialogClickListener = null;


    /**
     * 已废弃，请使用setDialogViewClickButtonListener方法
     */
    @Deprecated
    public void AddDialogClickListener(
            OnDialogClickListener mOnDialogClickListener) {
        this.mOnDialogClickListener = mOnDialogClickListener;
    }

    private OnDialogViewButtonClickListener mOnDialogViewButtonClickListener = null;
    public void setDialogViewClickButtonListener(OnDialogViewButtonClickListener mOnDialogViewButtonClickListener){
        this.mOnDialogViewButtonClickListener = mOnDialogViewButtonClickListener;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setSystemLevel() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }
}

