package com.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.statistics.service.statistics.MemScoreStatisticsService;
import com.statistics.util.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/statistics")
public class ExcelController {

    @Autowired
    private MemScoreStatisticsService memScoreStatisticsService;

    @RequestMapping(value = "/getMemberScoreExcel")
    public void getMemberScoreExcel(HttpServletRequest request, HttpServletResponse response){
        List<Map<String,String>> userList = memScoreStatisticsService.getAllUser();
        List<Map<String,Object>> userScoreList = new ArrayList<Map<String,Object>>();

        for(int i=0;i<userList.size();i++){
            List<Map<String,String>> levelList = new ArrayList<Map<String,String>>();
            List<Map<String,String>> professionList = new ArrayList<Map<String,String>>();
            Map<String,String> userMap = userList.get(i);
            String userId = StringUtil.getString(userMap.get("id"));
            String truename = StringUtil.getString(userMap.get("truename"));
            String nickname = StringUtil.getString(userMap.get("nickname"));
            String ispass = "未通过";
            Map queryMap = new HashMap();
            queryMap.put("userId",userId);
            queryMap.put("year","2019");
            List<Map> list = memScoreStatisticsService.getUserStatisticsScores(queryMap);
            for(int j=0;j<list.size();j++){
                Map scoreMap = list.get(j);
                 Integer type = Integer.valueOf(StringUtil.getString(scoreMap.get("type")));
                 if(type < 35){
                     if(Integer.valueOf(StringUtil.getString(scoreMap.get("ispass"))) == 0){
                         scoreMap.put("ispass","未通过");
                     }else{
                         scoreMap.put("ispass","通过");
                     }
                     levelList.add(scoreMap);
                 }else{
                     if(Integer.valueOf(StringUtil.getString(scoreMap.get("ispass"))) == 0){
                         scoreMap.put("ispass","未通过");
                     }else{
                         scoreMap.put("ispass","通过");
                     }
                     professionList.add(scoreMap);
                 }
            }
            if(levelList.size() == 0){
                Map<String,String> umap = new HashMap<String,String>();
                umap.put("typeName","层级课程");
                umap.put("passmark","0/0");
                umap.put("ispass","未通过");
                levelList.add(umap);
            }
            if(professionList.size() == 0){
                Map<String,String> umap = new HashMap<String,String>();
                umap.put("typeName","专业课程");
                umap.put("passmark","0/0");
                umap.put("ispass","未通过");
                professionList.add(umap);
            }
            List<Map<String,String>> userResult = memScoreStatisticsService.getUserResult(queryMap);
            if(userResult != null && userResult.size() > 0){
                Map<String,String> result = userResult.get(0);
                if(StringUtil.getString(result.get("status")).equals("1")) ispass = "通过";

            }
            Map<String,Object> userScoreMap = new HashMap<String,Object>();
            userScoreMap.put("userId",userId);
            userScoreMap.put("truename",truename);
            userScoreMap.put("levelList",levelList);
            userScoreMap.put("professionList",professionList);
            userScoreMap.put("ispass",ispass);
            userScoreMap.put("nickname",nickname);

            userScoreList.add(userScoreMap);
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("学生成绩统计");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFCellStyle styleGreen = wb.createCellStyle();
        styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleGreen.setFillForegroundColor(HSSFColor.GREEN.index);

        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("员工号");
        cell.setCellStyle(styleGreen);
        cell = row.createCell((short) 1);
        cell.setCellValue("员工姓名");
        cell.setCellStyle(styleGreen);
        cell = row.createCell((short) 2);
        cell.setCellValue("层级课");
        cell.setCellStyle(styleGreen);
        cell = row.createCell((short) 3);
        cell.setCellValue("专业课");
        cell.setCellStyle(styleGreen);
        cell = row.createCell((short) 4);
        cell.setCellValue("是否通过");
        cell.setCellStyle(styleGreen);


        for(int i=0;i<userScoreList.size();i++) {
            //System.out.println(JSON.toJSONString(userScoreList.get(i)));
            Map<String,Object> map = userScoreList.get(i);
            row = sheet.createRow((int) 1 + i);
            cell = row.createCell((short) 0);
            cell.setCellValue(StringUtil.getString(map.get("nickname")));
            cell = row.createCell((short) 1);
            cell.setCellValue(StringUtil.getString(map.get("truename")));
            cell = row.createCell((short) 2);
            List<Map<String,String>> l = (List<Map<String,String>>)map.get("levelList");
            String le = "";
            for(int j=0;j<l.size();j++){
                Map<String,String> lmap = l.get(j);
                le = le + StringUtil.getString(lmap.get("typeName")) + "       " + StringUtil.getString(lmap.get("passmark")) + "       " + StringUtil.getString(lmap.get("ispass")) + "\r\n";
            }
            cell.setCellValue(le);
            String pr = "";
            List<Map<String,String>> p = (List<Map<String,String>>)map.get("professionList");
            for(int j=0;j<p.size();j++){
                Map<String,String> pmap = p.get(j);
                pr = pr + StringUtil.getString(pmap.get("typeName")) + "       " + StringUtil.getString(pmap.get("passmark")) + "       " + StringUtil.getString(pmap.get("ispass")) + "\r\n";
            }
            cell = row.createCell((short) 3);
            cell.setCellValue(pr);
            cell = row.createCell((short) 4);
            cell.setCellValue( StringUtil.getString(map.get("ispass")));
        }

        // 第六步，将文件存到指定位置
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String fileName = "学生成绩统计表.xls";// 文件名
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/getEndemicAreaExcel")
    public void getEndemicAreaExcel(){

    }
}
