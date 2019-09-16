package com.statistics.dao;

import java.util.Map;

public interface CourseMgDao {

    void delCourseMemberByCourseId(String courseId);

    void delUserScoreByCourseId(String courseId);

    void delUserStatisticsScoreByCourseId(String courseId);

    void delUserStatisticsScore(Map delmap);

    void delCourseMember(Map map);

    void delUserScore(Map map);

    void delCourseById(String courseId);

    void delCourseChapterByCourseId(String courseId);

    void delCourseDraftByCourseId(String courseId);

    void delCourseExpriyByCourseId(String courseId);

    void delCourseFavoriteByCourseId(String courseId);

    void delCourseLessonByCourseId(String courseId);

    void delCourseLessonExtendByCourseId(String courseId);

    void delCourseLessonLearnByCourseId(String courseId);

    void delCourseLessonReplayByCourseId(String courseId);

    void delCourseLessonViewByCourseId(String courseId);

    void delCourseMaterialByCourseId(String courseId);

    void delCourseNoteByCourseId(String courseId);

    void delCourseNoteLikeByCourseId(String courseId);

    void delCourseReviewByCourseId(String courseId);

    void delCourseThreadByCourseId(String courseId);

    Map getTestPaperByTarget(String target);

    void delTestPaperById(Long testId);

    void delTestPaperItemByTestId(Long testId);

    void delTestPaperItemResultByTestId(Long testId);

    void delTestPaperResultByTestId(Long testId);
}
