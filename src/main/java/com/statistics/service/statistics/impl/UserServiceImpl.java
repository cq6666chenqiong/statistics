package com.statistics.service.statistics.impl;

import com.statistics.dao.UserDao;
import com.statistics.model.ExcelUser;
import com.statistics.model.User;
import com.statistics.service.statistics.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer getMaxId() {
        return userDao.getMaxId();
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public void addExcelUser(ExcelUser user) {
        userDao.addExcelUser(user);
    }

    @Override
    public Map getUser(User user) {
        return userDao.getUser(user);
    }

    @Override
    public List<Map> getUserDetail(Map map) {
        return userDao.getUserDetail(map);
    }
}
