package com.statistics.service.statistics.impl;

import com.statistics.dao.CourseMgDao;
import com.statistics.service.statistics.CourseMgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class CourseMgServiceImpl implements CourseMgService {

    @Autowired
    private CourseMgDao courseMgDao;

    @Override
    public void delCourseMemberByCourseId(String courseId) {
        courseMgDao.delCourseMemberByCourseId(courseId);
    }

    @Override
    public void delUserScoreByCourseId(String courseId) {
        courseMgDao.delUserScoreByCourseId(courseId);
    }

    @Override
    public void delUserStatisticsScoreByCourseId(String courseId) {
        courseMgDao.delUserStatisticsScoreByCourseId(courseId);
    }

    @Override
    public void delUserStatisticsScore(Map delmap) {
        courseMgDao.delUserStatisticsScore(delmap);
    }

    @Override
    public void delCourseMember(Map map) {
        courseMgDao.delCourseMember(map);
    }

    @Override
    public void delUserScore(Map map) {
        courseMgDao.delUserScore(map);
    }

    @Override
    public void delCourseById(String courseId) {
        courseMgDao.delCourseById(courseId);
    }

    @Override
    public void delCourseChapterByCourseId(String courseId) {
        courseMgDao.delCourseChapterByCourseId(courseId);
    }

    @Override
    public void delCourseDraftByCourseId(String courseId) {
        courseMgDao.delCourseDraftByCourseId(courseId);
    }

    @Override
    public void delCourseExpriyByCourseId(String courseId) {
        courseMgDao.delCourseExpriyByCourseId(courseId);
    }

    @Override
    public void delCourseFavoriteByCourseId(String courseId) {
        courseMgDao.delCourseFavoriteByCourseId(courseId);
    }

    @Override
    public void delCourseLessonByCourseId(String courseId) {
        courseMgDao.delCourseLessonByCourseId(courseId);
    }

    @Override
    public void delCourseLessonExtendByCourseId(String courseId) {
        courseMgDao.delCourseLessonExtendByCourseId(courseId);
    }

    @Override
    public void delCourseLessonLearnByCourseId(String courseId) {
        courseMgDao.delCourseLessonLearnByCourseId(courseId);
    }

    @Override
    public void delCourseLessonReplayByCourseId(String courseId) {
        courseMgDao.delCourseLessonReplayByCourseId(courseId);
    }

    @Override
    public void delCourseLessonViewByCourseId(String courseId) {
        courseMgDao.delCourseLessonViewByCourseId(courseId);
    }

    @Override
    public void delCourseMaterialByCourseId(String courseId) {
        courseMgDao.delCourseMaterialByCourseId(courseId);
    }

    @Override
    public void delCourseNoteByCourseId(String courseId) {
        courseMgDao.delCourseNoteByCourseId(courseId);
    }

    @Override
    public void delCourseNoteLikeByCourseId(String courseId) {
        courseMgDao.delCourseNoteLikeByCourseId(courseId);
    }

    @Override
    public void delCourseReviewByCourseId(String courseId) {
        courseMgDao.delCourseReviewByCourseId(courseId);
    }

    @Override
    public void delCourseThreadByCourseId(String courseId) {
        courseMgDao.delCourseThreadByCourseId(courseId);
    }

    @Override
    public Map getTestPaperByTarget(String target) {

        return courseMgDao.getTestPaperByTarget(target);
    }

    @Override
    public void delTestPaperById(Long testId) {
        courseMgDao.delTestPaperById(testId);
    }

    @Override
    public void delTestPaperItemByTestId(Long testId) {
        courseMgDao.delTestPaperItemByTestId(testId);
    }

    @Override
    public void delTestPaperItemResultByTestId(Long testId) {
        courseMgDao.delTestPaperItemResultByTestId(testId);
    }

    @Override
    public void delTestPaperResultByTestId(Long testId) {
        courseMgDao.delTestPaperResultByTestId(testId);
    }
}
