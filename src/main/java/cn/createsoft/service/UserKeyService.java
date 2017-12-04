package cn.createsoft.service;

import cn.createsoft.model.UserKey;

public interface UserKeyService extends IService<UserKey>{

//    boolean instertKeys(UserKey uk);

    UserKey selectKey(UserKey uk);
}
