package com.nowcoder.community.util;


//format key
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKTE = "ticket";
    private static final String PREFIX_USER = "user";

    //某个实体的赞
    //希望可以看到谁点的赞，因此可以存入userid
    //like: entity : entityType :entityID -> set(userId)
    public static String getEntityLikeKey(int entityType , int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    //某个用户的赞的数量
    //like:user:userId -> int
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //某个用户关注的实体
    //followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }


    //某个实体拥有的粉丝
    //follower:entityType:entityId - > zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //登录验证码
    //由于登陆时还没有用户传入，因此需要服务器传给浏览器一个cookie来标识某用户的验证码
    public static String getKaptcha(String owner){

        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    //登陆凭证
    public static String getTicketKey(String ticket){
        return PREFIX_TICKTE + SPLIT + ticket;
    }

    //用户
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }

}

