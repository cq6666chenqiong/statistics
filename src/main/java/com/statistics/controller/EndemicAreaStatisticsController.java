package com.statistics.controller;

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
}
