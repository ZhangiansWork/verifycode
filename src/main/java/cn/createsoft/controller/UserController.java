package cn.createsoft.controller;

import cn.createsoft.model.User;
import cn.createsoft.service.UserKeyService;
import cn.createsoft.service.UserService;
import cn.createsoft.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService uService;

    @Autowired
    private UserKeyService ukService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String phoneNum, String password, Map<Integer,String> keys){
        if (StringUtil.isNotNullorEmpty(phoneNum)&&StringUtil.isNotNullorEmpty(password)){
            int res = uService.saveUser(phoneNum,password,keys);
            if (res ==-2){
                return "already exist";
            }
            if (res ==-1){
                return "save key err";
            }
            if (res == 0){
                return "save user err";
            }

                return "success";

        }
        return "param err";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(String phoneNum,String password){
        if (StringUtil.isNotNullorEmpty(phoneNum)&&StringUtil.isNotNullorEmpty(password)){
            User u =uService.selectUser(phoneNum,password);
            if (u !=null){
                return "success";
            }
            return "no user";
        }
        return "param err";
    }
}

