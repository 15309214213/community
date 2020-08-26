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

}
