package ml.wpxm.java.rpc.netty.basic.serial;

public interface ISerializer {

    byte getType();

    <T> byte[] encode(T object);

    <T> T decode(byte[] bytes,Class<T> clz);

}
