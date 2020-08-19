package com.junjie.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String jsonErrorHandler(Exception e, HttpServletRequest httpServletRequest) {
        log.error("发生异常", e);
        return "服务打了个盹，请稍后再试";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public String jsonErrorHandler(AccessDeniedException e, HttpServletRequest httpServletRequest) {
        log.error("权限拒绝:", e);
        return e.getMessage();
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RequestLimitException.class)
    @ResponseBody
    public String invalidErrorHandler(RequestLimitException e, HttpServletRequest httpServletRequest) {
        log.error("限流异常", e);
        return e.getMessage();
    }

}
