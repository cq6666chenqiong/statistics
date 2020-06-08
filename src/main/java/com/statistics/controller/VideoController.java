package com.statistics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/video")
public class VideoController {

    @RequestMapping(value = "/getVideo")
    public String getVideo(){
        return "/video/videohide";
    }

}
