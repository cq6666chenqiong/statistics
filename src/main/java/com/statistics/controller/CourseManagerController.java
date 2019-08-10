package com.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.statistics.service.statistics.CourseMgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/courseMg")
public class CourseManagerController {

    @Autowired
    private CourseMgService courseMgService;

    @RequestMapping(value = "/delCourseMember")
    @ResponseBody
    public String delCourseMember(HttpServletRequest request, HttpServletResponse response){
        String courseId = request.getParameter("courseId");
        Map<String,String> result = new HashMap<String,String>();
        result.put("code","0");
        try{
            courseMgService.delCourseMemberByCourseId(courseId);
            courseMgService.delUserScoreByCourseId(courseId);

           // courseMgService.delUserStatisticsScoreByCourseId(courseId);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code","1");
            return JSON.toJSONString(result);
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/delAllCourseMember")
    @ResponseBody
    public String delAllCourseMember(HttpServletRequest request, HttpServletResponse response){
        String courseId = request.getParameter("courseId");
        String id = request.getParameter("ids");
        String[] ids = id.split(",");
        Map<String,String> result = new HashMap<String,String>();
        result.put("code","0");
        try{

            for(int i=0;i<ids.length;i++){

                String cid = courseId;
                String userId = ids[i];
                if(userId.equals("on")) continue;
                Map map = new HashMap();
                map.put("courseId",cid);
                map.put("userId",userId);
                courseMgService.delCourseMember(map);
                courseMgService.delUserScore(map);
            }
            // courseMgService.delUserStatisticsScoreByCourseId(courseId);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code","1");
            return JSON.toJSONString(result);
        }
        return JSON.toJSONString(result);
    }
}
