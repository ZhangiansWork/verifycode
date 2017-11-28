package cn.createsoft.service.impl;

import cn.createsoft.map.UserKeyMapper;
import cn.createsoft.model.UserKey;
import cn.createsoft.service.UserKeyService;
import cn.createsoft.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserKeyServiceImpl extends BaseService<UserKey> implements UserKeyService{

    @Autowired
    private UserKeyMapper userKeyMapper;

    @Override
    public boolean instertKeys(UserKey uk) {
        if (!StringUtil.isNotNullorEmpty(uk.getPhoneNum())
                &&!StringUtil.isNotNullorEmpty(uk.getPublicKey())
                &&StringUtil.isNotNullorEmpty(uk.getKeyId())){
            return false;
        }
        int count = userKeyMapper.insert(uk);
        if (count !=0){
            return true;
        }
        return false;
    }

    @Override
    public UserKey selectKey(UserKey uk) {
        if (!StringUtil.isNotNullorEmpty(uk.getKeyId())&&StringUtil.isNotNullorEmpty(uk.getPhoneNum())){
            return new UserKey();
        }
        UserKey res = userKeyMapper. selectOne(uk);
        return res;
    }
}
