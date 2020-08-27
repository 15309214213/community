package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.dao.arfDao;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
//@Scope("prototype")//生成多个实例，不写就只生成一个
public class ArfService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;


    @Autowired
    private arfDao arfdao;
    public  ArfService() {
        System.out.println("实例化");
    }
    @PostConstruct
    public void init(){
        System.out.println("初始化");

    }
    @PreDestroy
    public void destory(){
        System.out.println("销毁");
    }
    public  String find(){
        return arfdao.select();
    }


    //编写需要回滚的业务，采用声明式事务
    //REQUIRED:支持当前事务（外部事务），如果不存在，则创建新事物
    //REQUIRES_NEW:创建一个新事务，并且暂停当前事务（外部事物）
    //NESTED:如果当前存在事务，则嵌套在该是我中执行（独立的提交和回滚），若不存在则创建新事物
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String save1(){
        //新增用户
        User user = new User();
        user.setUsername("arf");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("arf@qq.com");
        user.setHeaderUrl("http://inin.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);


        //新增帖子
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setUserId(user.getId());
        discussPost.setTitle("hello");
        discussPost.setContent("666");
        discussPost.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(discussPost);

        //Integer.valueOf("qqq");
        return "okok";

    }
    
    //编程式事务
    @Autowired
    private TransactionTemplate transactionTemplate;
    
    public Object save2(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            //底层有tenmlate自动调用
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {

                //新增用户
                User user = new User();
                user.setUsername("tttsss");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("ttss@qq.com");
                user.setHeaderUrl("http://ininnini.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);


                //新增帖子
                DiscussPost discussPost = new DiscussPost();
                discussPost.setUserId(user.getId());
                discussPost.setUserId(user.getId());
                discussPost.setTitle("nihao");
                discussPost.setContent("999");
                discussPost.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(discussPost);


                //手动写入一个错误，看其是否回滚
                //Integer.valueOf("qqq");
                return "okok";

            }
        });
        
    }






}
