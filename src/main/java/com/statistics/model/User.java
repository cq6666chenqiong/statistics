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
    private String payPasswordSal;	//	支付密码Salt,
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
    private Long 最后更新时间;
    private String inviteCode;	//	邀请码,
    private Integer orgId;	//	组织机构ID,
    private String orgCode;	//	组织机构内部编码,

}
