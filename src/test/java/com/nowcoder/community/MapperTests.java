package com.nowcoder.community;


import com.nowcoder.community.controller.LoginTicket;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.entity.Message;
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
        user.setId(168);
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

    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testInsertTicker(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(100);
        loginTicket.setTicket("aqweasd");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testAndSelectLoginTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("qweasd");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("qweasd",1);
        loginTicket = loginTicketMapper.selectByTicket("qweasd");
        System.out.println(loginTicket);
    }

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testMessageMapper(){

        List<Message> list = messageMapper.selectConversations(111,0,100);
        for(Message message : list) {
            System.out.println(message);
        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message: list){
                System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);


        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);

    }
}
