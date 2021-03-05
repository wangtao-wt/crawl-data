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
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wangtao
 */
public class HttpDownloadDynamic {


    public static String getCookie(String url) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\lenovo\\Downloads\\chromedriver_win32\\chromedriver.exe");
        //实例化ChromeDriver对象
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        //获取cookie
        Set<Cookie> cookieSet = driver.manage().getCookies();

        String cookie = "";

        for (Cookie cookie1 : cookieSet) {
            cookie += cookie1.getName() + "=" + cookie1.getValue() + "; ";
        }

        cookie = cookie.substring(0,cookie.length()-2);
        System.out.println(cookie);

        driver.close();
        return cookie;
    }

    /**
     * 设置代理，模范浏览器
     */
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.190 Safari/537.36";

    public static String sendGet(String url, String cookie) throws IOException, ScriptException, NoSuchMethodException, InterruptedException {

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
            request.setHeader("cookie", cookie);
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