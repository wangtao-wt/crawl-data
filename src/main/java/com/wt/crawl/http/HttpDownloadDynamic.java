package com.wt.crawl.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author wangtao
 */
public class HttpDownloadDynamic {

    //设置代理，模范浏览器
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";

    public static String sendGet(String url) throws IOException, ScriptException, NoSuchMethodException {

//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("javascript");
//        String jsFileName = "D:\\projects\\crawl-data\\main.js";
//        // 读取js文件
//        FileReader reader = new FileReader(jsFileName);
//
//        // 执行指定脚本
//        engine.eval(reader);
//        if(engine instanceof Invocable) {
//            Invocable invoke = (Invocable)engine;
//            String s = (String) invoke.invokeFunction("setGatewayCookie");
//            System.out.println("s = " + s);
//        }
//        reader.close();

        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        CloseableHttpResponse response = null;
        String html = null;
        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet(url);
        try {
            request.setHeader("User-Agent", USER_AGENT);
            request.setHeader("referer", url);
            request.setConfig(requestConfig);
            request.setHeader("cookie","_bl_uid=L9kI1kOgcFpbh07bg91F91kd9phC; __g=sem; Hm_lvt_194df3105ad7148dcf2b98a91b5e727a=1614836821; wt2=tPsqDXxPWhouOAah; lastCity=100010000; __l=l=%2Fwww.zhipin.com%2Fc101220100%2F%3Fpage%3D1%26ka%3Dpage-1&r=https%3A%2F%2Fwww.baidu.com%2Fbaidu.php%3Fsc.060000KKXYb9K48fSqGl06kj41lzbVNF748Qa38gY7zTMSWTFhj6nywFlwSKM1La3uTiPLorjiaj8WPDUTzPv7vGoBejedK5bziI3FjTH3JzhB-CvB6i6q6ITeaseVT-EGskG3FvnoW4yASk9n9N2aIab2dEN0oz9aDXIKSLHsUrd2eYhdNacn5nFC802qXG9-6hjVfwTtE0c1Rzdljh_ALl2j61.DY_NR2Ar5Od663rj6t8AGSPticrZA1AlaqM766WHGek3hcYlXE_sgn8mE8kstVerQKMks4OgSWS534Oqo4yunOogEOtZV_zyUr1oWC_knyuQnhxj4e_rOW9vN3x5ksePSZutrZ1vmxUqTr14ITDHljAZ1LmIOZj_osSxW9vUtr13TSZ1en5o_se5gj4en5ot_5ot8EpMwsrh8mEIZZ9tOZjEqT5ovmx5u9vN3IS1j4SrZdSyZxg9tOZjexgjELqmmLmFCR_g89CgqubL5hHjfmJCRnTXZWeTrHl3TMd3rj6t8AGSPtinrHlkGY3TMdo_4JN9h9m3qrHIv20.U1Yk0ZDqmhq1TsKspynqn0KY5yFETL5y_Tp31xWNE6KGUHYznWR0u1dEuZCk0ZNG5yF9pywd0ZKGujYz0APGujYYnj60UgfqnH0krNtknjDLg1nknWwxnH0YP7tknjc1g1nvnjD0pvbqn0KzIjYkP1f0mhbqnHRdg1Ddr7tznjwxnWDLg1RsnsKVm1Yknj0kg1D3rjczPWD1nWNxnH0zg1Dsn-tkg100TgKGujYs0Z7Wpyfqn0KzuLw9u1Ys0A7B5HKxn0K-ThTqn0KsTjYs0A4vTjYsQW0snj0snj0s0AdYTjYs0AwbUL0qn0KzpWYs0Aw-IWdsmsKhIjYs0ZKC5H00ULnqn0KBI1Ykn0K8IjYs0ZPl5fK9TdqGuAnqTZnVuLGCXZb0pywW5R9rf6KYmgFMugfqn17xn1DYg1D40ZwdT1YknH6snW0srH6knHDkPWcsnH0kr0Kzug7Y5HDvnHf3n1m3njbkPWb0Tv-b5yfvmy7hPvFBnj0snjcLnjm0mLPV5HT1rjnzfHmLfHI7P1mvPHn0mynqnfKsUWYs0Z7VIjYs0Z7VT1Ys0Aw-I7qWTADqn0KlIjYs0AdWgvuzUvYqn7tsg1KxnH0YP-tsg100uA78IyF-gLK_my4GuZnqn7tsg1f1nH04rHNxPjnknjb4PNtsg100TA7Ygvu_myTqn0Kbmv-b5H00ugwGujYVnfK9TLKWm1Ys0ZNspy4Wm1Ys0Z7VuWYs0AuWIgfqn0KGTvP_5H00XMK_Ignqn0K9uAu_myTqnfK_uhnqn0KbmvPb5HDkPRnsP1n3rj9af1TsrHTLwW-DPjc4nRcLfbNjwjT3xj00IZF9uARqP1msnW0z0AFbpyfqfRfknR7DwWm4rH0YPY7jnbNDrRfswWmYrjPjnjuaPbm0UvnqnfKBIjY10Aq9IZTqnWDsrjnLn1RsPHcs0AqY5HD0ULFsIjYzc10WnH0WnBn1PjmkrHnkn10WnHcsnj0WnHcsnj08nj0snj0sc1DWnBnsczYWna3snHnLnjfWna34rH0snj00TNqv5H08rjuxna3sn7tsQW0sg108PWuxna3vndtsQWnsg1Dsna3sn7ts0AF1gLKzUvwGujYs0A-1gvPsmHYs0APs5H00mLFW5Hn3nWfs%26word%3D&g=%2Fwww.zhipin.com%2Fsem%2F10.html%3Fsid%3Dsem%26qudao%3Dbdpc_baidu-pc-BOSS-JD02-B19KA02084%26plan%3D0202-%25E5%2593%2581%25E7%2589%258C%25E8%25AF%258D%26unit%3D%25E5%2593%2581%25E7%2589%258C-%25E4%25B8%258B%25E8%25BD%25BD%26keyword%3Dboss%25E7%259B%25B4%25E8%2581%2598%25E7%25BD%2591%25E7%25AB%2599%26bd_vid%3D11320564472218264289%26csource%3Dboctb&s=3&friend_source=0&s=3&friend_source=0; __c=1614836820; __a=63919293.1611563195.1611563195.1614836820.27.2.19.19; __zp_stoken__=a243bEBETVFlMew4qAj4BPRx5P1xVBlQZeSFbOhsWGSIZWjRgUmxvc3Z6PzpSb3RINmQPV0scOmFfHTYXAUh7ZWoYPT9aGUg%2FYhBjcTx8OEwCd34wVkpbMUtoNABWQhNWMDUFbgRnNWddGAp0Fg%3D%3D; Hm_lpvt_194df3105ad7148dcf2b98a91b5e727a=1614842623");
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);
            //4.判断响应状态为200，进行处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                html = EntityUtils.toString(httpEntity, "GBK");
            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return html;
    }
}