package cn.createsoft.service;

import cn.createsoft.model.BankUser;

import java.util.List;

public interface BankService extends IService<BankUser> {
    String signup(String userName);

    BankUser select(String userName);
}
