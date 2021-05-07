package com.junjie.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.central.redis.template.RedisRepository;
import com.google.gson.Gson;
import com.junjie.common.annotation.RateLimit;
import com.junjie.common.bean.Result;
import com.junjie.product.entity.OperationLog;
import com.junjie.product.entity.TbProduct;
import com.junjie.product.service.IProductService;
import com.junjie.product.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private IProductService iProductService;
    private Map<String, String> map = new HashMap<>();
    @Autowired
    private RedisRepository redisRepository;
    @Value("${server.port}")
    private String port;

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        log.info("findById--->" + port);
        TbProduct tbProduct = iProductService.findById(id);
        return Result.succeed(tbProduct);
    }

    @GetMapping("/list")
    public Result<List<TbProduct>> getAll() {
        return Result.succeed(iProductService.list());
    }

    @PostMapping("/add")
    public String save(@RequestBody TbProduct tbProduct) {
        iProductService.add(tbProduct);
        return "保存成功";
    }

    @PutMapping("/update")
    public Result update(@RequestBody TbProduct tbProduct) {
        tbProduct.setVersion(1);
        TbProduct b = iProductService.update(tbProduct);
        return Result.succeed(b);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        iProductService.delete(id);
        return "删除成功";
    }

    @GetMapping("/{page}/{pageSize}")
    public Result<IPage<TbProduct>> getList(@PathVariable Long page, @PathVariable Long pageSize) {
        IPage<TbProduct> list = iProductService.getList(page, pageSize);
        return Result.succeed(list);
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
//    @RequestLimit(limitValue = 1, timeOut = 1, timeUnit = TimeUnit.SECONDS)//并发1，获取锁等待1秒
    @RateLimit(rate = 1, timeOut = 1)//一分钟只允许调用1次
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

    public static void main(String[] args) {
        TbProduct tbProduct = new TbProduct();
        tbProduct.setProductDesc("测试商品啊");
        tbProduct.setCaption("苹果电脑");
        tbProduct.setPrice(new BigDecimal(100));
        tbProduct.setType(2);
        tbProduct.setProductName("苹果手机");
        tbProduct.setStatus(1L);
        tbProduct.setInventory(100);
        Gson gson = new Gson();
        String s = gson.toJson(tbProduct);
        System.out.println(s);
    }
}
