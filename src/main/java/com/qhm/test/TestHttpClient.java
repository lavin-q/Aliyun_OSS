package com.qhm.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TestHttpClient {
    public static void main(String[] args){

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000)
                .setSocketTimeout(50000)
                .setConnectionRequestTimeout(1000)
                .build();

        //配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();
        //设置连接池大小
        ConnectingIOReactor ioReactor=null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(100);


        final CloseableHttpAsyncClient client = HttpAsyncClients.custom().
                setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();


        //构造请求
        String url = "http://127.0.0.1:8080/sleep";
        List<HttpPost> list = new ArrayList<HttpPost>();
        for(int i=0;i<5;i++){
            HttpPost httpPost = new HttpPost(url);
            /*StringEntity entity = null;
            try {
                String a = "{ \"index\": { \"_index\": \"test\", \"_type\": \"test\"} }\n" +
                        "{\"name\": \"上海\",\"age\":33}\n";
                entity = new StringEntity(a);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpPost.setEntity(entity);*/
            list.add(httpPost);
        }
        //start
        client.start();

        for(int i=0;i<5;i++){
            Future<HttpResponse> execute = client.execute(list.get(i), new Back());
        }
        //异步请求
        while(true){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Back implements FutureCallback<HttpResponse> {

        private long start = System.currentTimeMillis();
        Back(){
        }

        public void completed(HttpResponse httpResponse) {
            try {
                System.out.println("completed cost is:"+(System.currentTimeMillis()-start)+":"+ getResult(httpResponse));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void failed(Exception e) {
            log.error("failed cost is:"+(System.currentTimeMillis()-start)+":"+e);
        }

        public void cancelled() {

        }
    }

    //获取接口返回内容
    public static String getResult(HttpResponse httpResponse) throws Exception{
        BufferedReader in = null;
        String result = "";
        InputStream content = httpResponse.getEntity().getContent();
        in = new BufferedReader(new InputStreamReader(content,"UTF-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }
}