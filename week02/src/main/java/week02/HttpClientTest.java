package week02;/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuwh
 * @version HttpClientTest.java, v 0.1 2021年06月28日 14:42 wuwh
 */
public class HttpClientTest {

    private static final Logger LOGGER = LoggerFactory.getLogger("DEFAULT");

    public static void main(String[] args) {
        String url = "http://localhost:8801";
        String result = HttpClientUtil.get(url);
        LOGGER.info("请求{},返回:{}", url, result);
    }

}