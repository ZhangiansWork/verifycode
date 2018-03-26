package cn.createsoft.service;

public interface EncryptService {

    /**
     * @description 加密
     * @param code 验证码
     * @param phoneNum 接收方手机号
     * */
    String encrypt(String phoneNum, String code) throws Exception;

    /**
     * @description 解密
     * @param cipher 密文
     * @param phoneNum 接收方手机号
     * @param selfIndentityPrivateKey 自己身份私钥
     * @param serverIdentityPublicKey 服务器身份公钥
     * */
    String decrypt(String cipher,String selfIndentityPrivateKey,String serverIdentityPublicKey, String phoneNum) throws Exception;
}
