package cn.createsoft.Map;

import cn.createsoft.service.CryptoService;
import cn.createsoft.util.saltlib.AESCoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring-core.xml"})
public class MapperTest {

//    @Resource
//    private UserMapper uMap;

    @Resource
    private CryptoService cryptoService;


//    public void test1(){
//        User u = uMap.selectById(1);
//        System.out.println(u);
//    }
@Test
    public void Getrandom(){
        String r1 = cryptoService.randomNum();
        String r2 = cryptoService.randomNum();
        String r3 = cryptoService.randomNum();
        String r4 = cryptoService.randomNum();
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(r4);
    }
    @Test
    public void splite(){
        String a = "abc|cba";
        String[] arr = a.split("\\|");
        for (int i = 0; i< arr.length;i++) {
            System.out.println(arr[i]);
        }
    }

    @Test
    public void change(){
        String s = "aaa\\|bbb\\|ccc";
        byte[] ss = DatatypeConverter.parseBase64Binary(s);
        System.out.println(Arrays.toString(ss));
        String a = DatatypeConverter.printBase64Binary(ss);
        System.out.println(a);

    }

    @Test
    public void enc(){
    }

}
