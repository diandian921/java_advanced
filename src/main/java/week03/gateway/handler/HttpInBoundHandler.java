package week03.gateway.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import week02.HttpClientUtil;
import week03.gateway.filter.Filter;
import week03.gateway.filter.HttpRequestHeaderFilter;
import week03.gateway.filter.HttpResponseHeaderFilter;
import week03.gateway.router.RandomRouter;
import week03.gateway.router.Router;

import java.net.URI;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Created by diandian
 * @date 2021/7/11.
 */
public class HttpInBoundHandler extends ChannelInboundHandlerAdapter {

    private final List<String> hosts;

    private final Filter<HttpRequest> inboundFilter = new HttpRequestHeaderFilter();
    private final Filter<HttpResponse> outBoundFilter = new HttpResponseHeaderFilter();
    private final Router router = new RandomRouter();

    public HttpInBoundHandler(List<String> hosts) {
        this.hosts = hosts;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof HttpRequest)) {
            return;
        }
        HttpRequest httpRequest = (HttpRequest) msg;
        URI uri = new URI(httpRequest.uri());
        if("/favicon.ico".equals(uri.getPath())) {
            return;
        }
        handle(ctx, httpRequest);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void handle(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        FullHttpResponse response = null;
        try {
            //通过路由获取目标地址
            String target = router.route(this.hosts);

            //filter
            inboundFilter.filter(httpRequest);

            //请求目标地址获取结果
            String res = HttpClientUtil.get(target);

            //将结果写回
            ByteBuf content = Unpooled.copiedBuffer(res, CharsetUtil.UTF_8);
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            outBoundFilter.filter(response);
        } catch (Exception e) {
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (HttpUtil.isKeepAlive(httpRequest)) {
                ctx.write(response);
            } else {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
