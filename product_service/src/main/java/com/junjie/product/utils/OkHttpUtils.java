package com.junjie.product.utils;

import okhttp3.*;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Map;

public class OkHttpUtils {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new LogInterceptor())
            .build();
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //过滤null字段
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static Response get(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        Request request = joinHeaders(headers, builder);
        return okHttpClient.newCall(request).execute();
    }

    private static Request joinHeaders(Map<String, String> headers, Request.Builder builder) {
        if (headers != null && headers.size() > 0) {
            headers.forEach(builder::header);
        }
        return builder.build();
    }

    public static Response post(String url, Object body, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        String json;
        if (body instanceof String) {
            json = (String) body;
        } else {
            json = objectMapper.writeValueAsString(body);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);
        builder.post(requestBody);
        if (headers != null) {
            headers.put("Content-Type", "application/json;charset=UTF-8");
        }
        Request request = joinHeaders(headers, builder);
        return okHttpClient.newCall(request).execute();
    }

    /**
     * post http for x-www-form-urlencoded
     *
     * @param url
     * @param formMap
     * @return
     * @throws IOException
     */
    public static Response post(String url, Map<String, Object> formMap) throws IOException {
        if (formMap == null || formMap.size() == 0) {
            System.out.println("请求入参为空");
            return null;
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : formMap.keySet()) {
            builder.add(key, String.valueOf(formMap.get(key)));
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }

    public static File downloadFile(String url, String destFileDir) throws IOException {
        String[] split = url.split("/");
        String fileName = split[split.length - 1];
        //储存下载文件的目录
        File dir = new File(destFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName);
        if (file.exists()) {
            return file;
        }
        Response response = get(url, null);
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } finally {
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return file;
    }

    public static void main(String[] args) {
        try {
            Response response = OkHttpUtils.post("http://datacenter.techsel.cn:8888/GetValue",
                    "[{\"DeviceId\": \"12070B5AFB2F\",\"Points\": [1,2,3,4,5,6,7,8,9,10,11,20,21,22,23,24]}]", null);
            response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
