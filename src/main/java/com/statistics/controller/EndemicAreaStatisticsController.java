package com.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.statistics.service.statistics.MemScoreStatisticsService;
import com.statistics.util.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping(value = "/statistics")
public class EndemicAreaStatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(EndemicAreaStatisticsController.class);

    @Autowired
    private MemScoreStatisticsService memScoreStatisticsService;

    private BigDecimal standardScore = new BigDecimal(1);

    @RequestMapping(value = "/statisticsEveryOneTotalScore")
    @ResponseBody
    public void statisticsEveryOneTotalScore(){
        List<Map<String,String>> userList = memScoreStatisticsService.getAllUser();
        for(int i=0;i<userList.size();i++){
            Map<String,String> user = userList.get(i);
            String userId = StringUtil.getString(user.get("id"));
            Map queryMap = new HashMap();
            queryMap.put("userId",userId);
            queryMap.put("year","2019");
            Map<String,String> score = memScoreStatisticsService.getUserTotalScore(queryMap);

            queryMap.put("beginTime",1546272000L);
            queryMap.put("endTime",1577808000L);

            //获取用户当前年份所学的课程
            List<Map> list = memScoreStatisticsService.getUserStatisticsScores(queryMap);
            boolean ispass = true;
            BigDecimal sumScore = new BigDecimal(0);
            for(int j=0;j<list.size();j++){
                Map map = list.get(j);
                String ispassStr =  StringUtil.getString(map.get("ispass"));
                String totalScore =  StringUtil.getString(map.get("total_score"));
                sumScore = sumScore.add(new BigDecimal(totalScore));
                if(ispassStr.equals("0")){
                    ispass = false;
                }
            }

            if(sumScore.compareTo(standardScore) < 0){
                ispass = false;
            }

            if(ispass){
                Map map = new HashMap();
                map.put("userId",userId);
                map.put("status",1);
                map.put("year","2019");
                List<Map<String,String>> resultlist = memScoreStatisticsService.getUserResult(map);
                if(resultlist != null && resultlist.size() > 0){
                    memScoreStatisticsService.updateUserResult(map);
                }else{
                    memScoreStatisticsService.addUserResult(map);
                }

            }else{
                Map map = new HashMap();
                map.put("userId",userId);
                map.put("status",0);
                map.put("year","2019");
                List<Map<String,String>> resultlist = memScoreStatisticsService.getUserResult(map);
                if(resultlist != null && resultlist.size() > 0){
                    memScoreStatisticsService.updateUserResult(map);
                }else{
                    memScoreStatisticsService.addUserResult(map);
                }
            }
            /*if(score != null){
                BigDecimal s = new BigDecimal(StringUtil.getString(score.get("score")));
                System.out.println(s);
                if(s.compareTo(new BigDecimal(1)) >= 0){
                    Map map = new HashMap();
                    map.put("userId",userId);
                    map.put("status",1);
                    map.put("year","2019");
                    memScoreStatisticsService.addUserResult(map);
                }
            }else{
                Map map = new HashMap();
                map.put("userId",userId);
                map.put("status",0);
                map.put("year","2019");
                memScoreStatisticsService.addUserResult(map);
            }*/
        }
    }

    @RequestMapping(value = "/getEndemicAreaStatistics")
    @ResponseBody
    public String getEndemicAreaStatistics(){
        List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
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


        for(int i = 0;i<endemicArealist.size();i++){
            Map<String,String> resultmap = new HashMap<String,String>();
            Map map = endemicArealist.get(i);
            String area = StringUtil.getString(map.get("area"));
            String year = "2019";
            Map queryMap = new HashMap();
            queryMap.put("year",year);
            queryMap.put("area",area);
            List userScores = memScoreStatisticsService.getUserStatisticsScores(queryMap);
            System.out.print(area+"       "+userScores.size());
            Iterator<Integer> levelits = levelMap.keySet().iterator();
            resultmap.put("area",area);
            while(levelits.hasNext()){
                Integer type = levelits.next();
                queryMap.put("type",type);
                queryMap.put("ispass",0);
                List userScoresLevela = memScoreStatisticsService.getUserStatisticsScores(queryMap);
                queryMap.put("ispass",1);
                List userScoresLevelb = memScoreStatisticsService.getUserStatisticsScores(queryMap);
                System.out.print("       "+userScoresLevelb.size()+"/"+(userScoresLevela.size()+userScoresLevelb.size()));
                resultmap.put(type+"",userScoresLevelb.size()+"/"+(userScoresLevela.size()+userScoresLevelb.size()));
            }

            Iterator<Integer> professionnalits = professionnalMap.keySet().iterator();
            while(professionnalits.hasNext()){
                Integer type = professionnalits.next();
                queryMap.put("type",type);
                queryMap.put("ispass",0);
                List userScoresProa = memScoreStatisticsService.getUserStatisticsScores(queryMap);
                queryMap.put("ispass",1);
                List userScoresProb = memScoreStatisticsService.getUserStatisticsScores(queryMap);
                System.out.print("       "+userScoresProb.size()+"/"+(userScoresProa.size() + userScoresProb.size()));
                resultmap.put(type+"",userScoresProb.size()+"/"+(userScoresProa.size() + userScoresProb.size()));
            }
            resultList.add(resultmap);
            System.out.println();
        }

        return JSON.toJSONString(resultList);

    }
}
