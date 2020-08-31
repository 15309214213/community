package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId, int entityType, int entityId, int entityUserId){

//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
//            //查询该用户是否点过赞
//            boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey,userId);
//            if (isMember){
//                redisTemplate.opsForSet().remove(entityLikeKey,userId);
//            }else {
//                redisTemplate.opsForSet().add(entityLikeKey,userId);
//            }


        //有不止一个操作的事务，最好使用编程式事务进行回滚
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                boolean isMamber = operations.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();

                if (isMamber){
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });

    }


    //查询实体点赞数量
    public long findEntityLikeCount(int entityType, int entityId){

        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);

    }

    //查询某人是否点过赞
    public int findEntityLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0;
    }

    //查询某个用户的赞的数量
    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer likeCount = (Integer) redisTemplate.opsForValue().get(userLikeKey);

        return likeCount == null ? 0 : likeCount.intValue();
    }
}
