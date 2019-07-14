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
}
