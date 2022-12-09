package com.kob.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    //因为需要使用数据库，因此注入recordMapper
    @Autowired
    private RecordMapper recordMapper;
   //注入User表的查询操作
    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer page) {
        IPage<Record> recordIPage =new Page<>(page,10);
        //定义Wrapper用来排序
        QueryWrapper<Record> queryWrapper=new QueryWrapper<>();
        //按照时间降序排序
        queryWrapper.orderByDesc("id");
        //最后将查询到的结果存储到List中
        List<Record> records =recordMapper.selectPage(recordIPage,queryWrapper).getRecords();



        //将信息返回给前端
        JSONObject resp=new JSONObject();

        //存储用户双方头像和用户名+对战信息
        List<JSONObject> items =new LinkedList<>();
        //枚举每一局的对战记录
        for (Record record:records){
            //先去取出用户的Id
            User userA=userMapper.selectById(record.getAId());
            User userB=userMapper.selectById(record.getBId());
           //存储用户A和用户B的相关信息
            JSONObject item =new JSONObject();
            item.put("a_photo",userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo",userB.getPhoto());
            item.put("b_username",userB.getUsername());
            //判断输赢结果，将结果返回给前端
            String result="平局";
            //判断败者是谁
            if("A".equals(record.getLoser()))
                result="B获胜";
            else if("B".equals(record.getLoser())){
                result="A获胜";
            }
            item.put("result",result);
            item.put("record",record);
            items.add(item);
        }
        //存进JSON的records里
        resp.put("records",items);
        //传一下当前记录的总数
        resp.put("record_count",recordMapper.selectCount(null));//传空则是无条件返回总数
        return resp;
    }
}
