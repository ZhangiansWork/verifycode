package cn.createsoft.service.impl;

import cn.createsoft.map.UserMapper;
import cn.createsoft.model.ServerKey;
import cn.createsoft.model.User;
import cn.createsoft.model.UserKey;
import cn.createsoft.service.CryptoService;
import cn.createsoft.service.EncryptService;
import cn.createsoft.service.ServerKeyService;
import cn.createsoft.service.UserKeyService;
//import cn.createsoft.util.PropertiesUtil;
import cn.createsoft.util.saltlib.AESCoder;
import cn.createsoft.util.saltlib.Coder;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.mysql.fabric.xmlrpc.base.Data;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.SecureRandom;
import java.util.List;


@Service
public class EncryptServiceImpl implements EncryptService {
    protected Logger log = Logger.getLogger("SERVICE");
    @Autowired
    private ServerKeyService serverKeyService;

    @Autowired
    private UserKeyService userKeyService;

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private UserMapper uMapper;

    @Override
    public String encrypt(String phoneNum, String code) throws Exception {
        System.out.println("encrypt………"+code);
        //1. user public keys
        // default first key is identityKey
        // Key1 used in triple Diffie-Hellman
        // Key2 used in ECDH
        UserKey tempUserKey = new UserKey();
        String userRanId1 = cryptoService.randomNum();
//        String userRanId2 = cryptoService.randomNum();
        tempUserKey.setKeyId(Integer.valueOf(userRanId1));
        tempUserKey.setPhoneNum(phoneNum);
        UserKey userKey1 = userKeyService.selectKey(tempUserKey);
//        tempUserKey.setKeyId(userRanId2);
//        UserKey userKey2 = userKeyService.selectKey(tempUserKey);
        tempUserKey.setKeyId(0);
        UserKey userIdentityKey = userKeyService.selectKey(tempUserKey);

        //2. server private keys
        // default first key is identityKey
        // key1 used in triple Diffie-Hellman
        // key2 used in ECDH
        ServerKey serverKey1 = serverKeyService.randomChoose();
        ServerKey serverKey2 = serverKeyService.randomChoose();
        ServerKey serverIdentityKey = serverKeyService.select(1);


        //3. tripleDiffie-Hellman
        String secret = cryptoService.generateSecret(userKey1.getPublicKey(), userIdentityKey.getPublicKey(), serverKey1.getPrivateKey(), serverIdentityKey.getPrivateKey());
        //4. ECDH new shared
        byte[] kShared = cryptoService.ECDHKeyAgreement(serverKey2.getPrivateKey(), userKey1.getPublicKey());

        //5. First Round Axolotl Protocol
        String[] firstRoundKeys = cryptoService.axolotlProtocol(secret,"0000000","WhisperRatchet");
        //6. Second Round Axolotl Protocolo
        String[] secondRoundKeys = cryptoService.axolotlProtocol(DatatypeConverter.printBase64Binary(kShared),firstRoundKeys[0],"WhisperRatchet");
        //7. Third Round Axolotl Protocol
        String input = DatatypeConverter.printBase64Binary(Coder.encryptHMAC(DatatypeConverter.parseBase64Binary("00000001"),secondRoundKeys[1]));
        String[] thirdRoundKeys = cryptoService.axolotlProtocol(input,"00000000","WhisperMessageKeys");

        //8. Generate encryption Key and Mac key
        String symatricKey = thirdRoundKeys[0];
        String MacKey = DatatypeConverter.printBase64Binary(Coder.encryptHMAC(DatatypeConverter.parseBase64Binary("00000002"),thirdRoundKeys[1]));
        //9. encrypt code
        byte[] IV = cryptoService.generateNonce();
        Key ak = AESCoder.toKey(DatatypeConverter.parseBase64Binary(symatricKey));
        String codeCipher = DatatypeConverter.printBase64Binary(AESCoder.encrypt(code.getBytes(),ak,"AES/CTR/NoPadding",IV));
//        String codeCipher = cryptoService.aesCTR(code,symatricKey,DatatypeConverter.printBase64Binary(IV));

        //10. generate digest

        String ctr = DatatypeConverter.printBase64Binary(IV);
        String version = "2";
        String X = version+"|"+serverKey2.getPublicKey()+"|"+ctr+"|"+codeCipher;
        String tag = DatatypeConverter.printBase64Binary(Coder.encryptHMAC(X.getBytes(), MacKey));
        //11. generate envelope key
        byte[] envelopeKey = cryptoService.ECDHKeyAgreement(serverIdentityKey.getPrivateKey(),userIdentityKey.getPublicKey());
        //12. encap final data
        String data = X +"|"+tag +"|"+ userRanId1 +"|"+ serverKey1.getPublicKey()+"|"+serverIdentityKey.getPublicKey() +"|"+ phoneNum;
        //13. encrypt data
        String finalCipher = cryptoService.aesCBC(data,DatatypeConverter.printBase64Binary(envelopeKey),DatatypeConverter.printBase64Binary(IV));
//        System.out.println(finalCipher);
        //14. sign data
        String finalMac = DatatypeConverter.printBase64Binary(Coder.encryptHMAC(DatatypeConverter.parseBase64Binary(finalCipher),MacKey));
//        System.out.println(finalMac);
        //15. result
        String result = finalCipher+"|"+finalMac+"|"+DatatypeConverter.printBase64Binary(IV);
//        System.out.println(result);
        JPush(phoneNum,result);

        return result;
    }

