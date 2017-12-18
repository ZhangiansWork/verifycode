package cn.createsoft.service.impl;

import cn.createsoft.map.UserKeyMapper;
import cn.createsoft.map.UserMapper;
import cn.createsoft.model.User;
import cn.createsoft.model.UserKey;
import cn.createsoft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseService<User> implements UserService {


    @Autowired
    private UserMapper uMapper;

    @Autowired
    private UserKeyMapper ukMapper;

    @Override
    public int saveUser(String phoneNum, String password, Map<Integer, String> keys, String token) {
        User tempUser = new User();
        tempUser.setPhoneNum(phoneNum);
//        List<User> isExist =uMapper.select(tempUser);
        List<User> isExist = uMapper.selectByPhoneNum(phoneNum);
        System.out.println(isExist.size());
        if (isExist.size()!=0){
            return -2 ;// already exist
        }
        tempUser.setPassword(password);
        tempUser.setToken(token);
        int uRes = uMapper.insert(tempUser);
        if (uRes == 0){
            return 0; // save user err
        }

//        for(Object key : keys.keySet()) {
//            String value = (String) keys.get(key);
//            UserKey tempUserKey  = new UserKey();
//            tempUserKey.setKeyId(Integer.parseInt(String.valueOf(key)));
//            tempUserKey.setPhoneNum(phoneNum);
//            tempUserKey.setPublicKey(value);
//            int ukRes = ukMapper.insert(tempUserKey);
//            if (ukRes ==0){
//                return -1; // save key err
//            }
//        }
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

    @Override
    public void updateToken(User u ){
        uMapper.updateByPrimaryKeySelective(u);
    }
}
