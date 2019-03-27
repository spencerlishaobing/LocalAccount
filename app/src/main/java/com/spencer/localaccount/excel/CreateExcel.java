package com.spencer.localaccount.excel;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CreateExcel {
    // 准备设置excel工作表的标题
    private WritableSheet sheet;
    /**
     * 创建Excel工作薄
     */
    private WritableWorkbook wwb;
    private String[] title = {"日期", "苹果", "桃子", "梨子", "桔子", "香蕉", "荔枝", "桂圆"
            , "葡萄", "西瓜", "火龙果", "备注说明"};

    public CreateExcel() {
        excelCreate();
    }

    public void excelCreate() {
        try {
            /**输出的excel文件的路径*/
            String filePath = Environment.getExternalStorageDirectory() + "/my_price";
            File file = new File(filePath, "price.xls");
            if (!file.exists() && file.createNewFile()) {
                Log.i(ExcelUtils.class.getClass().toString(), "文件创建成功");
            }
            wwb = Workbook.createWorkbook(file);
            /**添加第一个工作表并设置第一个Sheet的名字*/
            sheet = wwb.createSheet("水果价格", 0);
        } catch (Exception e) {
            Log.i(getClass().toString(), e.getMessage());
        }
    }

    public void saveDataToExcel(int index, String[] content) {
        Label label;

        try {
            for (int i = 0; i < title.length; i++) {
                /**Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是y
                 * 在Label对象的子对象中指明单元格的位置和内容
                 * */
                label = new Label(i, 0, title[i]);
                /**将定义好的单元格添加到工作表中*/
                sheet.addCell(label);
            }
        /*
         * 把数据填充到单元格中
		 * 需要使用jxl.write.Number
		 * 路径必须使用其完整路径，否则会出现错误
		 */
            for (int i = 0; i < title.length; i++) {
                Label labeli = new Label(i, index, content[i]);
                sheet.addCell(labeli);
            }
            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();

        } catch (Exception e) {
            Log.i(getClass().toString(), e.getMessage());
        }
    }
}
