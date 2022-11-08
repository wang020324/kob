package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/pk/")
public class IndexController {
    //引入链接index的html页面
    //@RequestMapping("index/") //默认将上面的与下面的拼接起来
    @RequestMapping("/")
    public String index(){
        return "pk/index.html"; //index目录的路径
    }
}
