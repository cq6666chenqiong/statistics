package com.statistics.service.statistics;

import com.statistics.model.ExcelUser;
import com.statistics.model.ExcelUserProfile;
import com.statistics.model.User;
import com.statistics.model.UserProfile;

public interface UserProfileService {
    public void addUserProfile(UserProfile userProfile);
    public void addExcelUserProfile(ExcelUserProfile userProfile);
}
