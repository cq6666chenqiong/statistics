package com.statistics.service.statistics;

import com.statistics.model.ExcelUser;
import com.statistics.model.User;

public interface UserService {
    public Integer getMaxId();
    public void addUser(User user);
    public void addExcelUser(ExcelUser user);
}
