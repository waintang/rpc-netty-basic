package ml.wpxm.java.rpc.netty.basic.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import ml.wpxm.java.rpc.netty.basic.constants.ReqType;
import ml.wpxm.java.rpc.netty.basic.constants.RpcConstant;
import ml.wpxm.java.rpc.netty.basic.core.Header;
import ml.wpxm.java.rpc.netty.basic.core.RpcProtocol;
import ml.wpxm.java.rpc.netty.basic.core.RpcRequest;
import ml.wpxm.java.rpc.netty.basic.core.RpcResponse;
import ml.wpxm.java.rpc.netty.basic.serial.ISerializer;
import ml.wpxm.java.rpc.netty.basic.serial.SerializerManager;

import java.util.List;

import static ml.wpxm.java.rpc.netty.basic.constants.ReqType.REQUEST;
import static ml.wpxm.java.rpc.netty.basic.constants.ReqType.RESPONSE;

@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("---------- begin RpcDecoder-----------");

        if(byteBuf.readableBytes()< RpcConstant.HEAD_TOTAL_LEN){
            return ;
        }
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
        if(magic != RpcConstant.MAGIC){
            return ;
        }
        byte serialType  = byteBuf.readByte();
        byte reqType  = byteBuf.readByte();
        long requestId  = byteBuf.readLong();
        int dataLength  = byteBuf.readInt();
        if(byteBuf.readableBytes() < dataLength){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] content = new byte[dataLength];
        byteBuf.readBytes(content);

        Header header = new Header(magic,serialType,reqType,requestId,dataLength);
        ISerializer iSerializer = SerializerManager.getSerializer(serialType);
        ReqType reqTypeEnm = ReqType.findByCode(reqType);
        switch (reqTypeEnm){
            case REQUEST :
                RpcRequest rpcRequest = iSerializer.decode(content, RpcRequest.class);
                RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
                reqProtocol.setHeader(header);
                reqProtocol.setContent(rpcRequest);
                list.add(reqProtocol);
                break;
            case RESPONSE:
                RpcResponse rpcResponse = iSerializer.decode(content, RpcResponse.class);
                RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
                resProtocol.setHeader(header);
                resProtocol.setContent(rpcResponse);
                list.add(resProtocol);
                break;
            default:
                //TODO
                break;
        }

        log.info("---------- end RpcDecoder-------------");
    }
}
