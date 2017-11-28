package cn.createsoft.service.impl;

import cn.createsoft.map.ServerKeyMapper;
import cn.createsoft.model.ServerKey;
import cn.createsoft.service.CryptoService;
import cn.createsoft.service.ServerKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerKeyServiceImpl extends BaseService<ServerKey> implements ServerKeyService{


    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private ServerKeyMapper skMapper;

    @Override
    public ServerKey randomChoose() {
        String random = cryptoService.randomNum();
        ServerKey selectTemp =new ServerKey();
        selectTemp.setKeyId(Integer.valueOf(random));
        ServerKey randomKey = skMapper.selectOne(selectTemp);
        return randomKey;
    }

    @Override
    public ServerKey select(int id) {
        ServerKey tempServerKey = new ServerKey();
        tempServerKey.setKeyId(id);
        ServerKey res = skMapper.selectOne(tempServerKey);
        return res;
    }
}
