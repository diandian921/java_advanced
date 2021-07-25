package gateway.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

/**
 * @author Created by diandian
 * @date 2021/7/11.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final List<String> hosts;

    public ServerInitializer(List<String> hosts) {
        this.hosts= hosts;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpInBoundHandler(hosts));
    }
}
