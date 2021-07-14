package com.etocrm.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class HttpInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
        logger.info("Request Content:"  + httpRequest.getURI() + "\t Request Headers：" + httpRequest.getHeaders() );
        logger.info("Response Headers      : {}" ,response.getHeaders());//WARNING: comment out in production to improve performance
        return response;
    }

}
