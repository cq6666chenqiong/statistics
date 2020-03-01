package com.statistics.service.statistics.impl;

import com.statistics.dao.UserDao;
import com.statistics.dao.UserProfileDao;
import com.statistics.model.ExcelUser;
import com.statistics.model.ExcelUserProfile;
import com.statistics.model.User;
import com.statistics.model.UserProfile;
import com.statistics.service.statistics.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileDao userProfileDao;

    @Override
    public void addUserProfile(UserProfile userProfile) {
        userProfileDao.addUserProfile(userProfile);
    }

    @Override
    public void addExcelUserProfile(ExcelUserProfile userProfile) {
        userProfileDao.addExcelUserProfile(userProfile);
    }
}
