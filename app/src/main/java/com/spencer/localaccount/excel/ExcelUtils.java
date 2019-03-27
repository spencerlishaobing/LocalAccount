package com.spencer.localaccount.excel;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtils {


    private static final String TAG = "ExcelUtils";


    private static WritableCellFormat arial14format = null;
    private static WritableCellFormat arial10format = null;
    private static WritableCellFormat arial12format = null;

    public static final String UTF8_ENCODING = "UTF-8";
    public static final String GBK_ENCODING = "GBK";


    private ExcelUtils() {
    }

    public static void format() {
        WritableFont arial14font = null;
        WritableFont arial10font = null;
        WritableFont arial12font = null;
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(jxl.format.Colour.RED);
            arial12font = new WritableFont(WritableFont.ARIAL, 12);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        } catch (WriteException e) {

            Log.i(ExcelUtils.class.getClass().toString(), e.getMessage());
        }
    }

    public static void initExcel(String fileName, List<String> col) {
        String[] colName = new String[col.size()];
        col.toArray(colName);
        initExcel(fileName, colName);
    }

    public static void initExcel(String fileName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (!file.exists() && file.createNewFile()) {
                Log.i(ExcelUtils.class.getClass().toString(), "文件创建成功");
            }
            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("家集客SLA表", 0);

            sheet.addCell(new Label(0, 0, fileName, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
                sheet.setColumnView(col, 60);
            }
            workbook.write();
        } catch (Exception e) {
            Log.i(ExcelUtils.class.getClass().toString(), e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    Log.i(ExcelUtils.class.getClass().toString(), e.getMessage());
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(List<T> objList, String fileName, Context c) {
        if (objList != null && (!objList.isEmpty())) {
            WritableWorkbook writebook = null;
            try (InputStream in = new FileInputStream(new File(fileName))) {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);
                for (int j = 0; j < objList.size(); j++) {
                    ArrayList<String> list = (ArrayList<String>) objList.get(j);
                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                    }
                }
                writebook.write();
                Toast.makeText(c, "导出成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.i(ExcelUtils.class.getClass().toString(), e.getMessage());
                Log.e(TAG, "writeObjListToExcel: -------------" + e.toString());
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        Log.i(ExcelUtils.class.getClass().toString(), e.getMessage());
                    }

                }
            }

        }
    }

    public static Object getValueByRef(Class cls, String fieldName) {
        Object value = null;
        fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        String getMethodName = "get" + fieldName;
        try {
            Method method = cls.getMethod(getMethodName);
            value = method.invoke(cls);
        } catch (Exception e) {
            Log.i(ExcelUtils.class.getClass().toString(), e.getMessage());
        }
        return value;
    }
}
