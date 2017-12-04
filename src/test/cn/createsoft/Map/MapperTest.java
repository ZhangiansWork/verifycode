package cn.createsoft.Map;

import cn.createsoft.map.UserMapper;
import cn.createsoft.model.User;
import cn.createsoft.service.CryptoService;
import cn.createsoft.util.saltlib.Box;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;

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
        String r = cryptoService.randomNum();
        System.out.println(r);
    }
    @Test
    public void splite(){
        String a = "abc|cba";
        String[] arr = a.split("\\|");
        for (int i = 0; i< arr.length;i++) {
            System.out.println(arr[i]);
        }
    }


}
