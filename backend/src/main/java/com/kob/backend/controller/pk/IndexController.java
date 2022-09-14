package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pk/")
public class IndexController {
    //引入链接index的html页面
    @RequestMapping("index/")
    public String index(){
        return "pk/index.html"; //index目录的路径
    }
}
