package com.nowcoder.community;


import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//配置类
public class MapperTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    public void testSelectUser(){
        User user= userMapper.selectById(101);
        System.out.println(user);
        user = userMapper.selectByName("liubei");
        System.out.println(user);;
        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }
    @Test
    public void testInsetUser(){
        User user = new User();
        user.setUsername("12333");
        user.setPassword("123456");
        user.setSalt("aba");
        user.setEmail("12312@oo.com");
        user.setHeaderUrl("www.baidu.com/101/png");
        user.setCreateTime(new Date());;
        int i=userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());
    }
    @Test
    public void updateUser(){
        int i = userMapper.updateStatus(150,1);
        System.out.println(i);
        i = userMapper.updateHeader(150,"www.baidu.com/101/png");
        i= userMapper.updatePassword(150,"21389");
        System.out.println(i);
    }
    @Test
    public void testDeleteUser(){
        User user=new User();
        user.setId(150);
        userMapper.deleteUser(user);
        //System.out.println(i);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for(DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
}
