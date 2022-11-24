package com.example.nocv.tengxunapi;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.CompositeRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.io.IOException;

import static org.apache.http.impl.client.HttpClients.createDefault;

@Component
public class HttpUtils {


    /*
    * 发送网络请求的工具
    *
    * */
    public String getData(){
        //1.请求参数
        RequestConfig requestConfig = RequestConfig.custom()
                                           .setSocketTimeout(1000)
                                           .setConnectTimeout(1000)
                                           .setConnectionRequestTimeout(10000).build();

        CloseableHttpClient httpClient = null;
        HttpGet request = null;
        CloseableHttpResponse response = null;
        try {

        //创建HttpClient
        httpClient = HttpClients.createDefault();

        //发送网络请求
        request = new HttpGet("https://c.m.163.com/ug/api/wuhan/app/data/list-total");

        //配置信息
        request.setConfig(requestConfig);

        //发送请求
        response = httpClient.execute(request);

        //查看状态码是否成功
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200){
                //解析数据
                HttpEntity entity = response.getEntity();
                String s = EntityUtils.toString(entity, "utf-8");
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (request!=null){
                request.releaseConnection();
            }

            if (httpClient!=null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }
}
