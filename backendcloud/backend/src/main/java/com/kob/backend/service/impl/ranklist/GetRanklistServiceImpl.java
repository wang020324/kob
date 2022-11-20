package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRanklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRanklistServiceImpl implements GetRanklistService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getlist(Integer page) {
        //查询所有的用户
        IPage<User> userIPage =new Page<>(page,5);
        QueryWrapper<User>queryWrapper =new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users =userMapper.selectPage(userIPage,queryWrapper).getRecords();
        //返回数据，在返回的时候需要返回一个总数，用来帮助分页
        JSONObject resp=new JSONObject();
        for(User user : users)
            user.setPassword("");//为了防止密码泄露予以清空
        resp.put("users",users);
        resp.put("users_count",userMapper.selectCount(null));

        return resp;
    }
}
