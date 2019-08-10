package com.statistics.dao;

import java.util.Map;

public interface CourseMgDao {

    void delCourseMemberByCourseId(String courseId);

    void delUserScoreByCourseId(String courseId);

    void delUserStatisticsScoreByCourseId(String courseId);

    void delUserStatisticsScore(Map delmap);

    void delCourseMember(Map map);

    void delUserScore(Map map);
}
