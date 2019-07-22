package com.statistics.task;

import com.statistics.service.statistics.MemScoreStatisticsService;
import com.statistics.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class CalculateScoreTask {

    @Autowired
    private MemScoreStatisticsService memScoreStatisticsService;

    private BigDecimal standardScore = new BigDecimal(1);

    @Scheduled(cron = "* 0/5 * * * ?")
    public void run(){
        System.out.println("start=================task");
        Date date = new Date();
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
        //System.out.println("==================go on ==========================");
        List<Map<String,String>> memberList = memScoreStatisticsService.getAllUser();
        for(int i=0;i<memberList.size();i++){
            Map<String,String> map = memberList.get(i);
            String userId = StringUtil.getString(map.get("id"));
            String groups = map.get("professional_groups");
            List<Map> userScoreList = memScoreStatisticsService.getUserStatisticsScores(map);
            calculateScore(professionnalMap,levelMap,map,professionnalNameMap,levelNameMap,date);
        }
        calculatePass();
        calculateResult();

        System.out.println("end=================task");
    }


    public void calculateResult(){
        List<Map<String,String>> userList = memScoreStatisticsService.getAllUser();
        for(int i=0;i<userList.size();i++) {
            Map<String, String> user = userList.get(i);
            String userId = StringUtil.getString(user.get("id"));
            Map queryMap = new HashMap();
            queryMap.put("userId", userId);
            queryMap.put("year", "2019");
            Map<String, String> score = memScoreStatisticsService.getUserTotalScore(queryMap);

            queryMap.put("beginTime", 1546272000L);
            queryMap.put("endTime", 1577808000L);

            //获取用户当前年份所学的课程
            List<Map> list = memScoreStatisticsService.getUserStatisticsScores(queryMap);
            boolean ispass = true;
            BigDecimal sumScore = new BigDecimal(0);
            for (int j = 0; j < list.size(); j++) {
                Map map = list.get(j);
                String ispassStr = StringUtil.getString(map.get("ispass"));
                String totalScore = StringUtil.getString(map.get("total_score"));
                sumScore = sumScore.add(new BigDecimal(totalScore));
                if (ispassStr.equals("0")) {
                    ispass = false;
                }
            }

            if (sumScore.compareTo(standardScore) < 0) {
                ispass = false;
            }

            if (ispass) {
                Map map = new HashMap();
                map.put("userId", userId);
                map.put("status", 1);
                map.put("year", "2019");
                List<Map<String, String>> resultlist = memScoreStatisticsService.getUserResult(map);
                if (resultlist != null && resultlist.size() > 0) {
                    memScoreStatisticsService.updateUserResult(map);
                } else {
                    memScoreStatisticsService.addUserResult(map);
                }

            } else {
                Map map = new HashMap();
                map.put("userId", userId);
                map.put("status", 0);
                map.put("year", "2019");
                List<Map<String, String>> resultlist = memScoreStatisticsService.getUserResult(map);
                if (resultlist != null && resultlist.size() > 0) {
                    memScoreStatisticsService.updateUserResult(map);
                } else {
                    memScoreStatisticsService.addUserResult(map);
                }
            }
        }
    }

    public void calculatePass(){
        Map queryMap = new HashMap();
        List<Map> userScoreList = memScoreStatisticsService.getUserStatisticsScores(queryMap);

        for(int i=0;i<userScoreList.size();i++){
            Map map = userScoreList.get(i);
            String userId = StringUtil.getString(map.get("userId"));
            String type = StringUtil.getString(map.get("type"));
            Map coureMap = new HashMap();
            coureMap.put("userId",userId);
            coureMap.put("type",type);
            List<Map> list = memScoreStatisticsService.getMemberCourse(coureMap);
            Integer learnCount = list.size();
            Integer passCount = 0;
            int ispass = 0;
            for(int j=0;j<list.size();j++){

                BigDecimal passScore = new BigDecimal(StringUtil.getString(list.get(j).get("giveCredit")));
                coureMap.put("courseId",list.get(j).get("courseId"));
                Map cm = memScoreStatisticsService.getUserScoreMessage(coureMap);
                BigDecimal userScore = new BigDecimal(0);
                if(cm != null && cm.get("score") != null){
                    userScore = new BigDecimal(StringUtil.getString(cm.get("score")));
                }
                if(userScore.compareTo(passScore)>=0){
                    //System.out.println(list.get(j).get("courseId")+"============通过");
                    passCount++;
                }
            }
            if(learnCount == passCount && learnCount > 0 && passCount > 0){

                System.out.println("ispass==="+ispass);
                ispass = 1;
            }
            String passmark = passCount + "/" + learnCount;
            Map updateMap = new HashMap();
            updateMap.put("userId",userId);
            updateMap.put("type",type);
            updateMap.put("year","2019");
            updateMap.put("passmark",passmark);
            updateMap.put("totalnum",learnCount);
            updateMap.put("passnum",passCount);
            updateMap.put("ispass",ispass);
            memScoreStatisticsService.updateUserStatisticsScores(updateMap);

        }
    }


    public void calculateScore(Map<Integer,String> professionnalMap,Map<Integer,String> levelMap,Map<String,String> member,Map<String,Integer> professionnalNameMap,Map<String,Integer> levelNameMap,Date date){
        String userId = StringUtil.getString(member.get("id"));
        Integer cengji = Integer.valueOf(StringUtil.getString(member.get("cengji")).trim());
        Map queryMap = new HashMap();
        queryMap.put("userId",userId);
        queryMap.put("beginTime",1546272000L);
        queryMap.put("endTime",1577808000L);
        /* coureMap.put("userId",userId);
        coureMap.put("type",type);*/
        String groups = StringUtil.getString(member.get("professional_groups"));
        if(groups.equals("")){
            groups = "其他";
        }
        String[] groupStrs = groups.split(",");
        Set<Integer> set = new HashSet<Integer>();
        for(int i=0;i<groupStrs.length;i++){
            set.add(professionnalNameMap.get(groupStrs[i]));
        }
        set.add(cengji);
        //获取用户当前年份所学的课程
        List<Map> list = memScoreStatisticsService.getMemberCourse(queryMap);
        Map<Integer,Map> fenMap = new HashMap<Integer,Map>();
        for(int i=0;i<list.size();i++){
            //System.out.println("==================go on =========================="+userId);
            Map membercourse = list.get(i);
            String courseId = StringUtil.getString(membercourse.get("courseId"));
            queryMap.put("courseId",courseId);
            Map course = memScoreStatisticsService.getCourseById(queryMap);
            if(course == null) continue;
            String courseType = StringUtil.getString(course.get("buyable"));
            if(courseType.equals("")) continue;
            Integer ct = Integer.valueOf(courseType);
            queryMap.put("userId",userId);
            queryMap.put("year","2019");
            queryMap.put("courseId",courseId);
            Map<String,String> courseScore = memScoreStatisticsService.getMemberCourseScore(queryMap);
            String courseTypeName = "";
            if(ct < 30){
                courseTypeName = levelMap.get(ct);
            }else{
                courseTypeName = professionnalMap.get(ct);
            }
            String thisscore = "0";
            if(courseScore == null){
                //System.out.println("得分0===========");
                thisscore = "0";
            }else{//System.out.println("得分" + StringUtil.getString(courseScore.get("score")) + "===========");

                thisscore = StringUtil.getString(courseScore.get("score"));
            }

            if(fenMap.keySet().contains(ct)){
                String score = StringUtil.getString(fenMap.get(ct).get("totalScore"));
                BigDecimal s = new BigDecimal(score).add(new BigDecimal(thisscore));
                fenMap.get(ct).put("totalScore",s);
            }else{
                Map fMap = new HashMap();
                BigDecimal s = new BigDecimal(thisscore);
                fMap.put("id", UUID.randomUUID().toString());
                fMap.put("userId",userId);
                fMap.put("type",ct);
                fMap.put("typeName",courseTypeName);
                fMap.put("totalScore",s);
                fMap.put("year","2019");
                fMap.put("nickname",member.get("nickname"));
                fMap.put("endemicArea",member.get("endemic_area"));

                //fMap.put("totalScore",s);
                fenMap.put(ct,fMap);
            }

        }
        /*
        Iterator<Integer> its = set.iterator();
        while(its.hasNext()){
            Integer key = its.next();
            String courseTypeName = "";
            if(key == null) continue;
            if(key < 30){
                courseTypeName = levelMap.get(key);
            }else{
                courseTypeName = professionnalMap.get(key);
            }
            if(fenMap.keySet().contains(key)){
                continue;
            }else{
                Map fMap = new HashMap();
                BigDecimal s = new BigDecimal(0);
                fMap.put("id", UUID.randomUUID().toString());
                fMap.put("userId",userId);
                fMap.put("type",key);
                fMap.put("typeName",courseTypeName);
                fMap.put("totalScore",s);
                fMap.put("year","2019");
                fMap.put("nickname",member.get("nickname"));
                fMap.put("endemicArea",member.get("endemic_area"));
                fenMap.put(key,fMap);
            }
        }
        */
        //System.out.println(JSON.toJSONString(fenMap));
        Iterator<Integer> is = fenMap.keySet().iterator();
        while(is.hasNext()){
            Integer key = is.next();
            Map scoreMap =fenMap.get(key);
            List userScores = memScoreStatisticsService.getUserStatisticsScores(scoreMap);
            System.out.println("个数"+userScores.size());
            if(userScores != null && userScores.size() > 0){
                System.out.println("修改");
                memScoreStatisticsService.updateUserStatisticsScores(scoreMap);
            }else{
                System.out.println("增加");
                memScoreStatisticsService.addUserStatisticsScore(scoreMap);
            }
        }
        //Map useScoreMap = memScoreStatisticsService.getUserScore(queryMap);
    }
}