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
}
