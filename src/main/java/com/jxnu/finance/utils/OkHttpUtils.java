package com.jxnu.finance.utils;

import com.google.common.util.concurrent.RateLimiter;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class OkHttpUtils {
    private final static Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .build();

    /**
     * 解析指定的url,指定的编码 为jsoup的document对象
     *
     * @param url
     * @param encode
     * @return
     */
    public static Document parseToDocument(String url, String encode) {
        try {
            Request request = constructeRequst(url);
            Response response = call(request);
            int status = response.code();
            logger.info("http response status:{}", status);
            if (status != 200) return null;
            String body = new String(response.body().bytes(), encode);
            Document document = Jsoup.parse(body, url);
            return document;
        } catch (Exception e) {
            logger.error("error:{}", ExceptionUtils.getMessage(e));
            return null;
        }
    }

    /**
     * post 表单
     *
     * @param params 表单内容
     * @param heads  请求头
     * @param url    请求路径
     * @return
     * @throws IOException
     */
    public static String post(Map<String, String> params, Map<String, String> heads, String url) {
        if (CollectionUtils.isEmpty(params) || StringUtils.isBlank(url)) return null;
        //设置表单内容
        Response response = null;
        try {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> keys = params.keySet();
            for (String key : keys) {
                String value = params.get(key);
                builder.add(key, value);
            }
            //设置请求参数
            Request.Builder requestBuild = new Request.Builder().post(builder.build()).url(url);
            //设置请求头部
            if (!CollectionUtils.isEmpty(heads)) {
                Set<String> headKeys = heads.keySet();
                for (String key : headKeys) {
                    String value = heads.get(key);
                    requestBuild.addHeader(key, value);
                }
            }
            //获取响应
            response = call(requestBuild.build());
            return response.body().string();
        } catch (IOException e) {
            logger.error("error:{}", ExceptionUtils.getRootCause(e));
        }
        return "";
    }

    //解析指定url,返回的数据
    public static String parseToString(String url) {
        try {
            Request request = constructeRequst(url);
            Response response = call(request);
            int status = response.code();
            logger.info("http response status:{}", status);
            if (status != 200) return null;
            return response.body().string();
        } catch (Exception e) {
            logger.error("error:{}", ExceptionUtils.getMessage(e));
            return null;
        }
    }

    /**
     * 构造http请求
     *
     * @param url
     * @return
     */
    private static Request constructeRequst(String url) {
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "*/*")
                .header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0")
                .build();
        return request;
    }

    /**
     * http请求频率控制
     *
     * @param builder
     * @return
     * @throws IOException
     */
    private static Response call(Object builder) throws IOException {
        RateLimiter rateLimiter = RateLimiter.create(1000.0);
        if (rateLimiter.tryAcquire()) {
            rateLimiter.acquire();
            if (builder instanceof Request) {
                return client.newCall((Request) builder).execute();
            }
            return client.newCall(((Request.Builder) builder).build()).execute();
        }
        return null;
    }

    /**
     * 下载对应文件
     *
     * @param url
     * @param filePath
     */
    public static void fileDownLoad(String url, final String filePath) {
        RateLimiter rateLimiter = RateLimiter.create(1);
        if (rateLimiter.tryAcquire()) {
            rateLimiter.acquire();
            Call call = client.newCall(constructeRequst(url));
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    logger.error("url:{},Exception", ExceptionUtils.getRootCauseMessage(e));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    /**
                     * 创建目录
                     */
                    String fileCategory = filePath.substring(0, filePath.lastIndexOf(File.separator));
                    File category = new File(fileCategory);
                    if (!category.exists()) {
                        category.mkdirs();
                    }
                    /**
                     * 下载文件
                     */
                    File file = new File(filePath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    if (response != null) {
                        InputStream is = response.body().byteStream();
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = 0;
                        byte[] buffer = new byte[2048];
                        while (-1 != (len = is.read(buffer))) {
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        fos.close();
                        is.close();
                    }
                }
            });
        }
    }


    public static void main(String[] args) {
        OkHttpUtils.fileDownLoad("http://static.cninfo.com.cn/finalpage/2019-04-25/1206094800.PDF", "F:\\工作\\2019年报.pdf");
    }
}
