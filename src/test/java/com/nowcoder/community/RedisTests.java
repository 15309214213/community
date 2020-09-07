package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
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

    //统计20万个重复数据
    @Test
    public void testHyperLogLog(){
        String redisKey = "test:hyperlog:01";
        for (int i = 1; i < 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey, i);
        }
        for (int i = 1; i < 100000; i++) {
            int r = (int) (Math.random()*100000 + 1);
            redisTemplate.opsForHyperLogLog().add(redisKey, r);
        }
        long s = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(s);
    }

    //将三组数据合并，在统计合并后的重复数据的独立总数
    @Test
    public void testHyperLogLogUnion(){
        String redisKey2 = "test:hll:02";
        for (int i = 1; i < 10000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2, i);
        }
        String redisKey3 = "test:hll:03";
        for (int i = 5001; i < 15000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3, i);
        }
        String redisKey4 = "test:hll:04";
        for (int i = 10001; i < 20000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey4, i);
        }
        String  unionKey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionKey, redisKey2,redisKey3, redisKey4);

        System.out.println(redisTemplate.opsForHyperLogLog().size(unionKey));
    }

    //统计一组数据的bool值
    @Test
    public void testBitMap(){
        String redisKey = "test:bm:01";

        //记录
        redisTemplate.opsForValue().setBit(redisKey, 1,true);
        redisTemplate.opsForValue().setBit(redisKey, 3,true);
        redisTemplate.opsForValue().setBit(redisKey, 5,true);

        //查询
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 2));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 3));

        //统计
        Object object = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(object);
    }

    //统计多组数据的bool值，并做运算

}
