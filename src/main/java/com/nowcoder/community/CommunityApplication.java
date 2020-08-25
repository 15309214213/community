package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
//注解所标识的类，
public class CommunityApplication {
//自动创建了spring容器（不需要自己创建），启动了tomcat，自动扫描bean（）
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
