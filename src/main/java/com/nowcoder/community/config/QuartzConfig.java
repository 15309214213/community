package com.nowcoder.community.config;

import com.nowcoder.community.quartz.Arfjob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfig {
    //@Bean
    public JobDetailFactoryBean arfJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(Arfjob.class);
        factoryBean.setName("arfJob");
        factoryBean.setGroup("arfJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    //@Bean
    public SimpleTriggerFactoryBean arfTrigger(JobDetail arfJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(arfJobDetail);
        factoryBean.setName("arfTrigger");
        factoryBean.setGroup("arfTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
}
