package com.junjie.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junjie.common.bean.Result;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;


public class ResponseUtil {
    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通过流写到前端
     */
    public static void responseWriter(ObjectMapper objectMapper, HttpServletResponse response, String msg, int httpStatus) throws IOException {
        Result result = Result.from(httpStatus + "", msg);
        responseWrite(objectMapper, response, result);
    }

    /**
     * 通过流写到前端
     */
    public static void responseSucceed(ObjectMapper objectMapper, HttpServletResponse response, Object obj) throws IOException {
        Result result = Result.succeed(obj);
        responseWrite(objectMapper, response, result);
    }

    /**
     * 通过流写到前端
     */
    public static void responseFailed(ObjectMapper objectMapper, HttpServletResponse response, String msg) throws IOException {
        Result<String> result = Result.failed(msg);
        responseWrite(objectMapper, response, result);
    }

    private static void responseWrite(ObjectMapper objectMapper, HttpServletResponse response, Result result) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (Writer writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(result));
            writer.flush();
        }
    }
}
