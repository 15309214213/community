package com.nowcoder.community;

import com.nowcoder.community.service.ArfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//配置类
public class arfTests {
    @Autowired
    private ArfService arfService;
    @Test
    public void testSave1(){
        Object object = arfService.save1();
        System.out.println(object);
    }

    @Test
    public void testSave2(){
        Object object = arfService.save2();
        System.out.println(object);
    }
}
