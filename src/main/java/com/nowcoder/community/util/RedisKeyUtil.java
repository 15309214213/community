package com.nowcoder.community.util;


//format key
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";

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
}

