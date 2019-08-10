package com.statistics.task;

import com.alibaba.fastjson.JSON;
import com.statistics.service.statistics.CourseMgService;
import com.statistics.service.statistics.MemScoreStatisticsService;
import com.statistics.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CalculateScoreTask {

    @Autowired
    private MemScoreStatisticsService memScoreStatisticsService;

    @Autowired
    private CourseMgService courseMgService;

    private BigDecimal standardScore = new BigDecimal(13);

    /*@Scheduled(cron = "0/10 * * * * ?")*/
    public void run() throws Exception {
        System.out.println("start=================task");



        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String time = sd.format(date);
        String year = time.split("-")[0];

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR,Integer.valueOf(year));
        calendar.roll(Calendar.DAY_OF_YEAR,-1);
        Date currYearLast = calendar.getTime();
        String currYearLastday = sd.format(currYearLast);
        System.out.println(time + "============" + currYearLastday);
        if(time.equals(currYearLastday)){
            return;
        }


        Date beginDate = sd.parse(year + "-01-01 00:00:00");
        Date endDate = sd.parse(year + "-12-31 23:59:59");
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        long beginTime = cal.getTimeInMillis()/1000;
        cal.setTime(endDate);
        long endTime = cal.getTimeInMillis()/1000;
        Map delmap = new HashMap();
        delmap.put("year",year);
        courseMgService.delUserStatisticsScore(delmap);

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
        Map userQqueryMap = new HashMap();
        List<Map<String,String>> memberList = memScoreStatisticsService.getAllUser(userQqueryMap);
        for(int i=0;i<memberList.size();i++){
            Map<String,String> map = memberList.get(i);
            String userId = StringUtil.getString(map.get("id"));
            String groups = map.get("professional_groups");
            List<Map> userScoreList = memScoreStatisticsService.getUserStatisticsScores(map);
            System.out.println("==================calculateScore==========================");
            calculateScore(professionnalMap,levelMap,map,professionnalNameMap,levelNameMap,beginTime,endTime,year);
        }
        calculatePass(beginTime,endTime,year);
        calculateResult(year,beginTime,endTime);

        System.out.println("end=================task");
    }


    public void calculateResult(String year,long beginTime,long endTime){
        Map userQqueryMap = new HashMap();
        List<Map<String,String>> userList = memScoreStatisticsService.getAllUser(userQqueryMap);
        for(int i=0;i<userList.size();i++) {
            Map<String, String> user = userList.get(i);
            String userId = StringUtil.getString(user.get("id"));
            Map queryMap = new HashMap();
            queryMap.put("userId", userId);
            queryMap.put("year", year);
            Map<String, String> score = memScoreStatisticsService.getUserTotalScore(queryMap);

            queryMap.put("beginTime", beginTime);
            queryMap.put("endTime", endTime);

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
                map.put("total_score", sumScore);
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
                map.put("total_score", sumScore);
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

    public void calculatePass(long beginTime,long endTime,String year){
        Map queryMap = new HashMap();
        List<Map> userScoreList = memScoreStatisticsService.getUserStatisticsScores(queryMap);

        for(int i=0;i<userScoreList.size();i++){
            Map map = userScoreList.get(i);
            String userId = StringUtil.getString(map.get("userId"));
            String type = StringUtil.getString(map.get("type"));
            Map coureMap = new HashMap();
            coureMap.put("userId",userId);
            coureMap.put("type",type);
            if(type.equals("34") || type.equals("1001")){
                List<Map> list = memScoreStatisticsService.getUserStatisticsScores(coureMap);
                Map updateMap = new HashMap();
                updateMap.put("userId",userId);
                updateMap.put("type",type);
                updateMap.put("year",year);
                updateMap.put("passmark",list.size() + "/" + list.size());
                updateMap.put("totalnum",list.size());
                updateMap.put("passnum",list.size());
                updateMap.put("ispass",1);
                memScoreStatisticsService.updateUserStatisticsScores(updateMap);
            }else{
                coureMap.put("beginTime",beginTime);
                coureMap.put("endTime",endTime);
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
                    ispass = 1;
                }
                String passmark = passCount + "/" + learnCount;
                Map updateMap = new HashMap();
                updateMap.put("userId",userId);
                updateMap.put("type",type);
                updateMap.put("year",year);
                updateMap.put("passmark",passmark);
                updateMap.put("totalnum",learnCount);
                updateMap.put("passnum",passCount);
                updateMap.put("ispass",ispass);
                memScoreStatisticsService.updateUserStatisticsScores(updateMap);
            }

        }
    }


    public void calculateScore(Map<Integer,String> professionnalMap,Map<Integer,String> levelMap,Map<String,String> member,Map<String,Integer> professionnalNameMap,Map<String,Integer> levelNameMap,long beginTime,long endTime,String year){
        String userId = StringUtil.getString(member.get("id"));
        Integer cengji = Integer.valueOf(StringUtil.getString(member.get("cengji")).trim());
        Map queryMap = new HashMap();
        queryMap.put("userId",userId);
        queryMap.put("beginTime",beginTime);
        queryMap.put("endTime",endTime);

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
        //获取用户当前年份所学的课程      计算线上课程
        List<Map> list = memScoreStatisticsService.getMemberCourse(queryMap);
        System.out.println(userId+"==有======"+list.size()+"个==========="+beginTime+"===="+endTime);
        Map<Integer,Map> fenMap = new HashMap<Integer,Map>();
        //每个课程进行计算
        for(int i=0;i<list.size();i++){
            System.out.println("==================go on =========================="+userId);
            Map membercourse = list.get(i);
            String courseId = StringUtil.getString(membercourse.get("courseId"));
            queryMap.put("courseId",courseId);
            Map course = memScoreStatisticsService.getCourseById(queryMap);
            if(course == null) continue;
            String courseType = StringUtil.getString(course.get("buyable"));
            if(courseType.equals("")) continue;
            Integer ct = Integer.valueOf(courseType);
            queryMap.put("userId",userId);
            queryMap.put("year",year);
            queryMap.put("courseId",courseId);
            //计算每个课程下的课时总学分，就是这门课程的分数
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
            }else{System.out.println("得分" + StringUtil.getString(courseScore.get("score")) + "===========");

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
                fMap.put("year",year);
                fMap.put("nickname",member.get("nickname"));
                fMap.put("endemicArea",member.get("endemic_area"));

                //fMap.put("totalScore",s);
                fenMap.put(ct,fMap);
            }

        }

        queryMap.put("userId",userId);
        queryMap.put("year",year);
        queryMap.put("courseId",-1);
        Map<String,String> downLevelcourseScore = memScoreStatisticsService.getMemberCourseScore(queryMap);
        if(downLevelcourseScore != null && downLevelcourseScore.size() > 0){
            Map fMap = new HashMap();
            BigDecimal s = new BigDecimal(StringUtil.getString(downLevelcourseScore.get("score")));
            fMap.put("id", UUID.randomUUID().toString());
            fMap.put("userId",userId);
            fMap.put("type",34);
            fMap.put("typeName","线下层级课程");
            fMap.put("totalScore",s);
            fMap.put("year",year);
            fMap.put("nickname",member.get("nickname"));
            fMap.put("endemicArea",member.get("endemic_area"));

            //fMap.put("totalScore",s);
            fenMap.put(34,fMap);
        }

        queryMap.put("userId",userId);
        queryMap.put("year",year);
        queryMap.put("courseId",-2);
        Map<String,String> downProfessionalcourseScore = memScoreStatisticsService.getMemberCourseScore(queryMap);
        if(downProfessionalcourseScore != null && downProfessionalcourseScore.size() > 0){
            Map fMap = new HashMap();
            BigDecimal s = new BigDecimal(StringUtil.getString(downProfessionalcourseScore.get("score")));
            fMap.put("id", UUID.randomUUID().toString());
            fMap.put("userId",userId);
            fMap.put("type",1001);
            fMap.put("typeName","线下专业课程");
            fMap.put("totalScore",s);
            fMap.put("year",year);
            fMap.put("nickname",member.get("nickname"));
            fMap.put("endemicArea",member.get("endemic_area"));

            //fMap.put("totalScore",s);
            fenMap.put(1001,fMap);
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
        System.out.println(JSON.toJSONString(fenMap));
        Iterator<Integer> is = fenMap.keySet().iterator();
        while(is.hasNext()){
            Integer key = is.next();
            Map scoreMap =fenMap.get(key);
            List userScores = memScoreStatisticsService.getUserStatisticsScores(scoreMap);
            //System.out.println("个数"+userScores.size());
            if(userScores != null && userScores.size() > 0){
                //System.out.println("修改");
                memScoreStatisticsService.updateUserStatisticsScores(scoreMap);
            }else{
                //System.out.println("增加");
                memScoreStatisticsService.addUserStatisticsScore(scoreMap);
            }
        }

        System.out.println(beginTime);
        System.out.println(endTime);
        //Map useScoreMap = memScoreStatisticsService.getUserScore(queryMap);
    }
}
