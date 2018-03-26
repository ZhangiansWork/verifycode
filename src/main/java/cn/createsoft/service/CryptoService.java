package cn.createsoft.service;

import cn.createsoft.util.saltlib.Box;

import java.security.SecureRandom;

public interface CryptoService {
    /**
     * @description 生成nonce
     * */
    byte[] generateNonce();
    /**
     * @description 生成非对称密钥
     * */
    Box.KeyPair generateKeyPair();
    /**
     * @description 3-DH密钥交换
     * @param recIdentityPublicKey 接收方身份公钥
     * @param selfIdentityPrivateKey 自己的身份私钥
     * @param recRandomPublicKey 接收方随机公钥
     * @param selfRandomPrivateKey 自己的随机私钥
     * */
    String generateSecret(String recRandomPublicKey,String recIdentityPublicKey, String selfRandomPrivateKey, String selfIdentityPrivateKey);
    /**
     * @description 蝾螈棘轮协议
     * @param input
     * @param key
     * @param string
     * */
    String[] axolotlProtocol(String input, String key, String string) throws Exception;
    /**
     * @description ECDH密钥交换协议
     * @param selfPrivateKey 自己的某个私钥
     * @param recPublicKey 接收方的某个公钥
     * */
    byte[] ECDHKeyAgreement(String selfPrivateKey, String recPublicKey);

    /**
     * @description AESCTR加密
     * */
    String aesCTR(String data,String key, String IV) throws Exception;

    /**
     * @description AESCBC加密
     * */
    String aesCBC(String data,String key, String IV) throws Exception;

    /**
     * @description AESCBC解密
     * */
    String decryptCBC(String cipher, String key, String IV) throws Exception;

    /**
     * @description AESCTR解密
     * */
    String decryptCTR(String cipher, String key ,String IV) throws Exception;

    /**
     * @description 生成范围内随机数
     * */
    String randomNum();
}
