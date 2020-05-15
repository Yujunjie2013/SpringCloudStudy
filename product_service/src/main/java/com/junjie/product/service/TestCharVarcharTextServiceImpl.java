package com.junjie.product.service;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.junjie.common.bean.TestCharVarcharText;
import com.junjie.product.dao.TestCharVarcharTextMapper;
import com.junjie.product.service.TestCharVarcharTextService;
@Service
public class TestCharVarcharTextServiceImpl extends ServiceImpl<TestCharVarcharTextMapper, TestCharVarcharText> implements TestCharVarcharTextService{

}
