package cn.createsoft.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}
