package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//配置类
public class CafeineTests {

    @Autowired
    private DiscussPostService postService;

    @Test
    public void initDataForTest() {
        for (int i = 0; i < 300000; i++) {
            DiscussPost post = new DiscussPost();
            post.setUserId(111);
            post.setTitle("西电缓存计划");
            post.setContent("缓存test，用来测试设置一级缓存后的性能提升，真的跑了半天，早知道就不再平板上跑程序了");
            post.setCreateTime(new Date());
            post.setScore(Math.random() * 2000);
            postService.addDiscussPost(post);
        }
    }


    @Test
    public void testCache() {
        System.out.println(postService.findDiscussPost(0, 0, 10, 1));
        System.out.println(postService.findDiscussPost(0, 0, 10, 1));
        System.out.println(postService.findDiscussPost(0, 0, 10, 1));
        System.out.println(postService.findDiscussPost(0, 0, 10, 0));
    }
}
