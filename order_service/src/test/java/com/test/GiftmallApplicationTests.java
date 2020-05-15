package com.test;

import com.junjie.common.bean.TbOrder;
import com.junjie.order.dao.TbOrderMapper;
import com.junjie.order.service.GoodsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//@RunWith(SpringRunner.class)
//@SpringBootTest
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
        TbOrderMapper mapper = sqlSession.getMapper(TbOrderMapper.class);
        TbOrder tbOrder = mapper.findOnById(1);
//        TbOrder tbOrder = sqlSession.selectOne("com.junjie.order.dao.TbOrderMapper.selectOneById", 1);
//        TbOrder tbOrder = sqlSession.selectOne("com.junjie.order.dao.TbOrderMapper.findOnById", 1);
        System.out.println("哈哈："+tbOrder);
    }
}
