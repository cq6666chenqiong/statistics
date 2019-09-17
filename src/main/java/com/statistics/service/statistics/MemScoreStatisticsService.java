package com.statistics.service.statistics;

import com.alibaba.fastjson.JSON;
import com.statistics.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public interface MemScoreStatisticsService {

    public List<Map> getProfessionalGroup();

    public List<Map> getEndemicArea();

    List<Map> getlevels();

    List<Map> getUserScoreNotAcsy();

    Map getCourseById(Map queryMap);

    Map getLessonById(Map queryMap);

    Map getTestPagerByMap(Map queryMap);

    List<Map> getUserStatisticsScores(Map queryMap);

    void updateUserStatisticsScores(Map queryMap);

    void addUserStatisticsScore(Map queryMap);

    Map getUserProfileById(Map queryMap);

    Map getUserById(Map queryMap);

    List<Map> getCoursesScoreMessage(Map coureMap);

    List<Map> getMemberCourse(Map coureMap);

    Map getUserScoreMessage(Map coureMap);

    List<Map<String,String>> getAllUser(Map userQqueryMap);

    Map<String,String> getMemberCourseScore(Map queryMap);

    Map<String, String> getUserTotalScore(Map queryMap);

    void addUserResult(Map map);

    List<Map<String, String>> getUserResult(Map map);

    void updateUserResult(Map map);

    List<Map<String, String>> getUserByPage(Map userQqueryMap);

    List<Map<String, String>> getMemberDetailScore(Map queryMap);

    @RequestMapping(value = "/getEndemicAreaStatistics")
    @ResponseBody
    default String getEndemicAreaStatistics(HttpServletRequest request, HttpServletResponse response){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String year = StringUtil.getString(request.getParameter("year"));
        if(year == null || year.equals("")){
            Date date = new Date();
            year = df.format(date).split("-")[0];
        }
        List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
        List<Map> endemicArealist = getEndemicArea();
        List<Map> professionnallist = getProfessionalGroup();
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
        List<Map> levellist = getlevels();
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

        Integer a = 0;
        Integer b = 0;
        for(int i = 0;i<endemicArealist.size();i++){
            Map<String,String> resultmap = new HashMap<String,String>();
            Map map = endemicArealist.get(i);
            String area = StringUtil.getString(map.get("area"));
            Map queryMap = new HashMap();
            queryMap.put("year",year);
            queryMap.put("area",area);
            List userScores = getUserStatisticsScores(queryMap);
            System.out.print(area+"       "+userScores.size());
            Iterator<Integer> levelits = levelMap.keySet().iterator();
            resultmap.put("area",area);
            while(levelits.hasNext()){
                Integer type = levelits.next();
                queryMap.put("type",type);
                queryMap.put("ispass",0);
                List userScoresLevela = getUserStatisticsScores(queryMap);
                queryMap.put("ispass",1);
                List userScoresLevelb = getUserStatisticsScores(queryMap);
                System.out.print("       "+userScoresLevelb.size()+"/"+(userScoresLevela.size()+userScoresLevelb.size()));
                resultmap.put(type+"",userScoresLevelb.size()+"/"+(userScoresLevela.size()+userScoresLevelb.size()));
                a = a + userScoresLevelb.size();
                b = b + userScoresLevela.size() + userScoresLevelb.size();
            }
            if(b==0){
                resultmap.put("levelresult","0.00%");
            }else{
                resultmap.put("levelresult",new BigDecimal(a).divide(new BigDecimal(b),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)+"%");
            }

            a = 0;
            b = 0;

            Iterator<Integer> professionnalits = professionnalMap.keySet().iterator();
            while(professionnalits.hasNext()){
                Integer type = professionnalits.next();
                queryMap.put("type",type);
                queryMap.put("ispass",0);
                List userScoresProa = getUserStatisticsScores(queryMap);
                queryMap.put("ispass",1);
                List userScoresProb = getUserStatisticsScores(queryMap);
                System.out.print("       "+userScoresProb.size()+"/"+(userScoresProa.size() + userScoresProb.size()));
                resultmap.put(type+"",userScoresProb.size()+"/"+(userScoresProa.size() + userScoresProb.size()));
            }
            if(b==0){
                resultmap.put("professionnalresult","0.00%");
            }else{
                resultmap.put("professionnalresult",new BigDecimal(a).divide(new BigDecimal(b),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)+"%");
            }

            resultList.add(resultmap);
            System.out.println();
        }

        return JSON.toJSONString(resultList);

    }
}
