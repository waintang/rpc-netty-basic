package ml.wpxm.java.rpc.netty.basic.service;

public interface IUserService {

    /**
     * 保存用户信息
     * @param name
     * @return
     */
    String saveUser(String name);
}
