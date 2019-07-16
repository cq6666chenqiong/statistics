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
            Map userProfile = memScoreStatisticsService.getUserProfileById(queryMap);
            Map user = memScoreStatisticsService.getUserById(queryMap);
            if(user == null || userProfile == null) continue;
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
            String nickname = StringUtil.getString(user.get("nickname"));
            String endemicArea = StringUtil.getString(userProfile.get("varcharField3"));
            if(endemicArea==null || endemicArea.equals("")){
                endemicArea = "其他";
            }
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
            map.put("nickname",nickname);
            map.put("endemicArea",endemicArea);
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


        //统计按照层级和小组课程通过率 （查询会员所选课程需要增加年度时间戳限制）
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
            for(int j=0;j<list.size();j++){

                BigDecimal passScore = new BigDecimal(StringUtil.getString(list.get(j).get("giveCredit")));
                coureMap.put("courseId",list.get(j).get("courseId"));
                Map cm = memScoreStatisticsService.getUserScoreMessage(coureMap);
                BigDecimal userScore = new BigDecimal(0);
                if(cm != null && cm.get("score") != null){
                    userScore = new BigDecimal(StringUtil.getString(cm.get("score")));
                }
                if(userScore.compareTo(passScore)>=0){
                    System.out.println(list.get(j).get("courseId")+"============通过");
                    passCount++;
                }
            }

            String passmark = passCount + "/" + learnCount;
            Map updateMap = new HashMap();
            updateMap.put("userId",userId);
            updateMap.put("type",type);
            updateMap.put("year","2019");
            updateMap.put("passmark",passmark);
            memScoreStatisticsService.updateUserStatisticsScores(updateMap);

        }

        return JSON.toJSONString(userScorelist);
    }

    @RequestMapping(value = "/makeMemberStatistics")
    @ResponseBody
    public String makeMemberStatistics(){
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
        System.out.println("==================go on ==========================");
        List<Map<String,String>> memberList = memScoreStatisticsService.getAllUser();
        for(int i=0;i<memberList.size();i++){
            Map<String,String> map = memberList.get(i);
            String userId = StringUtil.getString(map.get("id"));
            String groups = map.get("professional_groups");
            List<Map> userScoreList = memScoreStatisticsService.getUserStatisticsScores(map);
            calculateScore(professionnalMap,levelMap,map,professionnalNameMap,levelNameMap);
        }
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
            for(int j=0;j<list.size();j++){

                BigDecimal passScore = new BigDecimal(StringUtil.getString(list.get(j).get("giveCredit")));
                coureMap.put("courseId",list.get(j).get("courseId"));
                Map cm = memScoreStatisticsService.getUserScoreMessage(coureMap);
                BigDecimal userScore = new BigDecimal(0);
                if(cm != null && cm.get("score") != null){
                    userScore = new BigDecimal(StringUtil.getString(cm.get("score")));
                }
                if(userScore.compareTo(passScore)>=0){
                    System.out.println(list.get(j).get("courseId")+"============通过");
                    passCount++;
                }
            }

            String passmark = passCount + "/" + learnCount;
            Map updateMap = new HashMap();
            updateMap.put("userId",userId);
            updateMap.put("type",type);
            updateMap.put("year","2019");
            updateMap.put("passmark",passmark);
            memScoreStatisticsService.updateUserStatisticsScores(updateMap);

        }
        return "ok";
    }

    public void calculateScore(Map<Integer,String> professionnalMap,Map<Integer,String> levelMap,Map<String,String> member,Map<String,Integer> professionnalNameMap,Map<String,Integer> levelNameMap){
        String userId = StringUtil.getString(member.get("id"));
        Integer cengji = Integer.valueOf(StringUtil.getString(member.get("cengji")).trim());
        Map queryMap = new HashMap();
        queryMap.put("userId",userId);
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
                System.out.println("得分0===========");
                thisscore = "0";
            }else{
                System.out.println("得分" + StringUtil.getString(courseScore.get("score")) + "===========");
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
        System.out.println(JSON.toJSONString(fenMap));
        Iterator<Integer> is = fenMap.keySet().iterator();
        while(is.hasNext()){
            Integer key = is.next();
            Map scoreMap =fenMap.get(key);
            memScoreStatisticsService.addUserStatisticsScore(scoreMap);
        }
        //Map useScoreMap = memScoreStatisticsService.getUserScore(queryMap);
    }

    public static void main(String[] args) {

    }

}