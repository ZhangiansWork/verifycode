package cn.createsoft.service;

import cn.createsoft.model.User;

import java.util.Map;

public interface UserService extends IService<User>{

    /**
     * @description 插入用户
     * @param phoneNum 手机号
     * @param keys 公钥<序号，base64后的公钥>
     * @param password 密码
     * @param token 验证码
     * */
    int saveUser(String phoneNum, String password, Map<Integer,String> keys, String token);

    /**
     * @description 查找用户
     * @param password 密码
     * @param phoneNum 手机号
     * */
    User selectUser(String phoneNum, String password);

    /**
     * @description 更新token
     * @param u User类型对象
     * */
    void updateToken (User u);
}
