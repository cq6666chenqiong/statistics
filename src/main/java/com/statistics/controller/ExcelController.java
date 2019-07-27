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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/statistics")
public class ExcelController {

    @Autowired
    private MemScoreStatisticsService memScoreStatisticsService;

    @RequestMapping(value = "/getMemberScoreExcel")
    public void getMemberScoreExcel(HttpServletRequest request, HttpServletResponse response){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String year = StringUtil.getString(request.getParameter("year"));
        if(year == null || year.equals("")){
            Date date = new Date();
            year = df.format(date).split("-")[0];
        }
        String tname = request.getParameter("name");
        System.out.println(tname);
        Map userQqueryMap = new HashMap();
        userQqueryMap.put("truename",tname);
        List<Map<String,String>> userList = memScoreStatisticsService.getAllUser(userQqueryMap);
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
            queryMap.put("year",year);
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
                umap.put("total_score","0");
                levelList.add(umap);
            }
            if(professionList.size() == 0){
                Map<String,String> umap = new HashMap<String,String>();
                umap.put("typeName","专业课程");
                umap.put("passmark","0/0");
                umap.put("ispass","未通过");
                umap.put("total_score","0");
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
        sheet.setDefaultColumnWidth((short)20);
        //sheet.setDefaultRowHeight((short)600);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFCellStyle styleGreen = wb.createCellStyle();
        styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleGreen.setFillForegroundColor(HSSFColor.GREEN.index);

        HSSFCellStyle styleCentor = wb.createCellStyle();
        styleCentor.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

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
            row.setHeight((short)900);
            cell = row.createCell((short) 0);
            cell.setCellValue(StringUtil.getString(map.get("nickname")));
            cell.setCellStyle(styleCentor);
            cell = row.createCell((short) 1);
            cell.setCellValue(StringUtil.getString(map.get("truename")));
            cell.setCellStyle(styleCentor);
            cell = row.createCell((short) 2);
            List<Map<String,String>> l = (List<Map<String,String>>)map.get("levelList");
            String le = "";
            for(int j=0;j<l.size();j++){
                Map<String,String> lmap = l.get(j);
                le = le + StringUtil.getString(lmap.get("typeName")) + "       " + StringUtil.getString(lmap.get("passmark")) + "       " + StringUtil.getString(lmap.get("total_score")) + "\r\n";
            }
            cell.setCellValue(le);
            cell.setCellStyle(styleCentor);
            String pr = "";
            List<Map<String,String>> p = (List<Map<String,String>>)map.get("professionList");
            for(int j=0;j<p.size();j++){
                Map<String,String> pmap = p.get(j);
                pr = pr + StringUtil.getString(pmap.get("typeName")) + "       " + StringUtil.getString(pmap.get("passmark")) + "       " + StringUtil.getString(pmap.get("total_score")) + "\r\n";
            }
            cell = row.createCell((short) 3);
            cell.setCellValue(pr);
            cell.setCellStyle(styleCentor);
            cell = row.createCell((short) 4);
            cell.setCellValue( StringUtil.getString(map.get("ispass")));
            cell.setCellStyle(styleCentor);
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
    public void getEndemicAreaExcel(HttpServletRequest request, HttpServletResponse response){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String year = StringUtil.getString(request.getParameter("year"));
        if(year == null || year.equals("")){
            Date date = new Date();
            year = df.format(date).split("-")[0];
        }
        String tname = request.getParameter("name");
        System.out.println(tname);
        Map userQqueryMap = new HashMap();
        userQqueryMap.put("truename",tname);
        List<Map> endemicArealist = memScoreStatisticsService.getEndemicArea();
        List<Map> professionnallist = memScoreStatisticsService.getProfessionalGroup();
        Map<Integer,String> professionnalMap = new HashMap<Integer,String>();
        Map<String,Integer> professionnalNameMap = new HashMap<String,Integer>();
        for(int i=0;i<professionnallist.size();i++){
            Map map = professionnallist.get(i);
            professionnalMap.put(Integer.valueOf(StringUtil.getString(map.get("group_no"))),StringUtil.getString(map.get("pgroup")));
            professionnalNameMap.put(StringUtil.getString(map.get("pgroup")),Integer.valueOf(StringUtil.getString(map.get("group_no"))));
        }
        for(int i=0;i<professionnallist.size();i++){
            Map map = professionnallist.get(i);
            professionnalMap.put(Integer.valueOf(StringUtil.getString(map.get("group_no"))),StringUtil.getString(map.get("pgroup")));
        }
        List<Map> levellist = memScoreStatisticsService.getlevels();
        Map<Integer,String> levelMap = new HashMap<Integer,String>();
        Map<String,Integer> levelNameMap = new HashMap<String,Integer>();
        for(int i=0;i<levellist.size();i++){
            Map map = levellist.get(i);
            levelMap.put(Integer.valueOf(StringUtil.getString(map.get("id"))),StringUtil.getString(map.get("title")));
        }
        for(int i=0;i<levellist.size();i++){
            Map map = levellist.get(i);
            levelNameMap.put(StringUtil.getString(map.get("title")),Integer.valueOf(StringUtil.getString(map.get("id"))));
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("病区成绩统计");
        sheet.setDefaultColumnWidth((short)20);
        //sheet.setDefaultRowHeight((short)600);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFCellStyle styleGreen = wb.createCellStyle();
        styleGreen.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        styleGreen.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleGreen.setFillForegroundColor(HSSFColor.GREEN.index);

        HSSFCellStyle styleCentor = wb.createCellStyle();
        styleCentor.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFRow row = sheet.createRow((int) 0);

        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("病区");
        cell.setCellStyle(styleGreen);


        int c = 1;
        Iterator<Integer> leveltitleits = levelMap.keySet().iterator();
        while(leveltitleits.hasNext()){
            int key = leveltitleits.next();
            String name = levelMap.get(key);
            cell = row.createCell((short) c);
            cell.setCellValue(name);
            cell.setCellStyle(styleGreen);
            c++;
        }

        cell = row.createCell((short) c);
        cell.setCellValue("总分合格率");
        cell.setCellStyle(styleGreen);
        c++;

        Iterator<Integer> professionnaltitleits = professionnalMap.keySet().iterator();
        while(professionnaltitleits.hasNext()){
            int key = professionnaltitleits.next();
            String name = professionnalMap.get(key);
            cell = row.createCell((short) c);
            cell.setCellValue(name);
            cell.setCellStyle(styleGreen);
            c++;
        }

        cell = row.createCell((short) c);
        cell.setCellValue("专业组合格率");
        cell.setCellStyle(styleGreen);




        for(int i = 0;i<endemicArealist.size();i++){
            int colum = 0;
            row = sheet.createRow((int) i+1);
            Map map = endemicArealist.get(i);
            String area = StringUtil.getString(map.get("area"));
            Map queryMap = new HashMap();
            queryMap.put("year",year);
            queryMap.put("area",area);
            List userScores = memScoreStatisticsService.getUserStatisticsScores(queryMap);
            System.out.print(area+"       "+userScores.size());
            Iterator<Integer> levelits = levelMap.keySet().iterator();
            cell = row.createCell((short) colum);
            cell.setCellValue(area);
            colum++;
            Integer a = 0;
            Integer b = 0;
            while(levelits.hasNext()){
                cell = row.createCell((short) colum);
                Integer type = levelits.next();
                queryMap.put("type",type);
                queryMap.put("ispass",0);
                List userScoresLevela = memScoreStatisticsService.getUserStatisticsScores(queryMap);
                queryMap.put("ispass",1);
                List userScoresLevelb = memScoreStatisticsService.getUserStatisticsScores(queryMap);

                // System.out.print("       "+userScoresLevelb.size()+"/"+(userScoresLevela.size()+userScoresLevelb.size()));
                cell.setCellValue(userScoresLevelb.size()+"/"+(userScoresLevela.size()+userScoresLevelb.size()));
                a = a + userScoresLevelb.size();
                b = b + userScoresLevela.size() + userScoresLevelb.size();
                colum++;
            }

            cell = row.createCell((short) colum);
            if(b==0){
                cell.setCellValue("0.00%");
            }else{
                cell.setCellValue(new BigDecimal(a).divide(new BigDecimal(b),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)+"%");
            }
            colum++;

            a = 0;
            b = 0;

            Iterator<Integer> professionnalits = professionnalMap.keySet().iterator();
            while(professionnalits.hasNext()){
                cell = row.createCell((short) colum);
                Integer type = professionnalits.next();
                queryMap.put("type",type);
                queryMap.put("ispass",0);
                List userScoresProa = memScoreStatisticsService.getUserStatisticsScores(queryMap);
                queryMap.put("ispass",1);
                List userScoresProb = memScoreStatisticsService.getUserStatisticsScores(queryMap);
                System.out.print("       "+userScoresProb.size()+"/"+(userScoresProa.size() + userScoresProb.size()));
                cell.setCellValue(userScoresProb.size()+"/"+(userScoresProa.size()+userScoresProb.size()));
                a = a + userScoresProb.size();
                b = b + userScoresProa.size() + userScoresProb.size();
                colum++;
            }

            cell = row.createCell((short) colum);
            if(b==0){
                cell.setCellValue("0.00%");
            }else{
                cell.setCellValue(new BigDecimal(a).divide(new BigDecimal(b),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)+"%");
            }

            System.out.println();
        }

        // 第六步，将文件存到指定位置
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String fileName = "病区成绩统计.xls";// 文件名
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
}
