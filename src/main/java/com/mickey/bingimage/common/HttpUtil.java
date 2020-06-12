package com.mickey.bingimage.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description
 *
 * @author mickey
 * 2020-02-14
 */
@Slf4j
public class HttpUtil {
    //链接超时
    private static int CONNECTION_TIME_OUT = 600000;
    //http客户端
    private static CloseableHttpClient httpClient;
    //同步锁
    private static Lock lock = new ReentrantLock();

    public static <T> Response<T> get(String url, Map<String, Object> params, Map<String, Object> headers) throws IOException {
        url = addParamsToUrl(url, params);
        HttpRequestBase httpGet = new HttpGet(url);
        return getResponse(callHttpGet(url, headers, httpGet));
    }

    public static Response get4InputStream(String url, Map<String, Object> params, Map<String, Object> headers) throws IOException {
        url = addParamsToUrl(url, params);
        HttpRequestBase httpGet = new HttpGet(url);
        return getResponse4InputStream(callHttpGet(url, headers, httpGet));
    }

    public static Response post(String url, Map<String, Object> params, Map<String, Object> headers) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpEntityEnclosingRequestBase httpPost = new HttpPost(url);
        return getResponse(callHttpPostWithContent(params, headers, httpClient, httpPost));
    }

    public static Response post(String url, String content, Map<String, Object> headers) throws IOException {
        HttpEntityEnclosingRequestBase httpPost = new HttpPost(url);
        RequestConfig postConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIME_OUT).build();
        httpPost.setConfig(postConfig);
        CloseableHttpClient httpClient = getHttpClient(url);
        return getResponse(callHttpPostWithParams(content, headers, httpPost, httpClient));
    }

    public static Response post(String url, byte[] bytes, Map<String, Object> headers) throws IOException {
        HttpEntityEnclosingRequestBase httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = getHttpClient(url);
        return getResponse(callHttpPostWithParams(bytes, headers, httpPost, httpClient));
    }

    public static Response put(String url, Map<String, Object> params, Map<String, Object> headers) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpEntityEnclosingRequestBase httpPut = new HttpPut(url);
        return getResponse(callHttpPostWithContent(params, headers, httpClient, httpPut));
    }

    public static Response put(String url, String content, Map<String, Object> headers) throws IOException {
        HttpEntityEnclosingRequestBase httpPut = new HttpPut(url);
        CloseableHttpClient httpClient = getHttpClient(url);
        return getResponse(callHttpPostWithParams(content, headers, httpPut, httpClient));
    }

    public static Response delete(String uri, String reqBody, Map<String, Object> headers) throws IOException {
        Map<String, Object> params = null;
        if (reqBody != null) {
            params = JSON.parseObject(reqBody, new TypeReference<Map<String, Object>>() {
            }.getType());
        }
        return delete(uri, params, headers);
    }

    public static Response delete(String url, Map<String, Object> params, Map<String, Object> headers) throws IOException {
        url = addParamsToUrl(url, params);
        HttpDelete httpGet = new HttpDelete(url);
        return getResponse(callHttpGet(url, headers, httpGet));
    }

    private static HttpResponse callHttpGet(String url, Map<String, Object> headers, HttpRequestBase httpGet) throws IOException {
        CloseableHttpClient httpClient = getHttpClient(url);
        //
        return callHttp(headers, httpClient, httpGet);
    }

    private static HttpResponse callHttp(Map<String, Object> headers, CloseableHttpClient httpClient, HttpUriRequest httpUriRequest) throws IOException {
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpUriRequest.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        // HTTP请求
        HttpResponse httpResponse = httpClient.execute(httpUriRequest);
        return httpResponse;
    }

    private static HttpResponse callHttpPostWithContent(Map<String, Object> params, Map<String, Object> headers, CloseableHttpClient httpClient, HttpEntityEnclosingRequestBase httpPost) throws IOException {
        // 设置HTTP POST请求参数，必须用NameValuePair对象
        List<BasicNameValuePair> nvps = new ArrayList<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8")); // 设置HTTP请求参数
        //
        return callHttp(headers, httpClient, httpPost);
    }

    private static HttpResponse callHttpPostWithParams(byte[] bytes, Map<String, Object> headers, HttpEntityEnclosingRequestBase httpPost, CloseableHttpClient httpClient) throws IOException {
        httpPost.setEntity(new ByteArrayEntity(bytes)); // 设置HTTP请求参数
        //
        return callHttp(headers, httpClient, httpPost);
    }

    private static HttpResponse callHttpPostWithParams(String content, Map<String, Object> headers, HttpEntityEnclosingRequestBase httpPost, CloseableHttpClient httpClient) throws IOException {
        httpPost.setEntity(new StringEntity(content, "UTF-8")); // 设置HTTP请求参数
        //
        return callHttp(headers, httpClient, httpPost);
    }

    private static <T> Response<T> getResponse(HttpResponse httpResponse) throws IOException {
        Response<T> response = new Response<>();
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        response.setStatusCode(HttpStatusEnum.valueOf(statusCode));

        if (httpResponse.getEntity() != null) {
            response.setHttpEntity(httpResponse.getEntity());
            InputStream inputStream = httpResponse.getEntity().getContent();
            String result = readStringFromInputStream(inputStream, "UTF-8");
            response.setResult(result);
        }
        return response;
    }

    private static Response getResponse4InputStream(HttpResponse httpResponse) throws IOException {
        Response response = new Response();
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        response.setStatusCode(HttpStatusEnum.valueOf(statusCode));

        if (httpResponse.getEntity() != null) {
            response.setHttpEntity(httpResponse.getEntity());
            InputStream inputStream = httpResponse.getEntity().getContent();
            response.setInputStream(inputStream);
        }
        return response;
    }

    public static String readStringFromInputStream(InputStream stream, String charset) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset), 8192);
            StringWriter writer = new StringWriter();

            char[] chars = new char[8192];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("读取文件流异常");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private static CloseableHttpClient getHttpClient(String url) {
        if (url.startsWith("https://")) {
            try {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                    // 信任所有
                    @Override
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                }).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
                return HttpClients.custom().setSSLSocketFactory(sslsf).build();
            } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                log.error(e.getMessage());
            }
            return HttpClients.createDefault();
        } else {
            return getHttpClient();
        }
    }

    private static CloseableHttpClient getHttpClient() {
        if (httpClient != null) {
            return httpClient;
        } else {
            CloseableHttpClient httpClientBuilder;
            lock.lock();
            try {
                if (httpClient == null) {
                    HttpClientBuilder httpClientBuilder1 = HttpClients.custom();
                    String config = "200";
                    Integer poolSize = Integer.valueOf(config);
                    poolSize = Integer.valueOf(poolSize.intValue() > 1000 ? 1000 : poolSize.intValue());
                    poolSize = Integer.valueOf(poolSize.intValue() < 10 ? 10 : poolSize.intValue());
                    httpClientBuilder1.setMaxConnTotal(poolSize.intValue());
                    httpClientBuilder1.setMaxConnPerRoute(poolSize.intValue());
                    httpClientBuilder1.disableCookieManagement();
                    httpClientBuilder1.disableAutomaticRetries();
                    httpClientBuilder1.setConnectionTimeToLive(60L, TimeUnit.SECONDS);
                    RequestConfig.Builder builder = RequestConfig.custom();
                    builder.setConnectTimeout(CONNECTION_TIME_OUT);
                    httpClientBuilder1.setDefaultRequestConfig(builder.build());
                    httpClientBuilder1.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);
                    httpClientBuilder1.disableAuthCaching();
                    httpClientBuilder1.disableCookieManagement();
                    httpClientBuilder1.disableRedirectHandling();
                    httpClient = httpClientBuilder1.build();
                    CloseableHttpClient arg3 = httpClient;
                    return arg3;
                }

                httpClientBuilder = httpClient;
            } finally {
                lock.unlock();
            }

            return httpClientBuilder;
        }
    }

    private static String addParamsToUrl(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer(url);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(String.format("%s%s=%s", sb.indexOf("?") == -1 ? "?" : "&", URLEncoder.encode(entry.getKey(), "UTF-8"), URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8")));
            }
        }
        url = sb.toString();
        return url;
    }

    public static class Response<T>  {
        HttpStatusEnum statusCode;

        String result;

        T object;

        HttpEntity httpEntity;

        InputStream inputStream;

        public HttpStatusEnum getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(HttpStatusEnum statusCode) {
            this.statusCode = statusCode;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;

        }

        public HttpEntity getHttpEntity() {
            return httpEntity;
        }

        public void setHttpEntity(HttpEntity httpEntity) {
            this.httpEntity = httpEntity;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }
    }
}