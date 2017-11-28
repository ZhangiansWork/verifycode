package cn.createsoft.service;

import cn.createsoft.model.ServerKey;

public interface ServerKeyService extends IService<ServerKey>{

//    ServerKey selectOne(ServerKey sk);

    ServerKey randomChoose();
    ServerKey select(int id);
}
