package cn.createsoft.service.impl;

import cn.createsoft.map.UserKeyMapper;
import cn.createsoft.map.UserMapper;
import cn.createsoft.model.User;
import cn.createsoft.model.UserKey;
import cn.createsoft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl extends BaseService<User> implements UserService {


    @Autowired
    private UserMapper uMapper;

    @Autowired
    private UserKeyMapper ukMapper;

    @Override
    public int saveUser(String phoneNum, String password, Map<Integer, String> keys) {

        User tempUser = new User();
        tempUser.setPhoneNum(phoneNum);
        tempUser.setPassword(password);
        User isExist =uMapper.selectOne(tempUser);
        if (isExist!=null){
            return -2 ;// already exist
        }

        int uRes = uMapper.insert(tempUser);
        if (uRes == 0){
            return 0; // save user err
        }


        for(int i=0; i < keys.size();i++){
            UserKey tempUserKey = new UserKey();
            tempUserKey.setKeyId(i);
            tempUserKey.setPublicKey(keys.get(i));
            tempUserKey.setPhoneNum(phoneNum);
            int ukRes = ukMapper.insert(tempUserKey);
            if (ukRes ==0){
                return -1; // save key err
            }
        }

        return 1;// correct
    }

    @Override
    public User selectUser(String phoneNum, String password) {
        User tempUser = new User();
        tempUser.setPhoneNum(phoneNum);
        tempUser.setPassword(password);
        User res = uMapper.selectOne(tempUser);
        return res;
    }
}
