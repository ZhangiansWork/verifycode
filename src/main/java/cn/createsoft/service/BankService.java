package cn.createsoft.service;

import cn.createsoft.model.BankUser;

import java.util.List;

public interface BankService extends IService<BankUser> {
    /**
     * @description 银行用户注册
     * @Param userName 用户名
     * */
    String signup(String userName);
    /**
     * @description 查询
     * @Param userName 用户名
     * */
    BankUser select(String userName);
}
