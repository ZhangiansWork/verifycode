package cn.createsoft.service.impl;

import cn.createsoft.map.BankUserMap;
import cn.createsoft.model.BankUser;
import cn.createsoft.model.UserKey;
import cn.createsoft.service.BankService;
import cn.createsoft.service.UserKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl extends BaseService<BankUser> implements BankService {

    @Autowired
    private BankUserMap buMap;

    @Override
    public String signup(String userName) {
        BankUser tempBu = new BankUser();
        tempBu.setUser_name(userName);
        BankUser bu = buMap.selectOne(tempBu);
        if (bu==null){
            int res =buMap.insert(tempBu);
            if (res ==1){
                return "insert ok";
            }
            return "insert err";
        }
        return "exist";
    }

    @Override
    public BankUser select(String userName) {
        BankUser tempBu = new BankUser();
        tempBu.setUser_name(userName);
        BankUser bu = buMap.selectOne(tempBu);
        if (bu==null){
            return null;
        }
        return bu;
    }
}
