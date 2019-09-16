package com.statistics.controller;

import com.alibaba.fastjson.JSON;
import com.statistics.service.statistics.CourseMgService;
import com.statistics.util.StringUtil;
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

    @RequestMapping(value = "/delCourse")
    @ResponseBody
    public String delCourse(HttpServletRequest request, HttpServletResponse response){
        String courseId = request.getParameter("courseId");
        Map<String,String> result = new HashMap<String,String>();
        result.put("code","0");
        try{
            courseMgService.delCourseById(courseId);


            courseMgService.delCourseChapterByCourseId(courseId);


            courseMgService.delCourseDraftByCourseId(courseId);

            courseMgService.delCourseExpriyByCourseId(courseId);

            courseMgService.delCourseFavoriteByCourseId(courseId);

            courseMgService.delCourseLessonByCourseId(courseId);

            courseMgService.delCourseLessonExtendByCourseId(courseId);

            courseMgService.delCourseLessonLearnByCourseId(courseId);

            courseMgService.delCourseLessonReplayByCourseId(courseId);

            courseMgService.delCourseLessonViewByCourseId(courseId);

            courseMgService.delCourseMaterialByCourseId(courseId);

            courseMgService.delCourseNoteByCourseId(courseId);

            //courseMgService.delCourseNoteLikeByCourseId(courseId);

            courseMgService.delCourseReviewByCourseId(courseId);

            courseMgService.delCourseThreadByCourseId(courseId);

            String target = "course-"+courseId;
            Map testMap = courseMgService.getTestPaperByTarget(target);
            if(testMap != null){
                Long testId = Long.valueOf(StringUtil.getString(testMap.get("id")));

                courseMgService.delTestPaperById(testId);

                courseMgService.delTestPaperItemByTestId(testId);

                courseMgService.delTestPaperItemResultByTestId(testId);

                courseMgService.delTestPaperResultByTestId(testId);

                courseMgService.delCourseMemberByCourseId(courseId);
                courseMgService.delUserScoreByCourseId(courseId);
            }


            // courseMgService.delUserStatisticsScoreByCourseId(courseId);
            System.out.println("cccccccc"+courseId);
        }catch(Exception e){
            e.printStackTrace();
            result.put("code","1");
            return JSON.toJSONString(result);
        }
        return JSON.toJSONString(result);
    }

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
