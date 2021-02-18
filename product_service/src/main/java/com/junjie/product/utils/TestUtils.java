package com.junjie.product.utils;

import feign.*;
import feign.form.FormEncoder;
import feign.form.FormProperty;
import feign.slf4j.Slf4jLogger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {


    public static void main(String[] args) throws IOException {
        System.out.println(System.currentTimeMillis());
//        http://10.212.82.35/subject
//        String url = "http://10.212.82.35/subject/file";
//        HashMap<String,String> multiValueMap = new HashMap<>();
//        multiValueMap.put("Content-Type", "application/json;charset=UTF-8");
//        multiValueMap.put("Authorization", "8e3b3adf-a530-46f3-aef7-2559e86177bb");
//        System.out.println(System.currentTimeMillis());
//        emp.setBirthday(System.currentTimeMillis());

////        emp.setPhoto(convertFileToBase64("/Users/yujunjie/Downloads/4351586784753_.jpg"));
//        Response post = OkHttpUtils.post(url, emp, multiValueMap);
//        ResponseBody body = post.body();
//        if (body != null) {
//            String json = body.string();
//            System.out.println("内容:" + json);
//        }
        JacksonDecoder jacksonDecoder = new JacksonDecoder();
        Emp emp = new Emp();
        emp.setName("何珊");
        emp.setSubject_type(1);
        emp.setPhoto(new File("/Users/yujunjie/Desktop/5ff2cfa7695c7a00016313cf.jpeg"));
        emp.setVisitor_type(1);
        emp.setGroup_ids(3);
        emp.setStart_time(System.currentTimeMillis() / 1000);
        emp.setEnd_time((System.currentTimeMillis() + 100000000) / 1000);
        Koala koala = Feign.builder()
//                .decoder(new GsonDecoder())
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(jacksonDecoder)
                .errorDecoder(new FeignErrorDecoder(jacksonDecoder))
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
//                .options(new Request.Options(1000, 3500))
//                .retryer(new Retryer.Default(5000, 5000, 3))
                .target(Koala.class, "http://10.212.82.35");

        BaseData s = koala.create(emp, "0ca61ddf-a25b-4e97-92bd-8bb5d862dea9");
        System.out.println("内容:" + s.getDesc());
    }


    public static interface Koala {
        /**
         * 创建员工并上传底图
         *
         * @param emp
         * @return
         */
        @RequestLine("POST /subject/file")
        @Headers({"Content-Type: multipart/form-data", "Charset: UTF-8", "Authorization: {token}"})
        BaseData create(Emp emp, @Param("token") String token);
    }


    /**
     * 本地文件（图片、excel等）转换成Base64字符串
     *
     * @param imgPath
     */
    public static String convertFileToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try (InputStream in = new FileInputStream(imgPath)) {
//            InputStream in = ;
            System.out.println("文件大小（字节）=" + in.available());
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组进行Base64编码，得到Base64编码的字符串
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = encoder.encode(data);
        return base64Str;
    }


    @Getter
    @Setter
    public static class Emp {
        @FormProperty("name")
        private String name;
        @FormProperty("subject_type")
        private int subject_type;
        @FormProperty("visitor_type")
        private int visitor_type;
        @FormProperty("photo")
        private File photo;
        @FormProperty("start_time")
        private long start_time;
        @FormProperty("end_time")
        private long end_time;
        @FormProperty("group_ids")
        private int group_ids;


    }

    @Getter
    @Setter
    @ToString
    public static class BaseData {
        /**
         * code : -1002
         * data : {}
         * desc : 参数错误
         * timecost : 10
         */

        private int code;
        private DataBean data;
        private String desc;
        private int timecost;

    }

    public static class DataBean {
    }
}
