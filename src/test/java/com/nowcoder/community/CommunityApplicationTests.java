package com.nowcoder.community;

import com.nowcoder.community.dao.arfDao;
import com.nowcoder.community.service.ArfService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//配置类
class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	@Test
	void contextLoads() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//ApplicationContext     是  spring 容器
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContest(){
		 System.out.println(applicationContext);
		 arfDao arfd = applicationContext.getBean(arfDao.class)  ;
		 System.out.println(arfd.select());

		 arfd = applicationContext.getBean("zidingyi", arfDao.class) ;
		 System.out.println(arfd.select());
	}
	@Test
	public void  testBeanManagement(){
		ArfService arfService  = applicationContext.getBean(ArfService.class);
		System.out.println(arfService);



		arfService  = applicationContext.getBean(ArfService.class);
	    System.out.println(arfService);
	}
	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat =
				applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}
	@Autowired
	@Qualifier("zidingyi")
	private arfDao arfdao;

	@Test
	public  void testDI(){
		System.out.println(arfdao);
	}

	@Autowired
	private ArfService arfService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI1(){
		System.out.println(arfdao);
		System.out.println(arfService);
		System.out.println(simpleDateFormat);
	}

















}
