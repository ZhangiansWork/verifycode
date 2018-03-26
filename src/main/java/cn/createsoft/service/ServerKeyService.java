package cn.createsoft.service;

import cn.createsoft.model.ServerKey;

public interface ServerKeyService extends IService<ServerKey>{

//    ServerKey selectOne(ServerKey sk);

    /**
     * @description 随机选择密钥
     * */
    ServerKey randomChoose();

    /**
     * @description 查找某一对密钥
     * */
    ServerKey select(int id);
}
