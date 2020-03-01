package com.statistics.controller;

import com.statistics.model.ExcelUser;
import com.statistics.model.ExcelUserProfile;
import com.statistics.service.statistics.MemScoreStatisticsService;
import com.statistics.service.statistics.UserProfileService;
import com.statistics.service.statistics.UserService;
import com.statistics.util.ExcelUtil;
import com.statistics.util.StringUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
}
