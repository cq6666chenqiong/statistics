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
}
