package com.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.statistics.service.statistics.MemScoreStatisticsService;
import com.statistics.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;


@Controller
@RequestMapping(value = "/statistics")
public class MemScoreStatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(MemScoreStatisticsController.class);

    @Autowired
    private MemScoreStatisticsService memScoreStatisticsService;

    @RequestMapping(value = "/getEndemicArea")
    @ResponseBody
    public String getEndemicArea(){
        List<Map> list = memScoreStatisticsService.getEndemicArea();
        String result = JSON.toJSONString(list);
        return result;
    }

    @RequestMapping(value = "/getMemberStatistics")
    @ResponseBody
    public String getMemberStatistics(){
        List<Map> endemicArealist = memScoreStatisticsService.getEndemicArea();
        List<Map> professionnallist = memScoreStatisticsService.getProfessionalGroup();
        Map<Integer,String> professionnalMap = new HashMap<Integer,String>();
        for(int i=0;i<professionnallist.size();i++){
            Map map = professionnallist.get(i);
            professionnalMap.put(Integer.valueOf(StringUtil.getString(map.get("group_no"))),StringUtil.getString(map.get("pgroup")));
        }
        List<Map> levellist = memScoreStatisticsService.getlevels();
        Map<Integer,String> levelMap = new HashMap<Integer,String>();
        for(int i=0;i<levellist.size();i++){
            Map map = levellist.get(i);
            levelMap.put(Integer.valueOf(StringUtil.getString(map.get("id"))),StringUtil.getString(map.get("title")));
        }
        List<Map> userScorelist = memScoreStatisticsService.getUserScoreNotAcsy();
        List<Map> userStatisticsList = new ArrayList<Map>();
        for(int i=0;i<userScorelist.size();i++){
            Map userscoremap = userScorelist.get(i);
            String courseId = StringUtil.getString(userscoremap.get("courseId"));
            String testId = StringUtil.getString(userscoremap.get("testId"));
            String userId = StringUtil.getString(userscoremap.get("userId"));
            Map queryMap = new HashMap();
            queryMap.put("courseId",courseId);
            queryMap.put("testId",testId);
            queryMap.put("userId",userId);
            //logger.error(courseId + "======" + testId);
            Map course = memScoreStatisticsService.getCourseById(queryMap);
            Map testPaper = memScoreStatisticsService.getTestPagerByMap(queryMap);
            if(testPaper == null) continue;
            String target = StringUtil.getString(testPaper.get("target"));
            String  lessonId = target.split("/")[1].split("-")[1];
            queryMap.put("lessonId",lessonId);
            Map lesson = memScoreStatisticsService.getLessonById(queryMap);
            if(course == null) continue;
            String courseType = StringUtil.getString(course.get("buyable"));
            Integer ct = Integer.valueOf(courseType);
            Integer id = null;
            Integer type = 0;
            String typeName = null;
            BigDecimal totalScore = new BigDecimal(0);
            String year = "2019";
            type = ct;
            if(ct < 30){
                System.out.print(course.get("title")+"===="+"层级:"+levelMap.get(ct)+"====");
                typeName = StringUtil.getString(levelMap.get(ct));
                if( lesson != null){
                    System.out.print("课程==="+lesson.get("title")+lesson.get("giveCredit"));
                }else{
                    continue;
                }

                totalScore = new BigDecimal( StringUtil.getString(lesson.get("giveCredit")) == null ? "0" : StringUtil.getString(lesson.get("giveCredit")));
                System.out.println("----------"+totalScore);
            }else{
                System.out.print(course.get("title")+"===="+"小组:"+professionnalMap.get(ct)+"====");
                typeName = StringUtil.getString(professionnalMap.get(ct));
                if( lesson != null){
                    System.out.print("课程==="+lesson.get("title")+lesson.get("giveCredit"));
                }else{
                    continue;
                }
                totalScore = new BigDecimal( StringUtil.getString(lesson.get("giveCredit")) == null  ? "0" : StringUtil.getString(lesson.get("giveCredit")));
                System.out.println("----------"+totalScore);
            }
            Map map = new HashMap();
            map.put("id", UUID.randomUUID().toString());
            map.put("userId",userId);
            map.put("type",type);
            map.put("typeName",typeName);
            map.put("totalScore",totalScore);
            map.put("year",year);
            userStatisticsList.add(map);
            //memScoreStatisticsService.addUserStatisticsScore(map);
        }
        //处理成绩
        for(int i=0;i<userStatisticsList.size();i++){
            Map map = userStatisticsList.get(i);
            List<Map> userScoreList = memScoreStatisticsService.getUserStatisticsScores(map);
            if(userScoreList != null && userScoreList.size()>0){
                Map userScore = userScoreList.get(0);
                BigDecimal newscore = new BigDecimal(StringUtil.getString(map.get("totalScore")));
                BigDecimal score = new BigDecimal(StringUtil.getString(userScore.get("total_score")));
                BigDecimal totalScore = newscore.add(score);
                map.put("totalScore",totalScore);
                memScoreStatisticsService.updateUserStatisticsScores(map);
            }else{
                memScoreStatisticsService.addUserStatisticsScore(map);
            }
        }
        return JSON.toJSONString(userScorelist);
    }

}
