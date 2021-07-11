package week03.gateway;

import week03.gateway.server.HttpServer;
import week03.gateway.server.Server;

import java.util.Arrays;
import java.util.List;

/**
 * @author Created by diandian
 * @date 2021/7/11.
 */
public class GatewayServer {

    public static void main(String[] args) {
        List<String> hosts = Arrays.asList("http://www.baidu.com");
        int port = 9999;
        Server server = new HttpServer(hosts, port);
        server.serve();
    }
}
