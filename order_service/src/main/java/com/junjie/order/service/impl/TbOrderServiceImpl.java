package com.junjie.order.service.impl;

import com.central.db.service.BaseServiceImpl;
import com.junjie.order.dao.ITbOrderMapper;
import com.junjie.order.entity.TbOrder;
import com.junjie.order.service.ITbOrderService;
import org.springframework.stereotype.Service;

@Service
public class TbOrderServiceImpl extends BaseServiceImpl<ITbOrderMapper, TbOrder> implements ITbOrderService {
}
