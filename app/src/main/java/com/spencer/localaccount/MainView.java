package com.spencer.localaccount;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.draw.ImageResDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.spencer.localaccount.db.accountdb.AccountBean;
import com.spencer.localaccount.db.accountdb.AccountDBManager;
import com.spencer.localaccount.excel.ExcelUtils;
import com.spencer.localaccount.utils.CommonUtils;
import com.spencer.localaccount.utils.GV;
import com.spencer.localaccount.utils.IntentCode;
import com.spencer.localaccount.utils.Tools;
import com.spencer.localaccount.widget.DialogView;
import com.spencer.localaccount.widget.OnDialogViewButtonClickListener;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected List<Column> columns = new ArrayList<>();
    protected List<String> col;
    protected SmartTable table;
    protected List<AccountBean> data;
    protected AccountDBManager accountDBManager;
    private Column<Boolean> operation;
    private boolean isSelectedAll = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        table = (SmartTable) findViewById(R.id.table);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                select(isSelectedAll);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initMainView();
    }

    private void initMainView() {
        initTableData();

    }


    private void select(boolean isSelectAll) {
        if (data.size() == 0) {
            return;
        }
        for (int position = 0; position < data.size(); position++) {
            data.get(position).setOperation(isSelectAll);
            operation.getDatas().set(position, isSelectAll);
        }
        table.refreshDrawableState();
        table.invalidate();
        isSelectedAll = !isSelectAll;
    }


    public void initTableData() {
        accountDBManager = new AccountDBManager(this);
        data = accountDBManager.queryAll(GV.getUserName(this));
        col = getCol();
        columns.clear();


        int size = DensityUtils.dp2px(this, 30);

        operation = new Column<>("选择", "operation", new ImageResDrawFormat<Boolean>(size, size) {    //设置"操作"这一列以图标显示 true、false 的状态
            @Override
            protected Context getContext() {
                return MainView.this;
            }

            @Override
            protected int getResourceID(Boolean isCheck, String value, int position) {
                if (isCheck) {
                    return R.drawable.ic_check_green;
                }
                return R.drawable.ic_check_gray;
            }
        });
        operation.setComputeWidth(40);
        operation.setFixed(true);//固定这一列
        operation.setOnColumnItemClickListener(new OnColumnItemClickListener<Boolean>() {
            @Override
            public void onClick(Column<Boolean> column, String value, Boolean bool, int position) {
                if (operation.getDatas().get(position)) {
                    onItemSelected(position, false);
                    operation.getDatas().set(position, false);
                } else {
                    onItemSelected(position, true);
                    operation.getDatas().set(position, true);
                }
                table.refreshDrawableState();
                table.invalidate();
            }
        });

        columns.add(operation);


        try {

            for (int j = 0; j < col.size(); j++) {
                Column column = new Column<String>(col.get(j), getFieldNames().get(j));
                column.setOnColumnItemClickListener(new OnColumnItemClickListener() {
                    @Override
                    public void onClick(Column column, String value, Object o, int position) {
                        CommonUtils.copyStr(MainView.this, value);
                        CommonUtils.showToast(MainView.this, value + "已经复制到剪切板");
                    }
                });
                columns.add(column);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        TableData<AccountBean> tableData = new TableData<AccountBean>("测试标题", data, columns);
        table.getConfig().setShowTableTitle(false);
        table.setTableData(tableData);
        table.getConfig().setMinTableWidth(Tools.getScreenWidth(this));
        FontStyle style = new FontStyle();
        style.setTextSize(38);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        table.getConfig().setContentStyle(style);       //设置表格主题字体样式

        FontStyle titleStyle = new FontStyle();
        titleStyle.setTextSize(42);
        titleStyle.setTextColor(Color.BLACK);
        table.getConfig().setColumnTitleStyle(titleStyle);   //设置表格标题字体样式
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {     //设置隔行变色
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {//在此处理

                if (cellInfo.row % 2 == 1) {
                    return ContextCompat.getColor(MainView.this, R.color.numbercolor);
                } else {
                    return TableConfig.INVALID_COLOR;
                }

            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                return super.getTextColor(cellInfo);
            }
        });

    }

    /**
     * 收集所有被勾选的并实时更新
     *
     * @param position      被选择记录的行数，根据行数用来找到其他列中该行记录对应的信息
     * @param selectedState 当前的操作状态：选中 || 取消选中
     */
    public void onItemSelected(int position, boolean selectedState) {
        data.get(position).setOperation(selectedState);
    }

    private ArrayList<String> getCol() {
        String[] array = null;
        array = new String[]{"描述", "账户", "密码", "邮箱", "电话", "备注"};
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(array));
        return list;
    }

    private ArrayList<String> getFieldNames() {
        String[] array = null;
        array = new String[]{"description", "account", "password", "email", "phone", "remark"};
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(array));
        return list;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(MainView.this, AddAccountView.class);
            intent.putExtra("isUpdate", false);
            startActivityForResult(intent, IntentCode.INTENT_ADD);

            return true;
        } else if (id == R.id.action_update) {

            if (update() == null) {
                return true;
            }

            Intent intent = new Intent(MainView.this, AddAccountView.class);
            intent.putExtra("isUpdate", true);
            intent.putExtra("data", update());
            startActivityForResult(intent, IntentCode.INTENT_ADD);
            return true;
        } else if (id == R.id.action_delete) {
            delete();
            refreshData();
            return true;
        } else if (id == R.id.action_refresh) {
            refreshData();
            return true;
        } else if (id == R.id.action_excel) {
            write();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (id == R.id.nav_clear) {
            final DialogView dialogView = new DialogView(this);
            dialogView.setMessage("清空数据后无法恢复，确定清空数据？");
            dialogView.setDialogViewClickButtonListener(new OnDialogViewButtonClickListener() {
                @Override
                public void onOKClick(View view) {
                    AccountDBManager accountDBManager = new AccountDBManager(MainView.this);
                    accountDBManager.deleteAllByUserName(GV.getUserName(MainView.this));
                    refreshData();

                    if (data.isEmpty()) {
                        CommonUtils.showToast(MainView.this, "清除成功");
                    } else {
                        CommonUtils.showToast(MainView.this, "清除失败");
                    }
                    dialogView.dismiss();


                }

                @Override
                public void onCancelClick(View view) {
                    dialogView.dismiss();
                }
            });

            dialogView.show();
        } else if (id == R.id.nav_back) {
            finish();
        }

        return true;
    }

    private void refreshData() {
        data = accountDBManager.queryAll(GV.getUserName(this));
        TableData<AccountBean> tableData = new TableData<AccountBean>("测试标题", data, columns);
        table.setTableData(tableData);
        table.refreshDrawableState();
        table.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == IntentCode.INTENT_ADD && resultCode == RESULT_OK) {
            refreshData();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void writeToExcel(List lists) {
        String now = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String path = Environment.getExternalStorageDirectory() + "/LocalAccount";

        File dir = new File(path);
        makeDir(dir);

        String fileAllPath = path + "/" + "dataexcel" + now + ".xls";
        ExcelUtils.initExcel(fileAllPath, col);
        ExcelUtils.writeObjListToExcel(lists, fileAllPath, this);
        CommonUtils.showToast(this, "数据导出到" + fileAllPath);
    }

    private void delete() {//删除操作
        boolean hasDeleDate = false;
        for (int i = 0; i < data.size(); i++) {
            AccountBean accountBean = data.get(i);
            if (accountBean.isOperation()) {
                accountDBManager.deleteByDes(accountBean.getDescription());
                hasDeleDate = true;
            }
        }
        if (!hasDeleDate) {
            CommonUtils.showToast(this, "请至少选择一组数据");
        }

    }

    private AccountBean update() {//删除操作
        List<AccountBean> accountBeanList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            AccountBean accountBean = data.get(i);
            if (accountBean.isOperation()) {
                accountBeanList.add(accountBean);
            }
        }
        if (accountBeanList.isEmpty()) {
            CommonUtils.showToast(this, "请选择一组数据");
            return null;
        }
        if (accountBeanList.size() > 1) {
            CommonUtils.showToast(this, "最多一组数据");
            return null;
        }

        return accountBeanList.get(0);

    }

    public static void makeDir(File dir) {
        if (!dir.exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdirs();
    }

    private void write() {//写入excel操作
        List<List> lists = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            AccountBean accountBean = data.get(i);
            if (accountBean.isOperation()) {
                List list = new ArrayList();
                list.add(accountBean.getDescription());
                list.add(accountBean.getAccount());
                list.add(accountBean.getPassword());
                list.add(accountBean.getEmail());
                list.add(accountBean.getPhone());
                list.add(accountBean.getRemark());
                lists.add(list);
            }
        }
        if (lists.isEmpty()) {
            CommonUtils.showToast(this, "请至少选择一组数据");
        } else {
            writeToExcel(lists);
        }
    }
}
