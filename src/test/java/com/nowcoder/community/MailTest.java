package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//配置类
public class MailTest {
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;


    @Test
    public  void testTextMail(){
        mailClient.sendMail("1029516758@qq.com", "test", "1111111113");
    }

    @Test
    public void testHtmlMail(){
        Context context=new Context();
        context.setVariable("username","sunday");
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);
        mailClient.sendMail("1029516758@qq.com","HTMLLLL",content);

    }
}
