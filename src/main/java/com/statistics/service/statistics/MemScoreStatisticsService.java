package com.statistics.service.statistics;

import java.util.List;
import java.util.Map;

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

    List<Map<String,String>> getAllUser();

    Map<String,String> getMemberCourseScore(Map queryMap);

    Map<String, String> getUserTotalScore(Map queryMap);

    void addUserResult(Map map);

    List<Map<String, String>> getUserResult(Map map);

    void updateUserResult(Map map);

    List<Map<String, String>> getUserByPage(Map userQqueryMap);
}
