package com.statistics.model;

/**
 * 用户
 */
public class User {
    private Integer id;	//用户ID,
    private String email;	//	用户邮箱,
    private String verifiedMobile;	//	  `verifiedMobile` varchar(32) NOT NULL DEFAULT ,
    private String password;   //	用户密码,
    private String salt;	//	密码SALT,
    private String payPassword;	//	支付密码,
    private String payPasswordSalt;	//	支付密码Salt,
    private String uri;	//	用户URI,
    private String nickname;	//	工号
    private String title;	//	头像,
    private String tags;	//	标签,
    private String type;	//	default默认为网站注册, weibo新浪微薄登录,
    private Integer point;	//	积分,
    private Integer coin;	//	金币,
    private String smallAvatar;	//	小头像,
    private String mediumAvatar;	//	中头像,
    private String largeAvatar;	//	大头像,
    private Integer emailVerified;	//	邮箱是否为已验证,
    private Integer setup;	//	是否初始化设置的，未初始化的可以设置邮箱、用户名。,
    private String roles;	//	用户角色,
    private Integer promoted;	//	是否为推荐,
    private Integer promotedSeq;	//	`promotedSeq` int(10) unsigned NOT NULL DEFAULT 0,
    private Long promotedTime;	//	推荐时间,
    private Integer locked;	//	是否被禁止,
    private Integer lockDeadline;	//	帐号锁定期限,
    private Integer consecutivePasswordErrorTimes;	//	帐号密码错误次数,
    private Long lastPasswordFailTime;	//		`lastPasswordFailTime` int(10) NOT NULL DEFAULT 0,
    private Long loginTime;	//		最后登录时间,
    private String loginIp;	//	最后登录IP,
    private String loginSessionId;	//	最后登录会话ID,
    private Long approvalTime;	//		实名认证时间,
    private Integer approvalStatus;	//	实名认证状态,
    private Integer newMessageNum;//		未读私信数,
    private Integer newNotificationNum; //		未读消息数,
    private String createdIp;	//	注册IP,
    private Long createdTime;	//	注册时间,
    private Long updateTime;   //最后更新时间;
    private String inviteCode;	//	邀请码,
    private Integer orgId;	//	组织机构ID,
    private String orgCode;	//	组织机构内部编码,

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifiedMobile() {
        return verifiedMobile;
    }

    public void setVerifiedMobile(String verifiedMobile) {
        this.verifiedMobile = verifiedMobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getPayPasswordSalt() {
        return payPasswordSalt;
    }

    public void setPayPasswordSalt(String payPasswordSalt) {
        this.payPasswordSalt = payPasswordSalt;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public String getSmallAvatar() {
        return smallAvatar;
    }

    public void setSmallAvatar(String smallAvatar) {
        this.smallAvatar = smallAvatar;
    }

    public String getMediumAvatar() {
        return mediumAvatar;
    }

    public void setMediumAvatar(String mediumAvatar) {
        this.mediumAvatar = mediumAvatar;
    }

    public String getLargeAvatar() {
        return largeAvatar;
    }

    public void setLargeAvatar(String largeAvatar) {
        this.largeAvatar = largeAvatar;
    }

    public Integer getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Integer emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Integer getSetup() {
        return setup;
    }

    public void setSetup(Integer setup) {
        this.setup = setup;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Integer getPromoted() {
        return promoted;
    }

    public void setPromoted(Integer promoted) {
        this.promoted = promoted;
    }

    public Integer getPromotedSeq() {
        return promotedSeq;
    }

    public void setPromotedSeq(Integer promotedSeq) {
        this.promotedSeq = promotedSeq;
    }

    public Long getPromotedTime() {
        return promotedTime;
    }

    public void setPromotedTime(Long promotedTime) {
        this.promotedTime = promotedTime;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public Integer getLockDeadline() {
        return lockDeadline;
    }

    public void setLockDeadline(Integer lockDeadline) {
        this.lockDeadline = lockDeadline;
    }

    public Integer getConsecutivePasswordErrorTimes() {
        return consecutivePasswordErrorTimes;
    }

    public void setConsecutivePasswordErrorTimes(Integer consecutivePasswordErrorTimes) {
        this.consecutivePasswordErrorTimes = consecutivePasswordErrorTimes;
    }

    public Long getLastPasswordFailTime() {
        return lastPasswordFailTime;
    }

    public void setLastPasswordFailTime(Long lastPasswordFailTime) {
        this.lastPasswordFailTime = lastPasswordFailTime;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(String loginSessionId) {
        this.loginSessionId = loginSessionId;
    }

    public Long getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Long approvalTime) {
        this.approvalTime = approvalTime;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Integer getNewMessageNum() {
        return newMessageNum;
    }

    public void setNewMessageNum(Integer newMessageNum) {
        this.newMessageNum = newMessageNum;
    }

    public Integer getNewNotificationNum() {
        return newNotificationNum;
    }

    public void setNewNotificationNum(Integer newNotificationNum) {
        this.newNotificationNum = newNotificationNum;
    }

    public String getCreatedIp() {
        return createdIp;
    }

    public void setCreatedIp(String createdIp) {
        this.createdIp = createdIp;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
