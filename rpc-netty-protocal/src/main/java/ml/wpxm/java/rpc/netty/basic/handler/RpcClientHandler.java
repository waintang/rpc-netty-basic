package ml.wpxm.java.rpc.netty.basic.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.rpc.netty.basic.core.RequestHolder;
import ml.wpxm.java.rpc.netty.basic.core.RpcFuture;
import ml.wpxm.java.rpc.netty.basic.core.RpcProtocol;
import ml.wpxm.java.rpc.netty.basic.core.RpcResponse;

/**
 * 按我理解：
 * 客户端 接收的是  Response，不需要再后续处理（不需要再response）
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        log.info("receive Rpc server Result");
        long requestId = rpcResponseRpcProtocol.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RequestHolder.REQUEST_MAP.remove(requestId);
        // 曾犯错四： Promise等待的对象 和 代理返回的对象，值类型要对应上！
        future.getPromise().setSuccess(rpcResponseRpcProtocol.getContent());
    }
}
