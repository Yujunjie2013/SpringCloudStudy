//package com.junjie.common.aspect;
//
//import com.junjie.common.annotation.RequestLimit;
//import com.junjie.common.exception.RequestLimitException;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Semaphore;
//
////@Component
//@Aspect
//@Slf4j
//public class SemaphoreAspect {
//
//    private ConcurrentHashMap<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();
//
//
//    @Around("@annotation(requestLimit)")
//    public Object around(ProceedingJoinPoint pjp, RequestLimit requestLimit) throws Throwable {
//        Signature signature = pjp.getSignature();
//        if (signature instanceof MethodSignature) {
//            MethodSignature methodSignature = (MethodSignature) signature;
//            Method method = methodSignature.getMethod();
//            if (requestLimit != null && requestLimit.limitValue() > 0) {
//                String name = method.getName();
//                int limitValue = requestLimit.limitValue();
//                Semaphore semaphore;
//                if ((semaphore = semaphoreMap.get(name)) == null) {
//                    semaphoreMap.put(name, semaphore = new Semaphore(limitValue));
//                }
//                boolean acquire = semaphore.tryAcquire(requestLimit.timeOut(), requestLimit.timeUnit());
//                if (!acquire) {
//                    throw new RequestLimitException("已超过该接口最大并发数");
//                }
//                try {
//                    return pjp.proceed();
//                } finally {
//                    //用完归还信号量
//                    semaphore.release();
//                }
//            }
//        }
//        return pjp.proceed();
//    }
//}
