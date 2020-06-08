package com.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.statistics.service.statistics.MemScoreStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/statistics")
public class MemDetailScoreController {

    @Autowired
    private MemScoreStatisticsService memScoreStatisticsService;

    @RequestMapping(value = "/getMemberDetailScore",produces="text/html;charset=utf-8")
    @ResponseBody
    public String getMemberDetailScore(HttpServletRequest request, HttpServletResponse response){
        String nickname = request.getParameter("nickname");
        Map queryMap = new HashMap();
        queryMap.put("nickname",nickname);
        System.out.println(nickname);
        List<Map<String,String>> list = memScoreStatisticsService.getMemberDetailScore(queryMap);
        System.out.println(JSON.toJSONString(list));

        return JSON.toJSONString(list);
    }
}
