package com.statistics.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;



@Controller
@RequestMapping(value = "/test")
public class TestController {

    /**
     * 商汤心跳接口
     *
     *
     *
     */
    @RequestMapping(value = "/heartbeat", method = RequestMethod.POST)
    @ResponseBody
    public String Shangheartbeat(/*@RequestParam(value = "device_id",required=true) String device_id,
			@RequestParam(value = "timestamp",required=true) String timestamp,
			@RequestParam(value = "version",required=true) String version,
			@RequestParam(value = "sign",required=true) String sign,*/
            HttpServletRequest req,
            HttpServletResponse res) {
        TimeUnit unit = TimeUnit.SECONDS;
        System.out.println("Shangheartbeat"+"========device_id========="+req.getParameter("device_id"));
        System.out.println("Shangheartbeat"+"========timestamp========="+req.getParameter("timestamp"));
        System.out.println("Shangheartbeat"+"========version========="+req.getParameter("version"));
        System.out.println("Shangheartbeat"+"========sign========="+req.getParameter("sign"));
        //redisTemplate.re
        //redisTemplate.opsForValue().set("a", "sssss");
        //redisCacheService.jedisService().set("a", "sssss");
		/*ServiceResult<?> serviceResult = ServiceResult.success(new HashMap());
		//bizLogger.info("用户登录信息 " + userInfoMap);
		return serviceResult;*/
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("message", "success");
        JSONObject data = new JSONObject();
        data.put("version", "1.1.0");
        data.put("timestamp", new Date().getTime());
        json.put("data", data);

        return JSON.toJSONString(json);
    }

    /**
     * 商汤心跳认证
     *
     *
     *
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public String auth(/*@RequestParam(value = "device_id",required=true) String device_id,
			@RequestParam(value = "timestamp",required=true) String timestamp,
			@RequestParam(value = "version",required=true) String version,
			@RequestParam(value = "sign",required=true) String sign,*/
            HttpServletRequest req,
            HttpServletResponse res) {
        TimeUnit unit = TimeUnit.SECONDS;
        System.out.println("auth");
        //redisTemplate.re
        //redisTemplate.opsForValue().set("a", "sssss");
        //redisCacheService.jedisService().set("a", "sssss");
		/*ServiceResult<?> serviceResult = ServiceResult.success(new HashMap());
		//bizLogger.info("用户登录信息 " + userInfoMap);
		return serviceResult;*/
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("message", "success");
        JSONObject data = new JSONObject();
        data.put("version", "1.1.0");
        data.put("timestamp", new Date().getTime());
        data.put("token", "222222222222222222222222222222222222222222222222222");
        json.put("data", data);

        return JSON.toJSONString(json);
    }


    /**
     * 商汤心跳认证
     *
     *
     *
     */
    @RequestMapping(value = "/upload_data_full", method = RequestMethod.POST)
    @ResponseBody
    public String upload_data_full(
            HttpServletRequest req,
            HttpServletResponse res) {
        TimeUnit unit = TimeUnit.SECONDS;
        String name = req.getParameter("name");
        String sex = req.getParameter("sex");
        System.out.println("upload_data_full"+"=======" + name + "========" + sex);
        //redisTemplate.re
        //redisTemplate.opsForValue().set("a", "sssss");
        //redisCacheService.jedisService().set("a", "sssss");
		/*ServiceResult<?> serviceResult = ServiceResult.success(new HashMap());
		//bizLogger.info("用户登录信息 " + userInfoMap);
		return serviceResult;*/
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("message", "upload successful");
		/*JSONObject data = new JSONObject();
		data.put("version", "1.1.0");
		data.put("timestamp", new Date().getTime());
		data.put("token", "111111111111111111111111111111111111111111");
		json.put("data", data);*/

        return JSON.toJSONString(json);
    }


    @RequestMapping(value = "/snap", method = RequestMethod.POST)
    @ResponseBody
    public String snap(/*@RequestParam(value = "device_id",required=true) String device_id,
			@RequestParam(value = "timestamp",required=true) String timestamp,
			@RequestParam(value = "version",required=true) String version,
			@RequestParam(value = "sign",required=true) String sign,*/
            HttpServletRequest req,
            HttpServletResponse res) {
        TimeUnit unit = TimeUnit.SECONDS;
        System.out.println("snap");
        //redisTemplate.re
        //redisTemplate.opsForValue().set("a", "sssss");
        //redisCacheService.jedisService().set("a", "sssss");
		/*ServiceResult<?> serviceResult = ServiceResult.success(new HashMap());
		//bizLogger.info("用户登录信息 " + userInfoMap);
		return serviceResult;*/
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("message", "upload successful");
		/*JSONObject data = new JSONObject();
		data.put("version", "1.1.0");
		data.put("timestamp", new Date().getTime());
		data.put("token", "111111111111111111111111111111111111111111");
		json.put("data", data);*/

        return JSON.toJSONString(json);
    }


    @RequestMapping(value = "/test")
    public String test(
            HttpServletRequest req,
            HttpServletResponse res,
            Model model) {
        model.addAttribute("message", "HelloWorld");
        return "upload1";
    }
}
