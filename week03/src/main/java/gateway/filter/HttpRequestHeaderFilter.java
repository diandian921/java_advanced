package gateway.filter;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @author Created by diandian
 * @date 2021/7/11.
 */
public class HttpRequestHeaderFilter implements HttpRequestFilter {

    @Override
    public void filter(HttpRequest httpRequest) {
        httpRequest.headers().set("headerTest","xiaowu");
    }
}
