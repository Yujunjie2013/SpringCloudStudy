package org.junjie.security.app;

import org.apache.commons.lang.StringUtils;
import org.junjie.security.core.social.JunjieSpringSocialConfiger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //在bean初始化的后置处理器中判断如果是自定义的这个处理器
        if (StringUtils.equals(beanName, "springSocialConfigurer")) {
            JunjieSpringSocialConfiger junjieSpringSocialConfiger = (JunjieSpringSocialConfiger) bean;
            //app模式下转发到这里让用户进行注册
            junjieSpringSocialConfiger.signupUrl("/social/signUp");
            return junjieSpringSocialConfiger;
        }
        return bean;
    }
}
