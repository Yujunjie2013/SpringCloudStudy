package com.junjie.common.exception;

import com.junjie.common.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 全局异常处理
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RequestLimitException.class)
    @ResponseBody
    public Result invalidErrorHandler(RequestLimitException e, HttpServletRequest httpServletRequest) {
        log.error("限流异常", e);
        return Result.failed(e.getMessage());
    }

    /**
     * AccessDeniedException异常处理返回json
     * 返回状态码:403
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public Result badMethodExpressException(AccessDeniedException e) {
        log.error("badMethodExpressException", e);
        return defHandler("没有权限请求当前方法", e);
    }

    /**
     * 返回状态码:405
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return defHandler("不支持当前请求方法", e);
    }

    /**
     * 返回状态码:415
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public Result handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return defHandler("不支持当前媒体类型", e);
    }

    /**
     * SQLException sql异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    @ResponseBody
    public Result handleSQLException(SQLException e) {
        return defHandler("服务运行SQLException异常", e);
    }

    /**
     * BusinessException 业务异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result handleException(BusinessException e) {
        return defHandler("业务异常", e);
    }

    private Result defHandler(String msg, Exception e) {
        log.error(msg, e);
        return Result.failed(msg);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result jsonErrorHandler(Exception e, HttpServletRequest httpServletRequest) {
        log.error("发生异常", e);
        return Result.failed("服务打了个盹，请稍后再试");
    }
}
