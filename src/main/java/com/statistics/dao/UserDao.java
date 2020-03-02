package com.statistics.dao;

import com.statistics.model.ExcelUser;
import com.statistics.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    void addUser(User user);

    void addExcelUser(ExcelUser user);

    Integer getMaxId();

    Map getUser(User user);

    List<Map> getUserDetail(Map map);
}
