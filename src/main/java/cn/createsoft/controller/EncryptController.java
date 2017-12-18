package cn.createsoft.controller;

import cn.createsoft.service.EncryptService;
import cn.createsoft.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api")
public class EncryptController {

    @Autowired
    private EncryptService encService;


    @RequestMapping(value = "/enc", method = RequestMethod.POST)
    @ResponseBody
    public String EncrpytionApi(String phoneNum, String code) throws Exception {
        if (StringUtil.isNotNullorEmpty(phoneNum)&&StringUtil.isNotNullorEmpty(code)){
            String res = encService.encrypt(phoneNum,code);

            return "{\"res\":\""+res+"\"}";// push res
        }


        return "param err";
    }
}
