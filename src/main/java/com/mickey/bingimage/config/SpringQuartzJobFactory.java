package com.mickey.bingimage.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * 主要思路是通过新建一个AutowiringSpringBeanJobFactory 实现 quartz 的SpringBeanJobFactory（job生产工厂类）
 * 通过实现ApplicationContextAware接口 set ApplicationContextAware(上下文)到该新建job工厂类。
 * 然后通过super.createJobInstance(bundle) 创建 job，　beanFactory来autowired job实例
 *
 * @author wangmeng
 * @date 2020-06-11
 */
@Component
public class SpringQuartzJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object obj = super.createJobInstance(bundle);
        beanFactory.autowireBean(obj);
        return obj;
    }
}
