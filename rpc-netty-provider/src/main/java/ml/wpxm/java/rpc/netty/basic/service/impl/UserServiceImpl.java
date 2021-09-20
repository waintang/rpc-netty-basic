package ml.wpxm.java.rpc.netty.basic.service.impl;

import ml.wpxm.java.rpc.netty.basic.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 曾犯错一：被spring容器管理起来，必须加Service注解
 */
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public String saveUser(String name) {
        return "保存用户成功："+name;
    }
}
