package week02;/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author wuwh
 * @version HttpClientUtil.java, v 0.1 2021年06月28日 15:09 wuwh
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("DEFAULT");

    public static String get(String url) throws HttpProcessException {
        return get(url, null, null);
    }

    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws HttpProcessException {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key));
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(httpGet::setHeader);
            }

            HttpResponse httpResponse = httpclient.execute(httpGet);
            LOGGER.info("请求{}，statusCode={}", url, httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return convertResult(httpResponse);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new HttpProcessException(e);
        }
    }

    /**
     * 结果转换
     *
     * @return
     */
    private static String convertResult(HttpResponse httpResponse) {
        if (httpResponse == null) {
            return null;
        }
        String result = null;
        try {
            result = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            EntityUtils.consume(httpResponse.getEntity());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new HttpProcessException(e);
        } finally {
            close(httpResponse);
        }
        return result;
    }

    /**
     * 尝试关闭response
     *
     * @param resp HttpResponse对象
     */
    private static void close(HttpResponse resp) {
        try {
            if (resp == null) { return; }
            if (CloseableHttpResponse.class.isAssignableFrom(resp.getClass())) {
                ((CloseableHttpResponse) resp).close();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}