package com.statistics.dao;

import java.util.List;
import java.util.Map;

public interface MemScoreStatisticsDao {

    List<Map> getEndemicArea();

    List<Map> getProfessionalGroup();

    List<Map> getlevels();

    List<Map> getUserScoreNotAcsy();

    Map getLessonById(Map queryMap);

    Map getCourseById(Map queryMap);

    Map getTestPagerByMap(Map queryMap);

    List<Map> getUserStatisticsScores(Map queryMap);

    void updateUserStatisticsScores(Map queryMap);

    void addUserStatisticsScore(Map queryMap);

    Map getUserProfileById(Map queryMap);

    Map getUserById(Map queryMap);

    List<Map> getMemberCourse(Map coureMap);

    List<Map> getCoursesScoreMessage(Map coureMap);

    Map getUserScoreMessage(Map coureMap);

    List<Map<String,String>> getAllUser(Map userQqueryMap);

    Map<String,String> getMemberCourseScore(Map queryMap);

    Map<String, String> getUserTotalScore(Map queryMap);

    void addUserResult(Map map);

    List<Map<String, String>> getUserResult(Map map);

    void updateUserResult(Map map);

    List<Map<String, String>> getUserByPage(Map userQqueryMap);
}
