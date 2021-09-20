package ml.wpxm.java.rpc.netty.basic.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ml.wpxm.java.rpc.netty.basic.constants.RpcConstant;
import ml.wpxm.java.rpc.netty.basic.core.Header;
import ml.wpxm.java.rpc.netty.basic.core.RpcProtocol;
import ml.wpxm.java.rpc.netty.basic.serial.ISerializer;
import ml.wpxm.java.rpc.netty.basic.serial.SerializerManager;

public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol<Object> tRpcProtocol, ByteBuf byteBuf) throws Exception {
        Header header = tRpcProtocol.getHeader();
        Object content = tRpcProtocol.getContent();
        byte serialType = header.getSerialType();
        byte reqType = header.getReqType();
        byteBuf.writeShort(RpcConstant.MAGIC);
        // 此三者 需要 上游传入
        byteBuf.writeByte(header.getSerialType());
        byteBuf.writeByte(header.getReqType());
        byteBuf.writeLong(header.getRequestId());
        ISerializer iSerializer = SerializerManager.getSerializer(serialType);
        byte[] bytes = iSerializer.encode(content);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
