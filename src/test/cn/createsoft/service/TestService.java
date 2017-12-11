package cn.createsoft.service;


import cn.createsoft.map.ServerKeyMapper;
import cn.createsoft.map.UserKeyMapper;
import cn.createsoft.model.ServerKey;
import cn.createsoft.model.UserKey;
import cn.createsoft.util.saltlib.Box;
import cn.createsoft.util.saltlib.Coder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.security.ec.ECDHKeyAgreement;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring-web.xml",
        "classpath:spring-core.xml"})
public class TestService {

    @Resource
    private CryptoService cryptoService;

    @Resource
    private ServerKeyMapper skMapper;

    @Resource
    private UserKeyMapper ukMapper;
    @Test
    public void RandomNum(){
        java.lang.String r = cryptoService.randomNum();
        System.out.println(r);
    }

    @Test
    public void SecureRandom() throws UnsupportedEncodingException {
        String slat = DatatypeConverter.printBase64Binary(cryptoService.generateNonce());

        System.out.println(slat);
    }
    @Test
    public void coderAndDataConverter() throws Exception {
        byte[] b = cryptoService.generateNonce();
        System.out.println(Coder.encryptBASE64(b));
        System.out.println(DatatypeConverter.printBase64Binary(b));
    }

    @Test
    public void GenerateKeyPairs(){
        for (int i = 0 ; i <50; i++) {
            Box.KeyPair keys = cryptoService.generateKeyPair();
            ServerKey sk = new ServerKey();
            sk.setPublicKey(DatatypeConverter.printBase64Binary(keys.pubKey));
            sk.setPrivateKey(DatatypeConverter.printBase64Binary(keys.privKey));
            skMapper.insert(sk);
        }
    }

    @Test
    public void generateUserKeys(){
        for (int i = 0; i <50 ; i++){
            Box.KeyPair keys = cryptoService.generateKeyPair();
            UserKey uk = new UserKey();
            uk.setKeyId(i);
            uk.setPhoneNum("120");
            uk.setPublicKey(DatatypeConverter.printBase64Binary(keys.pubKey));
            ukMapper.insert(uk);
        }
    }

    @Test
    public void verify() throws Exception {
        byte[] eveKey = cryptoService.ECDHKeyAgreement("vbe6g1cM+SwXnJua8CHtrZndj6bQU2a0Ab7wDhpRj38=","DjUL+W4rdyQ9DODUWCMOFjd8/EOj481EY8hkcY65GC4=");
        System.out.println(Coder.encryptBASE64(eveKey));
        System.out.println(DatatypeConverter.printBase64Binary(eveKey));
    }

    @Test
    public void decryptCBC() {
        try {
            String rawData  = "raw456456345634563456345634563456Data";
            String cipher = cryptoService.aesCBC(rawData,"P6MXliNBbMrbXHd+q3p/ORltzvWXSQld9U3zqXuPBJw=","VmFyrMdlKf3UdinB5IJgOw==");
            String raw = cryptoService.decryptCBC(cipher,"P6MXliNBbMrbXHd+q3p/ORltzvWXSQld9U3zqXuPBJw=","VmFyrMdlKf3UdinB5IJgOw==");
            System.out.println(raw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
