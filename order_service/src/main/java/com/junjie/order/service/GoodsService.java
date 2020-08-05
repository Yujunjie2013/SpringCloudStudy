package com.junjie.order.service;

import com.junjie.common.annotation.RedisLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsService {

    @Transactional(rollbackFor = Exception.class)
    @RedisLock
    public void multi(long goodId) {

    }
}
