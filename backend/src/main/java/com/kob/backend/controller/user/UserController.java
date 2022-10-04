package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.service.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    //调用接口
    UserMapper userMapper;

    @GetMapping("/user/all")
    public List<User> getAll(){
        //查询所有用户
       return userMapper.selectList(null);
    }

  @GetMapping("/user/{userId}/")
    public User getUser(@PathVariable int userId){
        //复杂查询方法
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);//按照id进行查询
        return userMapper.selectOne(queryWrapper);
        //按照id查询的简单查询方法
        //return userMapper.selectById(userId);

    }


    /*
    //查询一个区间
    @GetMapping("/user/{userId}/")
    public List<User> getUser(@PathVariable int userId){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.ge("id",2).le("id",3);
        return userMapper.selectList(queryWrapper);
    }*/
    //插入一个值
    @GetMapping("/user/add/{userId}/{username}/{password}")
    public String addUser(@PathVariable int userId,@PathVariable String username,@PathVariable String password ) {
     //修改密码
        /*if(password.length()<6){
            System.out.println("密码太短");
        }*/
        PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
        String encoderPassword =passwordEncoder.encode(password);
        User user = new User(userId, username, encoderPassword);
        //在数据库中查润一行
        userMapper.insert(user);
        return "Add User successfully";
    }

    //删除一个操作
    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable int userId){
        userMapper.deleteById(userId);
        return "Delete User Successfully";
    }

}
