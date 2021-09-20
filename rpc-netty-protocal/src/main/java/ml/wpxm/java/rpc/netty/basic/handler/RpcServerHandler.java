package ml.wpxm.java.rpc.netty.basic.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ml.wpxm.java.rpc.netty.basic.core.Header;
import ml.wpxm.java.rpc.netty.basic.core.RpcProtocol;
import ml.wpxm.java.rpc.netty.basic.core.RpcRequest;
import ml.wpxm.java.rpc.netty.basic.core.RpcResponse;
import ml.wpxm.java.rpc.netty.basic.spring.SpringBeanManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static ml.wpxm.java.rpc.netty.basic.constants.ReqType.RESPONSE;

/**
 * 按目前 我的理解：
 * 服务端 接收的是 请求，此处处理完，返回Response
 *
 * 此前 已经 decode
 * 此后 会自动 encode
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequestRpcProtocol) throws Exception {
        RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
        Header header = rpcRequestRpcProtocol.getHeader();
        header.setReqType(RESPONSE.code());
        Object response = invoke(rpcRequestRpcProtocol.getContent());
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setData(response);
        rpcResponse.setMsg("success.");
        // header五参数，其三不变 magic、serialType、requestId  其一reqType 必为响应 其一后续encode时，才能确认
        resProtocol.setHeader(header);
        resProtocol.setContent(rpcResponse);
        // netty会自动 调用 encoder方法 进行加密
        channelHandlerContext.writeAndFlush(resProtocol);
    }

    private Object invoke(RpcRequest rpcRequest){
        try {
            Class<?> clz = Class.forName(rpcRequest.getClassName());
            Object bean = SpringBeanManager.getBean(clz);
            // 曾犯错二：注意：methodName、parameterTypes 两者都得填齐（因为有重载）
            Method method = clz.getDeclaredMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
            // 曾犯错三：invoke 指定 对象、 参数值 即可（不用指定参数类型）
            return method.invoke(bean,rpcRequest.getParams());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
