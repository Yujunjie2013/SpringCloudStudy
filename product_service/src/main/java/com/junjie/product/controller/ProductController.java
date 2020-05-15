package com.junjie.product.controller;

import com.junjie.common.bean.OperationLog;
import com.junjie.common.util.ExcelUtils;
import com.junjie.product.entity.Product;
import com.junjie.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;
    private Map<String, String> map = new HashMap<>();
    @Value("${server.port}")
    private String port;
    @Value("${spring.cloud.client.ip-address}")
    private String address;

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return product;
    }

    @PostMapping()
    public String save(@RequestBody Product product) {
        productService.save(product);
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
    public void exportFile(HttpServletResponse response) {
        long start = System.currentTimeMillis();
        List<OperationLog> personList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OperationLog personVo = new OperationLog();
            personVo.setOperator("张三" + i);
            personVo.setPhone("1352688023" + i);
            personVo.setPlatform(i / 2 == 0 ? "小程序" : "web");
            personVo.setOperationContent("测试操作");
            personVo.setCreateTime(new Date());
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