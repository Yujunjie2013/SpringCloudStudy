package com.junjie.product;

import com.junjie.common.bean.TestCharVarcharText;
import com.junjie.product.dao.TestCharVarcharTextMapper;
import com.junjie.product.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@SpringBootTest
public class GiftmallApplicationTests {
    @Resource
    private ProductService goodsService;

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(100);
    private CyclicBarrier cyclicBarrier1 = new CyclicBarrier(100);
//    @Resource
//    private TestCharVarcharTextMapper testCharVarcharTextMapper;


    @Test
    public void contextLoads() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrier.await();

                    goodsService.findById(1L);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
            ).start();
            new Thread(() -> {
                try {
                    cyclicBarrier1.await();
                    goodsService.findById(2L);
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedis() {
        redisTemplate.opsForSet().add("hello","哈哈哈哈");
    }

    @Test
    public void insertTEST(){
        TestCharVarcharText testCharVarcharText = new TestCharVarcharText();
        testCharVarcharText.setMychar("mychar1");
        testCharVarcharText.setMyvarchar("myvarchar1");
        testCharVarcharText.setMytext("myText1");
//        testCharVarcharTextMapper.insert(testCharVarcharText);
    }

}
