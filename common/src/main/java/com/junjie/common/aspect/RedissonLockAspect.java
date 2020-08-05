package com.junjie.common.aspect;

import com.junjie.common.annotation.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)//该order必须设置，很关键
public class RedissonLockAspect {
    private ExpressionParser parser = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private Redisson redisson;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redissonLock) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        String key = redissonLock.key();
        if (StringUtils.isEmpty(key)) {
            key = declaringClass.getName() + "#" + method.getName();
        } else {
            //方法内的所有参数
            Object[] args = joinPoint.getArgs();
            String parsedValue = parse(key, method, args);
            log.info("参数值" + parsedValue);
            key = method.getName() + parsedValue;
        }
        log.info("redis锁:" + key);

        RLock rLock = redisson.getLock(key);
        boolean res;
        if (redissonLock.leaseTime() == -1) {
            //无论锁多久都不会不自动释放
            res = rLock.tryLock(redissonLock.waitTime(), redissonLock.timeUnit());
        } else {
            res = rLock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit());
        }
        try {
            if (res) {
                log.info("取到锁成功:" + rLock.getName());
                return joinPoint.proceed();
            } else {
                log.info("----------nono----------");
                throw new RuntimeException("没有获得锁");
            }
        } finally {
            rLock.unlock();
            log.info("释放锁");
        }
    }

    /**
     * @param key    表达式
     * @param method 方法
     * @param args   方法参数
     * @return
     * @description 解析spring EL表达式
     * @author luocan
     * @date 2018年11月27日 上午10:41:01
     * @version 1.0.0
     */
    private String parse(String key, Method method, Object[] args) {
        String[] params = discoverer.getParameterNames(method);
        if (params == null || params.length == 0) {
            return "";
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}
