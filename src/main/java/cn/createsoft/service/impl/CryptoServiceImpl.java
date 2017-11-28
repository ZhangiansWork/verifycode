package cn.createsoft.service.impl;

import cn.createsoft.service.CryptoService;
import cn.createsoft.util.saltlib.AESCoder;
import cn.createsoft.util.saltlib.Box;
import cn.createsoft.util.saltlib.Coder;
import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.SecureRandom;


@Service
public class CryptoServiceImpl implements CryptoService {
    @Override
    public byte[] generateNonce() {
        byte[] nouce = new byte[24];
        SecureRandom r = new SecureRandom();
        r.nextBytes(nouce);
//        StringBuffer hexString = new StringBuffer();
//        for (int i = 0; i < aesKey.length; i++) {
//            String hex = Integer.toHexString(0xff & aesKey[i]);
//            if (hex.length() == 1)
//                hexString.append('0');
//            hexString.append(hex);
//        }
        return nouce;
    }

    @Override
    public Box.KeyPair generateKeyPair() {
        Box.KeyPair keyPairs = Box.generate();
        return keyPairs;
    }

    @Override
    public String generateSecret(String recRandomPublicKey, String recIdentityPublicKey, String selfRandomPrivateKey, String selfIdentityPrivateKey) {
        byte[] key1 = ECDHKeyAgreement(selfIdentityPrivateKey,recRandomPublicKey);
        byte[] key2 = ECDHKeyAgreement(selfRandomPrivateKey,recIdentityPublicKey);
        byte[] key3 = ECDHKeyAgreement(selfRandomPrivateKey,recRandomPublicKey);
        String fisrtKey = DatatypeConverter.printBase64Binary(key1);
        String secondKey = DatatypeConverter.printBase64Binary(key2);
        String thirdKey = DatatypeConverter.printBase64Binary(key3);
        return fisrtKey+"|"+secondKey+"|"+thirdKey;
    }

    @Override
    public String[] axolotlProtocol(String input, String key, String string) throws Exception {
        byte[] Keypr = Coder.encryptHMAC(DatatypeConverter.parseHexBinary(input),key);
        byte[] Key0 = Coder.encryptHMAC(DatatypeConverter.parseHexBinary(string+"00000000"),DatatypeConverter.printBase64Binary(Keypr));
        byte[] Key1 = Coder.encryptHMAC(DatatypeConverter.parseHexBinary(DatatypeConverter.printHexBinary(Key0)+string+"00000001"),DatatypeConverter.printBase64Binary(Keypr));
        String[] res = new String[2];
        res[0] = DatatypeConverter.printBase64Binary(Key0);
        res[1] = DatatypeConverter.printBase64Binary(Key1);
        return res;
    }

    @Override
    public byte[] ECDHKeyAgreement(String selfPrivateKey, String recPublicKey) {
        byte[] privateKey = DatatypeConverter.parseHexBinary(selfPrivateKey);
        byte[] publicKey = DatatypeConverter.parseHexBinary(recPublicKey);
        Box keyshared = new Box(publicKey, privateKey);
//        keyshared.agreement(publicKey,privateKey);
        return keyshared.agreement(publicKey,privateKey);
    }

    @Override
    public String aesCTR(String data, String key, String IV) throws Exception {
        Key k = new SecretKeySpec(DatatypeConverter.parseHexBinary(key),"AES");
        byte[] cipherByte = AESCoder.encrypt(DatatypeConverter.parseHexBinary(data),k,"AES/CTR/NoPadding",DatatypeConverter.parseBase64Binary(IV));
        String cipher = DatatypeConverter.printBase64Binary(cipherByte);
        return cipher;
    }

    @Override
    public String aesCBC(String data, String key, String IV) throws Exception {
        Key k = new SecretKeySpec(DatatypeConverter.parseHexBinary(key),"AES");
        byte[] cipherByte = AESCoder.encrypt(DatatypeConverter.parseHexBinary(data),k,"AES/CBC/PKCS5Padding",DatatypeConverter.parseBase64Binary(IV));
        String cipher = DatatypeConverter.printBase64Binary(cipherByte);
        return cipher;

    }

    @Override
    public String decryptCBC(String cipher, String key, String IV) throws Exception {
        Key k = new SecretKeySpec(DatatypeConverter.parseHexBinary(key),"AES");
        byte[] dataByte = AESCoder.decrypt(DatatypeConverter.parseHexBinary(cipher),k,"AES/CBC/PKCS5Padding",DatatypeConverter.parseBase64Binary(IV));
        String data = DatatypeConverter.printBase64Binary(dataByte);
        return data;
    }

    @Override
    public String decryptCTR(String cipher, String key, String IV) throws Exception {
        Key k = new SecretKeySpec(DatatypeConverter.parseHexBinary(key),"AES");
        byte[] dataByte = AESCoder.decrypt(DatatypeConverter.parseHexBinary(cipher),k,"AES/CTR/NoPadding",DatatypeConverter.parseBase64Binary(IV));
        String data = DatatypeConverter.printBase64Binary(dataByte);
        return data;
    }

    @Override
    public String randomNum() {
        int temp = (int)(Math.random()*50);
        return String.valueOf(temp);
    }
}
