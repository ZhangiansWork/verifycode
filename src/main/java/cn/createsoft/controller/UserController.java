package cn.createsoft.controller;

import cn.createsoft.model.User;
import cn.createsoft.service.UserKeyService;
import cn.createsoft.service.UserService;
import cn.createsoft.util.StringUtil;
import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.function.BiConsumer;

@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController{

    @Autowired
    private UserService uService;

    @Autowired
    private UserKeyService ukService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object register(String phoneNum, String password, String keys,String token) {
        System.out.println("toekn"+token);
        if (StringUtil.isNotNullorEmpty(phoneNum)&&StringUtil.isNotNullorEmpty(password)){
            JSONObject key = JSONObject.parseObject(keys);
            Gson go  = new Gson();
            Map<Integer,String> keyMap= go.fromJson(keys,new TypeToken<Map<Integer,String>>(){}.getType());
            int res = uService.saveUser(phoneNum,password,keyMap, token);
            if (res ==-2){

                return "already exist";
            }
            if (res ==-1){
                return "save key err";
            }
            if (res == 0){
                return "save user err";
            }

            return "{\"status\":\"success\"}";

        }
        return "param err";
    }

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(String phoneNum,String password, String token){
        if (StringUtil.isNotNullorEmpty(phoneNum)&&StringUtil.isNotNullorEmpty(password)){
            User u =uService.selectUser(phoneNum,password);
            if (u !=null){
                u.setToken(token);
                uService.updateToken(u);
                return "success";
            }
            return "no user";
        }
        return "param err";
    }
}

