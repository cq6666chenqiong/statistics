package com.statistics.controller;

import com.statistics.model.ExcelUser;
import com.statistics.model.ExcelUserProfile;
import com.statistics.service.statistics.MemScoreStatisticsService;
import com.statistics.service.statistics.UserProfileService;
import com.statistics.service.statistics.UserService;
import com.statistics.util.ConstantData;
import com.statistics.util.ExcelUtil;
import com.statistics.util.StringUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFRow;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping(value = "/memberUpload")
public class MemberUploadController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MemScoreStatisticsService memScoreStatisticsService;

    @RequestMapping(value = "")
    public String index(){
        return "/memberUpload/memberUpload";
    }

    @RequestMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            System.out.println("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            System.out.println("文件的后缀名为：" + suffixName);

            String newName = UUID.randomUUID().toString().replaceAll("-","");

            // 设置文件存储路径
            String filePath = "D://aim//";
            //String path = filePath + fileName + suffixName;
            String path = filePath + newName + suffixName;

            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入

            readExcel(path);


            dest.delete();

            return "success";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    public void readExcel(String path){

        List<Map> rankList = memScoreStatisticsService.getlevels();

        Map<String,String> rankMap = new HashMap<String,String>();

        for(int i=0;i<rankList.size();i++){
            Map map = rankList.get(i);
            rankMap.put(StringUtil.getString(map.get("title")),StringUtil.getString(map.get("id")));
        }



        Integer idnum = userService.getMaxId();
        List<ExcelUser> userList = new ArrayList<ExcelUser>();
        List<ExcelUserProfile> userProfileList = new ArrayList<ExcelUserProfile>();
        try {
            Workbook wb = new HSSFWorkbook(new FileInputStream(path));
            Sheet sheet=wb.getSheetAt(0);
            Cell cell = null;
            //下面是处理合并单元格的代码。

            for(int i = 1; i <= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);// 获取行对象
                if (row == null) {// 如果为空，不处理
                    continue;
                 }
                /*int firstCellNum = row.getFirstCellNum();
                int lastCellNum = row.getLastCellNum();
                for(int j = firstCellNum;j<lastCellNum;j++){
                    Cell cell = row.getCell(j);
                    System.out.println(ExcelUtil.getValue(cell));
                }*/
                ExcelUser user = new ExcelUser();
                user.setId(idnum + i);
                ExcelUserProfile userProfile = new ExcelUserProfile();
                userProfile.setId(idnum + i);

                cell = row.getCell(0);//工号
                String userno = ExcelUtil.getValue(cell);
                user.setNickname(userno);
                user.setEmail(userno+"");

                cell = row.getCell(1);//姓名
                String truename = ExcelUtil.getValue(cell);
                userProfile.setTruename(truename);

                cell = row.getCell(2);//性别
                String gender = ExcelUtil.getValue(cell);
                if("男".equals("gender")){
                    userProfile.setGender("male");
                }else if("女".equals("gender")){
                    userProfile.setGender("female");
                }else{
                    userProfile.setGender("secret");
                }

                cell = row.getCell(3);//身份证号
                String idcard = ExcelUtil.getValue(cell);
                userProfile.setIdcard(idcard);

                cell = row.getCell(4);//出生日期
                String birthDay = ExcelUtil.getValue(cell);
                userProfile.setVarcharField1(birthDay);

                cell = row.getCell(5);//手机号码
                String phone = ExcelUtil.getValue(cell);
                userProfile.setMobile(phone);

                cell = row.getCell(6);//职称
                String jobname = ExcelUtil.getValue(cell);
                userProfile.setJob(jobname);

                cell = row.getCell(7);//行政职务
                String position = ExcelUtil.getValue(cell);
                userProfile.setVarcharField2(position);

                cell = row.getCell(8);//病区
                String area = ExcelUtil.getValue(cell);
                userProfile.setVarcharField3(area);

                cell = row.getCell(9);//层级
                String rank = ExcelUtil.getValue(cell);
                userProfile.setCompany(rankMap.get(rank));

                cell = row.getCell(10);//专业类别
                String profession = ExcelUtil.getValue(cell);
                userProfile.setVarcharField4(profession);

                cell = row.getCell(11);//最高学位
                String maxEducation = ExcelUtil.getValue(cell);
                userProfile.setVarcharField6(maxEducation);

                cell = row.getCell(12);//教学老师
                String teacher = ExcelUtil.getValue(cell);
                userProfile.setVarcharField5(teacher);

                cell = row.getCell(13);//其他
                String other = ExcelUtil.getValue(cell);
                userProfile.setVarcharField7(other);

                userList.add(user);
                userProfileList.add(userProfile);
            }

            for(int i=0;i<userList.size();i++){
                ExcelUser user = userList.get(i);
                ExcelUserProfile userProfile = userProfileList.get(i);
                userService.addExcelUser(user);
                userProfileService.addExcelUserProfile(userProfile);
            }

            //处理合并单元格 end

            //如果没有合并单元格，可以设置行和列的下标，0开始，循环，使用sheet.getRow(rowFrom).getCell(cellFrom).toString();，可以遍历整个excel。
            //如果有合并单元格，请按照上面代码块查询数据。

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    /*public void readExcel(String path){
        try {
            Workbook wb = new HSSFWorkbook(new FileInputStream(path));
            Sheet sheet=wb.getSheetAt(0);

            //下面是处理合并单元格的代码。

            //获取当前sheet页中，所有的合并单元格的数量。
            int regionsCount = sheet.getNumMergedRegions();

            if (regionsCount > 0) {
                for (int i = 0; i < regionsCount; i++) {
                    CellRangeAddress region = sheet.getMergedRegion(i);
                    //输出单元格的起止。
                    System.out.println(region.formatAsString());
                    //获取单元格的起止行和列
                    int rowFrom = region.getFirstRow();
                    int rowTo = region.getLastRow();
                    int cellFrom = region.getFirstColumn();
                    int cellTo = region.getLastColumn();

                    //sheet.getRow().getCell   可以获取某行某个单元格。
                    String hbzhi = sheet.getRow(rowFrom).getCell(cellFrom).toString();
                    System.out.println(hbzhi);
                }
            }

            //处理合并单元格 end

            //如果没有合并单元格，可以设置行和列的下标，0开始，循环，使用sheet.getRow(rowFrom).getCell(cellFrom).toString();，可以遍历整个excel。
            //如果有合并单元格，请按照上面代码块查询数据。

        } catch (Exception e) {
            e.printStackTrace();

        }

    }*/

    @RequestMapping(value = "/downLoadMemberModel")
    @ResponseBody
    public void downLoadMemberModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = new File(ConstantData.memberModelpath);
        //设置中文文件名与后缀
        String encodedFileName = URLEncoder.encode("批量上传用户信息模板" + ".xls","utf-8").replaceAll("\\+", "%20");
        // 清除buffer缓存
        response.reset();
        // 指定下载的文件名
        response.setHeader("Content-Disposition",
                "attachment;filename="+encodedFileName+"");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0 ;
        while ((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer ,0 , len);
        }
        inputStream.close();
        outputStream.close();

    }

    /***
     * 创建表头
     * @param workbook
     * @param sheet
     */
    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet)
    {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(2, 12*256);
        sheet.setColumnWidth(3, 17*256);

        //设置为居中加粗
        short m = 3;
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBoldweight(m);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("员工编号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("性别");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("身份证号");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("出生日期");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("手机号码");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("职称");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("行政职务");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("病区");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("层级");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("专业类别");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellValue("最高学位");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellValue("教学老师");
        cell.setCellStyle(style);

        cell = row.createCell(13);
        cell.setCellValue("其他");
        cell.setCellStyle(style);
    }


    @RequestMapping(value = "/downLoadMemberExcel")
    @ResponseBody
    public void downLoadMemberExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String nickname = request.getParameter("nickname");  //员工工号
        String truename = request.getParameter("truename");  //员工姓名
        String company = request.getParameter("company");    //层级

        Map map = new HashMap();
        map.put("nickname",nickname);
        map.put("truename",truename);
        map.put("company",company);
        List<Map> userList = userService.getUserDetail(map);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("用户信息");
        createTitle(workbook, sheet);
        int rowNum = 1;
        for (Map m:userList) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(StringUtil.getString(m.get("nickname")));          //工号
            row.createCell(1).setCellValue(StringUtil.getString(m.get("truename")));          //姓名
            row.createCell(2).setCellValue(StringUtil.getString(m.get("birthday")));          //性别
            row.createCell(3).setCellValue(StringUtil.getString(m.get("idcard")));            //身份证号
            row.createCell(4).setCellValue(StringUtil.getString(m.get("varcharField1")));    //出生日期
            row.createCell(5).setCellValue(StringUtil.getString(m.get("mobile")));            //手机号码
            row.createCell(6).setCellValue(StringUtil.getString(m.get("job")));               //职称
            row.createCell(7).setCellValue(StringUtil.getString(m.get("varcharField2")));    //行政职务
            row.createCell(8).setCellValue(StringUtil.getString(m.get("varcharField3")));     //病区
            row.createCell(9).setCellValue(StringUtil.getString(m.get("company")));           //层级
            row.createCell(10).setCellValue(StringUtil.getString(m.get("varcharField4")));    //专业类别
            row.createCell(11).setCellValue(StringUtil.getString(m.get("varcharField6")));    //最高学历
            row.createCell(12).setCellValue(StringUtil.getString(m.get("varcharField5")));    //教学老师
            row.createCell(13).setCellValue(StringUtil.getString(m.get("varcharField7")));    //其他

            rowNum++;
        }
        //设置中文文件名与后缀
        String encodedFileName = URLEncoder.encode("用户信息" + ".xls","utf-8").replaceAll("\\+", "%20");
        // 清除buffer缓存
        response.reset();
        // 指定下载的文件名
        response.setHeader("Content-Disposition",
                "attachment;filename="+encodedFileName+"");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();

    }
}
