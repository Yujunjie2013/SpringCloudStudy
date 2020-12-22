package com.junjie.common.aspect;

import com.junjie.common.annotation.RedisLock;
import com.junjie.common.exception.LockException;
import com.junjie.common.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
//@Order(1)//该order必须设置，很关键
@Slf4j
public class RedissonLockAspect {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Resource
    private DistributedLock distributedLock;

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

        Object lock;
        if (redissonLock.leaseTime() == -1) {
            //无论锁多久都不会不自动释放
            lock = distributedLock.tryLock(key, redissonLock.waitTime(), redissonLock.timeUnit(), redissonLock.isFair());
        } else {
            lock = distributedLock.tryLock(key, redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit(), redissonLock.isFair());
        }
        try {
            if (lock != null) {
                return joinPoint.proceed();
            } else {
                throw new LockException("锁等待超时");
            }
        } finally {
            distributedLock.unlock(lock);
        }
    }

    /**
     * 解析spring EL表达式
     *
     * @param key    表达式
     * @param method 方法
     * @param args   方法参数
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
