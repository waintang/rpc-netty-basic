package ml.wpxm.java.rpc.netty.basic;

import ml.wpxm.java.rpc.netty.basic.protocol.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcNettyProvider {
    public static void main(String[] args) {
        SpringApplication.run(RpcNettyProvider.class,args);
        new NettyServer("127.0.0.1",8888).startNettyServer();
    }
}
