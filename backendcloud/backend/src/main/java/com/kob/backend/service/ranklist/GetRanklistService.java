package com.kob.backend.service.ranklist;

import com.alibaba.fastjson.JSONObject;

public interface GetRanklistService {
    JSONObject getlist(Integer page);
}
