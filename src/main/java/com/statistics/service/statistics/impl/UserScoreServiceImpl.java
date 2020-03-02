package com.statistics.service.statistics.impl;

import com.statistics.dao.UserScoreDao;
import com.statistics.model.UserScore;
import com.statistics.service.statistics.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserScoreServiceImpl implements UserScoreService {

    @Autowired
    private UserScoreDao userScoreDao;


    @Override
    public void addUserScore(UserScore userScore) {
        userScoreDao.addUserScore(userScore);
    }
}
