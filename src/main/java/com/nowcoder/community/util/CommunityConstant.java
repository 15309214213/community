package com.nowcoder.community.util;



public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;
    /**
     * 激活重复
     */
    int ACTIVATION_REPEAT= 1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE= 2;


    /**
     * 登陆凭证超时时间
     */
    int DEFAULT_EXPIRED_SECONDS=3600*12;
    /**
     * 记住状态下的登录凭证超时时间
     */
    int REMEBER_EXPIRED_SECONDS=3600*24*100;


    // * 评论的对象：
    /**
     * 实体类型： 帖子
     */
    int ENTITY_TYPE_POST = 1;
    /**
     * 实体类型： 评论
     */
    int ENTITY_TYPE_COMMENT = 2;
    /**
     * 实体类型： 用户
     */
    int ENTITY_TYPE_USER = 3;


    /**
     * 主题：评论
     */
    String TOPIC_COMMENT = "comment";


    /**
     * 主题：点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 主题：关注
     */
    String TOPIC_FOLLOW = "follow";
    /**
     * 主题：关注
     */
    String TOPIC_PUBLISH = "publish";

    /**
     * 系统用户的Id
     *
     */
    int SYSTEM_USER_DI = 1;

    /**
     * 权限：普通用户
     */
    String  AUTHORITY_USER = "user";
    /**
     * 权限：普通管理员
     */
    String  AUTHORITY_ADMIN = "admin";
    /**
     * 权限：普通版主
     */
    String  AUTHORITY_MODERATOR ="moderator";

}
