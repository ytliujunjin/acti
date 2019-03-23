package com.cd.acti.controller;


import com.alibaba.fastjson.JSON;
import com.cd.acti.model.MeetingPlace;
import com.cd.acti.service.MeetingPlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meetingPlace")
@Slf4j
public class meetingPlaceController {
    @Autowired
    private MeetingPlaceService meetingPlaceService;

    @RequestMapping("/deleteMP")
    @Transactional
    public String deleteMP(HttpServletRequest request, Integer id) {
        Map<String, Object> returnData = new HashMap<>();
        int deleteResult = meetingPlaceService.delete(id);
        returnData.put("status", String.valueOf(deleteResult));
        returnData.put("data", "");
        returnData.put("token", request.getHeader("Authorization"));
        returnData.put("text", "");
        return JSON.toJSONString(returnData);
    }

    @RequestMapping("/saveMP")
    public String saveMP(HttpServletRequest request, String place) {
        Map<String, Object> returnData = new HashMap<>();
        Map<String, Object> returnDataBak = new HashMap<>();
        MeetingPlace meetingPlace = new MeetingPlace();
        meetingPlace.setPlace(place);
        int insertResult = meetingPlaceService.save(meetingPlace);
        returnData.put("status", String.valueOf(insertResult));
        returnDataBak.put("id", meetingPlace.getId());
        returnData.put("data", returnDataBak);
        returnData.put("token", request.getHeader("Authorization"));
        returnData.put("text", "");
        return JSON.toJSONString(returnData);
    }

    public String getMP(HttpServletRequest request){
        Map<String, Object> returnData = new HashMap<>();
        Map<String, Object> returnDataBak = new HashMap<>();
        List<MeetingPlace> apList = new ArrayList<>();
        apList=meetingPlaceService.findAll();
        int apListSize=apList.size();
        returnDataBak.put("total", apListSize);
        returnDataBak.put("rows", apList);

        returnData.put("status","1");
        returnData.put("data", returnDataBak);
        returnData.put("token", request.getHeader("Authorization"));
        returnData.put("text", "");
        return JSON.toJSONString(returnData);
    }
}
