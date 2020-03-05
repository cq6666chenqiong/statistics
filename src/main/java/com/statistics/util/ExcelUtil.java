package com.statistics.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {

    public static String getValue(Cell cell){
        String result = "";

        if(cell == null){
            return result;
        }

        switch (cell.getCellType()) // Cell.getCellType() has not deprecated in API
        {
            // Because of Cell.getCellType().getCode() has deprecated And do not provide a replacement way So just use direct ways
            case -1 : // _NONE
                result = "";
                break;
            case 0 : // NUMERIC Date
                short dataFormat = cell.getCellStyle().getDataFormat();
                // 31/176 : yyyy年m月d日（自定义型和日期型） ， 58 : m月d日 ， 32 : h时mm分
                // 有时候176也是数字型，那这样会报错，干脆直接抓个异常。总之抓一堆dateFormat来兼容就好了。
                if(HSSFDateUtil.isCellDateFormatted(cell) || HSSFDateUtil.isCellDateFormatted(cell)
                        || 31 == dataFormat || 58 == dataFormat || 32 == dataFormat || 176 == dataFormat) {
                    try {
                        result = DateFormatUtils.format(cell.getDateCellValue(), "yyyy-MM-dd hh:MM:ss");
                    }catch ( Exception e) {
                        try {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                        }catch (Exception ex) {
                            //出问题就不转
                        }
                        result = String.valueOf(cell.getStringCellValue());
                    }
                } else {
                    try {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    }catch (Exception ex) {
                        //出问题就不转
                    }
                    result = String.valueOf(cell.getStringCellValue());
                }
                break;
            case 1 : // STRING
                result = cell.getStringCellValue();
                break;
            case 2 : // FORMULA
                try {
                    result = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    try {
                        result = String.valueOf(cell.getRichStringCellValue());
                    } catch (Exception ex){
                        result = "";
                    }
                }
                break;
            case 3 : // BLANK
                result = "";
                break;
            case 4 : // BOOLEAN
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case 5 : // ERROR
                result = String.valueOf(cell.getErrorCellValue());
                break;
            default :
                result = "";
                break;
        }
        return result;
    }

    public static void main(String[] args) {

    }

}
