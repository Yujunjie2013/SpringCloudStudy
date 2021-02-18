package com.junjie.product.utils;

import feign.Logger;
import org.slf4j.LoggerFactory;

public class FeignLogger extends Logger {
    org.slf4j.Logger logger = LoggerFactory.getLogger(FeignLogger.class);

    @Override
    protected void log(String configKey, String format, Object... args) {
        logger.info(methodTag(configKey) + format + "%n", args);
    }
}
