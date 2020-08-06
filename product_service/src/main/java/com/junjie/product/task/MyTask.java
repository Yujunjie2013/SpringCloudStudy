package com.junjie.product.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@EnableAsync
@Slf4j
@Component
public class MyTask {
//    @Scheduled(fixedRate = 5000)
    public void count() {
        log.info("执行了一次");
    }


    @Async
    @Scheduled(cron = "0/60 * * * * ? ")
    public void bathReleaseMeetingInfo() {
        log.info("bathReleaseMeetingInfo >> 基础释放 job开始");
    }
}
