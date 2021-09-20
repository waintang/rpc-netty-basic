package ml.wpxm.java.rpc.netty.basic.core;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse implements Serializable {
    private Object data;
    private String msg;
}
