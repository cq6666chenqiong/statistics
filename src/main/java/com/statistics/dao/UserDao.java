package com.statistics.dao;

import com.statistics.model.ExcelUser;
import com.statistics.model.User;

public interface UserDao {

    void addUser(User user);

    void addExcelUser(ExcelUser user);

    Integer getMaxId();
}
