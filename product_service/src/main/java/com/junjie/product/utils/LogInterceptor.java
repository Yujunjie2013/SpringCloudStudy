package com.junjie.product.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;


@Slf4j
public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Buffer buffer = new Buffer();
        String reqContentType = request.header("Content-Type");
        if (reqContentType != null && reqContentType.contains("application/json")) {
            RequestBody body = request.body();
            if (body != null)
                body.writeTo(buffer);
            log.info(String.format("Sending request %s on %s %n%s Request Params: %s",
                    request.url(), chain.connection(), request.headers(), buffer.clone().readUtf8()));
            buffer.close();
        } else {
            log.info(String.format("Sending request %s on %s %n%s", request.url(), chain.connection(), request.headers()));
        }

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        String contentType = response.header("Content-Type");
        if (contentType != null && contentType.contains("application/json")) {
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE);
            buffer = source.buffer().clone();
            log.info(String.format("Received response for %s in %.1fms \ncode:%s %n%s Response Json: %s",
                    response.request().url(), (t2 - t1) / 1e6d, response.code(), response.headers(),
                    buffer.readUtf8()));
        } else {
            log.info(String.format("Received response for %s in %.1fms \ncode:%s %n%s ",
                    response.request().url(), (t2 - t1) / 1e6d, response.code(), response.headers()));
        }
        return response;
    }
}
