package com.junjie.common.util;

import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.locks.ReentrantLock;

public class MyBloomFilter {
    //你的布隆过滤器容量
    private static final int DEFAULT_SIZE = 2 << 28;
    //bit数组，用来存放结果
    private static BitSet bitSet = new BitSet(DEFAULT_SIZE);
    //后面hash函数会用到，用来生成不同的hash值，可随意设置，别问我为什么这么多8，图个吉利
    private static final int[] hashInts = {1, 6, 16, 38, 58, 68};

    //add方法，计算出key的hash值，并将对应下标置为true
    public void add(Object key) {
//        bitSet.set(hash(key,0));
        Arrays.stream(hashInts).forEach(i -> bitSet.set(hash(key, i)));
    }

    //判断key是否存在，true不一定说明key存在，但是false一定说明不存在
    public boolean isContain(Object key) {
        boolean result = true;
        for (int i : hashInts) {
            //短路与，只要有一个bit位为false，则返回false
            result = result && bitSet.get(hash(key, i));
        }
        return result;
//        return bitSet.get(hash(key,0));
    }

    //hash函数，借鉴了hashmap的扰动算法，强烈建议大家把这个hash算法看懂，这个设计真的牛皮加闪电
    private int hash(Object key, int i) {
        int h;
        return key == null ? 0 : (i * (DEFAULT_SIZE - 1) & ((h = key.hashCode()) ^ (h >>> 16)));
    }
    private  static ReentrantLock lock=new ReentrantLock();
    public static void main(String[] args) {

        new Thread(){
            @Override
            public void run() {
                System.out.println("线程1:"+Thread.currentThread().getName());
                lock.lock();
                System.out.println("获取锁成功了");
                try {
                    for (int i = 0; i < 100; i++) {
                        try {
                            Thread.sleep(50000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }finally {
                    lock.unlock();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                System.out.println("线程2:"+Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("aaaa");
                lock.lock();
                System.out.println("线程2获取锁成功");
                try {
                    for (int i = 0; i < 100; i++) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }finally {
                    lock.unlock();
                }
            }
        }.start();

        MyBloomFilter myNewBloomFilter = new MyBloomFilter();
        myNewBloomFilter.add("张学友");
        myNewBloomFilter.add("郭德纲");
        myNewBloomFilter.add("蔡徐鸡6");
        myNewBloomFilter.add("蔡徐鸡5");
        myNewBloomFilter.add("蔡徐鸡8");
        myNewBloomFilter.add("蔡徐鸡1");
        myNewBloomFilter.add("蔡徐鸡2");
        myNewBloomFilter.add("蔡徐鸡3");
        myNewBloomFilter.add("蔡徐鸡4");
        myNewBloomFilter.add(666);
        myNewBloomFilter.add(6616);
        myNewBloomFilter.add(6662);
        myNewBloomFilter.add(6665);
        myNewBloomFilter.add(6666);
        System.out.println(myNewBloomFilter.isContain("张学友"));//true
        System.out.println(myNewBloomFilter.isContain("张学友 "));//false
        System.out.println(myNewBloomFilter.isContain("张学友1"));//false
        System.out.println(myNewBloomFilter.isContain("郭德纲"));//true
        System.out.println(myNewBloomFilter.isContain("蔡徐老母鸡"));//false
        System.out.println(myNewBloomFilter.isContain("蔡徐鸡4"));//false
        System.out.println(myNewBloomFilter.isContain(666));//true
        System.out.println(myNewBloomFilter.isContain(888));//false
        System.out.println(bitSet.size());
        System.out.println(bitSet.cardinality());
    }
}
