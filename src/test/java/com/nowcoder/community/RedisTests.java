package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSting(){
        String redisKey = "test:count";

        redisTemplate.opsForValue().set(redisKey, 1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHash(){

        String redisKey = "test:user";

        redisTemplate.opsForHash().put(redisKey,"id",111);
        redisTemplate.opsForHash().put(redisKey, "username","lisi");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));

    }

    @Test
    public void testLists(){
        String redisKey = "test:id";
        redisTemplate.opsForList().leftPush(redisKey,111);
        redisTemplate.opsForList().leftPush(redisKey,222);
        redisTemplate.opsForList().leftPush(redisKey,333);

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey,0));
        System.out.println(redisTemplate.opsForList().range(redisKey,0,2));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().rightPop(redisKey));
        System.out.println(redisTemplate.opsForList().rightPop(redisKey));


    }

    @Test
    public void testSets(){
        String redisKey = "test:teachers";
        redisTemplate.opsForSet().add(redisKey,"id1","id2","id3", "id4", "id5");
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void TestSorted(){
        String redisKey = "test:students";

        redisTemplate.opsForZSet().add(redisKey,"aa", 90);
        redisTemplate.opsForZSet().add(redisKey,"bb", 80);
        redisTemplate.opsForZSet().add(redisKey,"cc", 55);
        redisTemplate.opsForZSet().add(redisKey,"dd", 200);

        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "bb"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey,"bb"));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey,0,1));

    }

    //测试操作所用keys
    @Test
    public void testKeys(){
        redisTemplate.delete("test:user");

        System.out.println(redisTemplate.hasKey("test:user"));

        redisTemplate.expire("test:students",10, TimeUnit.SECONDS);
    }

    @Test
    //多次访问同一个key
    public void testBoundOperations(){
        String redisKey = "test:count";

        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);

        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    //编程式事务
    @Test
    public void testTransactional(){
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String redisKey = "tes:tttt";

                //启用事务
                operations.multi();

                operations.opsForSet().add(redisKey, "aa");
                operations.opsForSet().add(redisKey, "b");
                operations.opsForSet().add(redisKey, "c");


                System.out.println(operations.opsForSet().members(redisKey));

                //提交事务
                return operations.exec();
            }
        });
        System.out.println(obj);
    }

}
