package com.nowcoder.community.entity;


//format key
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    //某个实体的赞
    //希望可以看到谁点的赞，因此可以存入userid
    //like: entity : entityType :entityID -> set(userId)
    public static String getEntityLikeKey(int entityType , int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
