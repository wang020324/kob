package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pk")
public class BotInfoController {
    @RequestMapping("getbotinfo/") //添加映射
    /*
    public String getInfo(){
        return "Hello World";
    }*/

    /*public List<String>getBotInfo(){
        List<String>list=new LinkedList<>();
        list.add("sword");
        list.add("tiger");
        list.add("apple");
        return list;
    }

     */
    //返回一个字典+嵌套链表

    public Map<String,String>getBotInfo(){
        Map<String,String>map=new HashMap<>();

        map.put("name","title");
        map.put("rating","1500");
        return map;
    }
    /*public List<Map<String,String>>getBotInfo(){
        List<Map<String,String>>list=new LinkedList<>();
        Map<String,String>bot1=new HashMap<>();
        bot1.put("name","tiger");
        bot1.put("rating","1500");
        Map<String,String>bot2=new HashMap<>();
        bot2.put("name","apple");
        bot2.put("rating","1800");
        list.add(bot1);
        list.add(bot2);
        return list;
    }

     */
}
