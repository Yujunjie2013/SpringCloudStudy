package com.test;

import com.junjie.order.OrderApplication;
import com.junjie.order.dao.ITbOrderMapper;
import com.junjie.order.entity.TbOrder;
import com.junjie.order.service.GoodsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderApplication.class})
@Slf4j
public class GiftmallApplicationTests {
    @Resource
    private GoodsService goodsService;

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(100);
    private CyclicBarrier cyclicBarrier1 = new CyclicBarrier(100);

    @Test
    public void contextLoads() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrier.await();

                    goodsService.multi(1L);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
            ).start();
            new Thread(() -> {
                try {
                    cyclicBarrier1.await();

                    goodsService.multi(2L);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
            ).start();
        }

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ITbOrderMapper mapper = sqlSession.getMapper(ITbOrderMapper.class);
        TbOrder tbOrder = mapper.selectById(1);
//        TbOrder tbOrder = sqlSession.selectOne("com.junjie.order.dao.TbOrderMapper.selectOneById", 1);
//        TbOrder tbOrder = sqlSession.selectOne("com.junjie.order.dao.TbOrderMapper.findOnById", 1);
        System.out.println("哈哈：" + tbOrder);
    }

    @Autowired
    private Redisson redisson;

    @Test
    public void test() {
//        Redisson redisson = redisson();
        RMap<String, Boolean> limit = redisson.getMap("limit");
        if (limit != null) {
            Boolean exist = limit.get("rMap");
            RRateLimiter rateli;
            if (exist == null || !exist) {
                rateli = redisson.getRateLimiter("rateli");
                //最大流速 = 每1秒钟产生10个令牌
                rateli.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);
                limit.put("rMap", true);
                rateli.tryAcquire(11);
            } else {
                rateli = redisson.getRateLimiter("rateli");
                rateli.acquire();
            }
        }
        RMap<String, Boolean> limit2 = redisson.getMap("limit2");
        for (int i = 0; i < 2; i++) {
            Boolean exist = limit2.get("rSeamByMap");
            RSemaphore rSemaphore;
            if (exist == null || !exist) {
                System.out.println("---->");
                rSemaphore = redisson.getSemaphore("rSeam");
                rSemaphore.trySetPermits(100);
                limit2.put("rSeamByMap", true);
                rSemaphore.tryAcquire();
            } else {
                rSemaphore = redisson.getSemaphore("rSeam");
                rSemaphore.tryAcquire();
                System.out.println("已经存在");
            }
            System.out.println("----》keyong：" + rSemaphore.availablePermits());

        }


        System.out.println("结束");
    }

    @Test
    public void test2() {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            int j = i % 2;
            RRateLimiter rateLimiter = redisson.getRateLimiter("delay:13526880238" + j);
            boolean b = rateLimiter.trySetRate(RateType.OVERALL, 1, 60, RateIntervalUnit.SECONDS);
            System.out.println("配置是否成功:" + b);
            if (rateLimiter.tryAcquire()) {
                System.out.println("成功获取令牌" + i);
            } else {
                System.out.println("没有获取到令牌" + i);
            }
        }
        long l1 = System.currentTimeMillis();
        System.out.println("累计耗时:" + (l1 - l));
        //253/268/260
        //1214/1122/1456/1248/1048
    }

    @Test
    public void test3() {
        ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap<>();
        long l = System.currentTimeMillis();
        for (int i = 0; i < 2000; i++) {
            int j = i % 2;
            String key = "delay:13526880238" + j;
            RRateLimiter rateLimiter = redisson.getRateLimiter(key);
            Boolean aBoolean = hashMap.get(key);
            boolean first = false;
            if (aBoolean == null || !aBoolean) {
                boolean b = rateLimiter.trySetRate(RateType.OVERALL, 1, 60, RateIntervalUnit.SECONDS);
                System.out.println("配置是否成功:" + b);
                first = true;
            }
            if (rateLimiter.tryAcquire()) {
                System.out.println("成功获取令牌" + i);
                if (first) {
                    hashMap.put(key, true);
                }
            } else {
                System.out.println("没有获取到令牌" + i);
            }
        }
        long l1 = System.currentTimeMillis();
        System.out.println("累计耗时:" + (l1 - l) + "集合大小:" + hashMap.size());
        //291、270、317
        //1232/1081/1117/1184/1186
        //
    }

    @Test
    public void test4() {
        RSemaphore semaphone = redisson.getSemaphore("semaphone");
        boolean b = semaphone.trySetPermits(10);
        System.out.println("是否初始化成功:" + b + "可用:" + semaphone.availablePermits());
        for (int i = 0; i < 4; i++) {
            if (semaphone.tryAcquire()) {
                System.out.println("成功");
            } else {
                System.out.println("失败");
            }
        }
    }

    @Test
    public void test5() throws InterruptedException {
        RPermitExpirableSemaphore semaphoneExt = redisson.getPermitExpirableSemaphore("semaphoneExt");

        boolean b = semaphoneExt.trySetPermits(10);
        System.out.println("可过期是否成功:" + b);
        for (int i = 0; i < 60; i++) {
            String s = semaphoneExt.tryAcquire(1,3, TimeUnit.SECONDS);
//            String acquire = semaphoneExt.acquire(1, TimeUnit.SECONDS);
            if (StringUtils.isNotEmpty(s)) {
                System.out.println(i + "成功:" + s);
            } else {
                System.out.println("失败");
            }
        }
    }

}
