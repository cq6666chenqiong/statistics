package com.statistics.service.statistics;

import com.statistics.model.ExcelUser;
import com.statistics.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public Integer getMaxId();
    public void addUser(User user);
    public void addExcelUser(ExcelUser user);
    public Map getUser(User user);
    public List<Map> getUserDetail(Map map);
}
