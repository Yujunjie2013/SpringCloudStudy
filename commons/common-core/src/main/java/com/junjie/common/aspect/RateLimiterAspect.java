package com.junjie.common.aspect;

import com.junjie.common.annotation.RateLimit;
import com.junjie.common.exception.RequestLimitException;
import com.junjie.common.limiter.Limiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
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
@Slf4j
public class RateLimiterAspect {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Resource
    private Limiter limiter;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        if (limiter == null) {
            log.info("Limiter is null");
            return pjp.proceed();
        }
        Signature signature = pjp.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            if (rateLimit != null && rateLimit.rate() >= 1) {
                Class<?> declaringClass = method.getDeclaringClass();
                String key = rateLimit.key();
                if (StringUtils.isEmpty(key)) {
                    key = declaringClass.getName() + "#" + method.getName();
                } else {
                    //方法内的所有参数
                    Object[] args = pjp.getArgs();
                    String parsedValue = parse(key, method, args);
                    log.info("参数值" + parsedValue);
                    key = method.getName() + parsedValue;
                }

                try {
                    if (!limiter.tryAcquire(key, rateLimit.rate(), rateLimit.timeOut())) {
                        throw new RequestLimitException("请勿连续尝试");
                    }
                    return pjp.proceed();
                } finally {
                    limiter.release();
                }
            }
        }

        return pjp.proceed();
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
