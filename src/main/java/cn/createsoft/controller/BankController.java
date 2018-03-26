package cn.createsoft.controller;

import cn.createsoft.model.BankUser;
import cn.createsoft.service.BankService;
import cn.createsoft.util.HttpRequest;
import cn.createsoft.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("bank")
public class BankController {

    @Autowired
    private BankService bService;

    private static ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();

    /**
     * @description 银行用户注册
     * @Param userName 用户名
     * */
    @RequestMapping("/signUp")
    @ResponseBody
    public String signUp(String userName){
        if (StringUtil.isNotNullorEmpty(userName)){
            String res = bService.signup(userName);
            return "{\"res\":\""+res+"\"}";
        }
        return "{\"res\":\""+"param err"+"\"}";
    }


    /**
     * @description 发送验证码
     * @Param userName 用户名
     * */
    @RequestMapping("/send")
    @ResponseBody
    public String sendCode(String userName){
        if (!StringUtil.isNotNullorEmpty(userName)){
            return "{\"res\":\""+"param err"+"\"}";
        }
        BankUser bu = bService.select(userName);
        if (bu==null){
            return "{\"res\":\""+"no user"+"\"}";
        }
        String  code = String.valueOf(getRandNum(1,999999));

        //Todo http
        String res = HttpRequest.sendPost("http://localhost:8080/api/enc","phoneNum="+userName+"&code="+code);
        if (res.contains("err")){
            return "{\"res\":\""+"http err"+"\"}";
        }
        System.out.println("send"+code);
        map.put(code,userName);
        return "{\"res\":\""+"ok"+"\"}";
    }

    /**
     * @description 验证验证码
     * @Param userName 用户账号
     * @Param code 验证码
     * */
    @RequestMapping("/verify")
    @ResponseBody
    public String verify(String userName,String code){
        if (StringUtil.isNotNullorEmpty(userName)&&StringUtil.isNotNullorEmpty(code)){
            if (map.containsKey(code)){
                String uName = map.get(code);
                if (uName.equals(userName)){
                    return "{\"res\":\""+"ok"+"\"}";
                }
                return "{\"res\":\""+"用户名验证码不陪陪"+"\"}";
            }
            return "{\"res\":\""+"验证码不存在"+"\"}";
        }
        return "{\"res\":\""+"param err"+"\"}";
    }

    public static int getRandNum(int min, int max) {
        int randNum = min + (int)(Math.random() * ((max - min) + 1));
        return randNum;
    }
}
