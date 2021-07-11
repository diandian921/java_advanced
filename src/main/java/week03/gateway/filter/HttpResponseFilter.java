package week03.gateway.filter;

import io.netty.handler.codec.http.HttpResponse;

/**
 * @author Created by diandian
 * @date 2021/7/11.
 */
public interface HttpResponseFilter extends Filter<HttpResponse> {

}
