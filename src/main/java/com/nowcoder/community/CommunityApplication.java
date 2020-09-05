package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
//注解所标识的类，
public class CommunityApplication {

	//有此注解的方法在spring构造器执行后紧跟着执行
	@PostConstruct
	public void init(){
		//解决netty启动冲突
		System.setProperty("es.set.netty.runtime.available.processors","false");
	}
//自动创建了spring容器（不需要自己创建），启动了tomcat，自动扫描bean（）
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

}
