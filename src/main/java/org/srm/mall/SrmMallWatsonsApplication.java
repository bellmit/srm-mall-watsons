package org.srm.mall;

import feign.Client;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.srm.autoconfigure.catalogue.EnableSrmMall;

/**
 * SRM目录化商城
 *
 * @author fu.ji@hand-china.com 2020-12-21 14:15:37
 */
@EnableSrmMall
@EnableDiscoveryClient
@SpringBootApplication
public class SrmMallWatsonsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SrmMallWatsonsApplication.class,args);

        Client client = new Client.Default(null, null);

        //设置代理IP、端口、协议（请分别替换）
        HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");

        //把代理设置到请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        CloseableHttpClient client1 =  HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
    }
}
