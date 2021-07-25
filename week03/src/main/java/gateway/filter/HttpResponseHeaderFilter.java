package gateway.filter;

import io.netty.handler.codec.http.HttpResponse;

/**
 * @author Created by diandian
 * @date 2021/7/11.
 */
public class HttpResponseHeaderFilter implements HttpResponseFilter {
    @Override
    public void filter(HttpResponse httpResponse) {
        httpResponse.headers().set("resHeaderTest","xiaowu");
    }
}
