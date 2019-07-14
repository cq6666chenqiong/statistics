package com.statistics.service.statistics.impl;

import com.statistics.dao.MemScoreStatisticsDao;
import com.statistics.service.statistics.MemScoreStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MemScoreStatisticsServiceImpl implements MemScoreStatisticsService {

    @Autowired
    private MemScoreStatisticsDao memScoreStatisticsDao;

    @Override
    public List<Map> getProfessionalGroup() {
        return memScoreStatisticsDao.getProfessionalGroup();
    }

    @Override
    public List<Map> getEndemicArea() {
        return memScoreStatisticsDao.getEndemicArea();
    }

    @Override
    public List<Map> getlevels() {
        return memScoreStatisticsDao.getlevels();
    }

    @Override
    public List<Map> getUserScoreNotAcsy() {
        return memScoreStatisticsDao.getUserScoreNotAcsy();
    }

    @Override
    public Map getCourseById(Map queryMap) {
        return memScoreStatisticsDao.getCourseById(queryMap);
    }

    @Override
    public Map getLessonById(Map queryMap) {
        return memScoreStatisticsDao.getLessonById(queryMap);
    }

    @Override
    public Map getTestPagerByMap(Map queryMap) {
        return memScoreStatisticsDao.getTestPagerByMap(queryMap);
    }

    @Override
    public List<Map> getUserStatisticsScores(Map queryMap) {
        return memScoreStatisticsDao.getUserStatisticsScores(queryMap);
    }

    @Override
    public void updateUserStatisticsScores(Map queryMap) {
        memScoreStatisticsDao.updateUserStatisticsScores(queryMap);
    }

    @Override
    public void addUserStatisticsScore(Map queryMap) {
        memScoreStatisticsDao.addUserStatisticsScore(queryMap);
    }
}
