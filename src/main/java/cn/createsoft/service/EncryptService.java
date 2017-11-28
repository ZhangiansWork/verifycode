package cn.createsoft.service;

public interface EncryptService {

    String encrypt(String phoneNum, String code) throws Exception;

    String decrypt(String cipher,String selfIndentityPrivateKey,String serverIdentityPublicKey, String phoneNum) throws Exception;
}
