package cn.createsoft.service;

import cn.createsoft.model.User;

import java.util.Map;

public interface UserService extends IService<User>{

    int saveUser(String phoneNum, String password, Map<Integer,String> keys);
    User selectUser(String phoneNum, String password);
}
