package com.statistics.controller;

import com.statistics.model.ExcelUser;
import com.statistics.model.ExcelUserProfile;
import com.statistics.model.User;
import com.statistics.model.UserScore;
import com.statistics.service.statistics.UserScoreService;
import com.statistics.service.statistics.UserService;
import com.statistics.util.ConstantData;
import com.statistics.util.ExcelUtil;
import com.statistics.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping(value = "/scoreUpload")
public class ScoreUploadController {

    @Autowired
    private UserScoreService userScoreService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "")
    public String index(){
        return "/scoreUpload/scoreUpload";
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


        List<UserScore> userScoreList = new ArrayList<UserScore>();
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

                UserScore userScore = new UserScore();

                cell = row.getCell(0);//姓名
                String name = ExcelUtil.getValue(cell);

                cell = row.getCell(1);//工号
                String userno = ExcelUtil.getValue(cell);
                User queryUser = new User();
                queryUser.setNickname(userno);
                Map user = userService.getUser(queryUser);
                userScore.setUserId(Integer.valueOf(StringUtil.getString(user.get("id"))));


                cell = row.getCell(2);//所获学分
                String score = ExcelUtil.getValue(cell);
                userScore.setScore(new BigDecimal(score));

                cell = row.getCell(3);//备注
                String remark = ExcelUtil.getValue(cell);
                userScore.setRemark(remark);

                cell = row.getCell(4);//课程名称
                String courseName = ExcelUtil.getValue(cell);
                userScore.setCourseName(courseName);

                cell = row.getCell(5);//年份
                String year = ExcelUtil.getValue(cell);
                userScore.setYear(Integer.valueOf(StringUtil.getString(year)));

                cell = row.getCell(6);//课程分类
                String type = ExcelUtil.getValue(cell);
                if(type.equals("线下专业课程")){
                    userScore.setCourseType(1001);
                    userScore.setCourseId(-1);
                }else if(type.equals("线下层级课程")){
                    userScore.setCourseType(1003);
                    userScore.setCourseId(-2);
                }else{
                    userScore.setCourseType(1002);
                    userScore.setCourseId(-3);
                }
                userScoreList.add(userScore);
            }

            for(int i=0;i<userScoreList.size();i++){
                UserScore userScore = userScoreList.get(i);
                userScoreService.addUserScore(userScore);
            }


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @RequestMapping(value = "/downLoadScoreModel")
    @ResponseBody
    public void downLoadScoreModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = new File(ConstantData.scoreModelpath);
        //设置中文文件名与后缀
        String encodedFileName = URLEncoder.encode("批量上传用户线下成绩模板" + ".xls","utf-8").replaceAll("\\+", "%20");
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
}
