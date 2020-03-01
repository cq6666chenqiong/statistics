package com.statistics.dao;

import com.statistics.model.ExcelUserProfile;
import com.statistics.model.UserProfile;

public interface UserProfileDao {
    public void addExcelUserProfile(ExcelUserProfile userProfile);

    public void addUserProfile(UserProfile userProfile);
}