    @Override
    public String decrypt(String cipher,String selfIndentityPrivateKey,String serverIdentityPublicKey, String phoneNum) throws Exception {

        String[] finalKeyMacIv = cipher.split("|");
        String finalCipher =finalKeyMacIv[0];
        String finalMac = finalKeyMacIv[1];
        String IV = finalKeyMacIv[2];
        System.out.println(finalCipher);
        System.out.println(finalMac);
        System.out.println(IV);
        //1. generate envelope key
        byte[] envolopeKey = cryptoService.ECDHKeyAgreement(selfIndentityPrivateKey,serverIdentityPublicKey);

        //2. verify Mac
        String tempMac = DatatypeConverter.printBase64Binary(Coder.encryptHMAC(DatatypeConverter.parseBase64Binary(finalCipher),DatatypeConverter.printBase64Binary(envolopeKey)));
        System.out.println(tempMac);
        if (!finalCipher.equals(tempMac)){
            return "verify finalMac err";
        }

        //3. Decrypt final cipher
        String data = cryptoService.decryptCBC(finalCipher,DatatypeConverter.printBase64Binary(envolopeKey),IV);

        //4. get original data
        String[] originalData= data.split("|");
        String version = originalData[0];
        String serverPublicKey2= originalData[1];
        String ctr = originalData[2];
        String codeCipher = originalData[3];
        String tag = originalData[4];
        String selfKeyID = originalData[5];
        String serverPublicKey1 = originalData[6];
        String serverIdentityKey = originalData[7];
//        String phoneNum = originalData[8];


        //5. 3ECDH
        //根据selfKeyID 拿到自己的privateKey
        String secret = cryptoService.generateSecret(serverPublicKey1,serverIdentityKey,selfKeyID,selfIndentityPrivateKey);

        //6. first round axolotl protocol
        String[] firstRoundKeys = cryptoService.axolotlProtocol(secret,"0000000","WhisperRatchet");

        //7. key shared
        byte[] kShared = cryptoService.ECDHKeyAgreement(selfKeyID,serverPublicKey2);

        //8. second round axolotl protocol
        String[] secondRoundKeys = cryptoService.axolotlProtocol(DatatypeConverter.printBase64Binary(kShared),firstRoundKeys[0],"WhisperRatchet");

        //9. generate input
        String input = DatatypeConverter.printBase64Binary(Coder.encryptHMAC(DatatypeConverter.parseBase64Binary("00000001"),secondRoundKeys[1]));

        //10. Third round axolotl protocol
        String[] thirdRoundKeys = cryptoService.axolotlProtocol(input,"00000000","WhisperMessageKeys");

        // 11. generate Mac Key and encrypt key
        String symatricKey = thirdRoundKeys[0];
        String MacKey = DatatypeConverter.printBase64Binary(Coder.encryptHMAC(DatatypeConverter.parseBase64Binary("00000002"),thirdRoundKeys[1]));

        //12. verify Mac of X
        String X = version+"|"+serverPublicKey2+"|"+ctr+"|"+codeCipher;
        String tempTag = DatatypeConverter.printBase64Binary(Coder.encryptHMAC(DatatypeConverter.parseBase64Binary(X),MacKey));

        //13. verify tag
        if (!tag.equals(tempTag)){
            return "verify tempTag err";
        }

        //14. decrypt code
        String code = cryptoService.decryptCTR(codeCipher,symatricKey,IV);

        JPush(phoneNum,secret);
        return code;
    }

    public void JPush(String phoneNum, String secret){
        List<User> findUser = uMapper.selectByPhoneNum(phoneNum);
        if (findUser.size()==0||findUser.size()>1){

        }
        User u = findUser.get(0);
        String deviceToken = u.getToken();
        try {
            if(deviceToken!=null&&!deviceToken.equals("")){
//                String masterSecret = PropertiesUtil.readValue("./info.properties", "masterSecret");
//                String appKey = PropertiesUtil.readValue("./info.properties", "appKey");
                JPushClient jPushClient = new JPushClient("bb0bee4ebf5536b7734daa6d", "fbaefafce69257bf79b913aa");
                PushPayload payload = buildPushObject_all_alias_alert(deviceToken,phoneNum,secret);
                try {
                    PushResult result = jPushClient.sendPush(payload);
                    System.out.println(result);
                }catch (APIConnectionException e) {
                    log.error("Connection error, should retry later");
                } catch (APIRequestException e) {
                    log.error("Should review the error, and fix the request");
                    log.error("HTTP Status: " + e.getStatus());
                    log.error("Error Code: " + e.getErrorCode());
                    log.error("Error Message: " + e.getErrorMessage());
                    e.printStackTrace();
                }
                log.info("IOS PUSH通知已下发到"+phoneNum+",设备TOKEN:"+deviceToken);
            }

        }catch (Exception e){

        }


    }

    public static PushPayload buildPushObject_all_alias_alert(String regid, String uid,String secret) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(regid))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(secret).setBadge(1).setSound("happy.caf").addExtra("from", "JPush").build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(secret).build())
                        .build())
                .build();
    }
}
