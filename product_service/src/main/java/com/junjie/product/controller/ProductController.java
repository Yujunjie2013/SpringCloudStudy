package com.junjie.product.controller;

import com.junjie.common.annotation.RequestLimit;
import com.junjie.common.bean.BaseResponseBody;
import com.junjie.product.entity.OperationLog;
import com.junjie.product.utils.ExcelUtils;
import com.junjie.product.entity.TbProduct;
import com.junjie.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private IProductService iProductService;
    private Map<String, String> map = new HashMap<>();
    @Value("${server.port}")
    private String port;
    @Value("${spring.cloud.client.ip-address}")
    private String address;

    @GetMapping("/{id}")
    public BaseResponseBody findById(@PathVariable Long id) {
        TbProduct tbProduct = iProductService.findById(id);
        log.info("查询数据:{}",tbProduct.toString());
        return BaseResponseBody.success(tbProduct);
    }

    @GetMapping("/list")
    public BaseResponseBody<List<TbProduct>> getAll() {
        return BaseResponseBody.success(iProductService.list());
    }

    @PostMapping("/add")
    public String save(@RequestBody TbProduct tbProduct) {
        iProductService.save(tbProduct);
        return "保存成功";
    }

    @GetMapping("/resp")
    public ResponseEntity<String> test() {
        return ResponseEntity.badRequest().body("失败了");
    }


    @GetMapping("/test/{id}")
    public String test1(@PathVariable long id) {
        log.info("开始了：" + id);
        String name = Thread.currentThread().getName();
        if (!map.containsKey(name)) {
            map.put(name, name);
        } else {
            System.out.println("一共有线程:" + map.size());
        }
        try {
            Thread.sleep(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "休眠结束";
    }

    @GetMapping("/export")
    @RequestLimit(limitValue = 1, timeOut = 1, timeUnit = TimeUnit.SECONDS)//并发1，获取锁等待1秒
    public void exportFile(HttpServletResponse response) {
        long start = System.currentTimeMillis();
        List<OperationLog> personList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OperationLog personVo = new OperationLog();
            personVo.setOperator("张三" + i);
            personVo.setPhone("1352688023" + i);
            personVo.setPlatform(i / 2 == 0 ? "小程序" : "web");
            personVo.setOperationContent("测试操作");
            personList.add(personVo);
        }
        log.debug("导出excel所花时间：" + (System.currentTimeMillis() - start));
        try {
            ExcelUtils.exportExcel(personList, "操作记录表", "操作日志", OperationLog.class, "操作记录", response);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("导出报错:", e);
        }
    }
}
