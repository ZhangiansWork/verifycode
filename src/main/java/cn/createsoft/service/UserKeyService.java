package cn.createsoft.service;

import cn.createsoft.model.UserKey;

public interface UserKeyService extends IService<UserKey>{

//    boolean instertKeys(UserKey uk);

    /**
     * @description 用户密钥查询（公钥）
     * */
    UserKey selectKey(UserKey uk);
}
