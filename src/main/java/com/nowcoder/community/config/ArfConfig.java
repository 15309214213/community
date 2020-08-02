package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class ArfConfig {
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return  new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    }
}
