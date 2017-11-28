package cn.createsoft.service;

import cn.createsoft.util.saltlib.Box;

import java.security.SecureRandom;

public interface CryptoService {

    byte[] generateNonce();

    Box.KeyPair generateKeyPair();

    String generateSecret(String recRandomPublicKey,String recIdentityPublicKey, String selfRandomPrivateKey, String selfIdentityPrivateKey);

    String[] axolotlProtocol(String input, String key, String string) throws Exception;

    byte[] ECDHKeyAgreement(String selfPrivateKey, String recPublicKey);

    String aesCTR(String data,String key, String IV) throws Exception;

    String aesCBC(String data,String key, String IV) throws Exception;

    String decryptCBC(String cipher, String key, String IV) throws Exception;

    String decryptCTR(String cipher, String key ,String IV) throws Exception;

    String randomNum();
}
