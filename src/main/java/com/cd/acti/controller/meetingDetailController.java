package com.cd.acti.controller;

import com.alibaba.fastjson.JSON;
import com.cd.acti.model.MeetingDetail;
import com.cd.acti.service.MeetingDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/meetingDetail")
@Slf4j
public class meetingDetailController {
    @Autowired
    private MeetingDetailService meetingDetailService;

    @RequestMapping("/deleteMD")
    @Transactional
    public String deleteMD(HttpServletRequest request, Integer id){
        Map<String, Object> returnData = new HashMap<>();
        int deleteResult = meetingDetailService.delete(id);
        returnData.put("status",String.valueOf(deleteResult));
        returnData.put("data", "");
        returnData.put("token", request.getHeader("Authorization"));
        returnData.put("text", "");
        return JSON.toJSONString(returnData);
    }

    @RequestMapping("/saveMD")
    public String saveMD(HttpServletRequest request,Integer id ,String meeting_title,
                                      String meeting_content,String initiator,String participant,String place){
        Map<String, Object> returnData = new HashMap<>();
        Map<String, Object> returnDataBak = new HashMap<>();
        MeetingDetail meetingDetail=new MeetingDetail();
        if (id==null){
            meetingDetail.setMeetingTitle(meeting_title);
            meetingDetail.setMeetingContent(meeting_content);
            meetingDetail.setInitiator(initiator);
            meetingDetail.setParticipant(participant);
            meetingDetail.setTime(new Date());
            meetingDetail.setPlace(place);
            int insertResult=meetingDetailService.save(meetingDetail);
            returnData.put("status",String.valueOf(insertResult));
            returnDataBak.put("id",meetingDetail.getId());
            returnData.put("data", returnDataBak);
            returnData.put("token", request.getHeader("Authorization"));
            returnData.put("text", "");
            return JSON.toJSONString(returnData);
        }else{
            meetingDetail.setId(id);
            meetingDetail.setMeetingTitle(meeting_title);
            meetingDetail.setMeetingContent(meeting_content);
            meetingDetail.setInitiator(initiator);
            meetingDetail.setParticipant(participant);
            meetingDetail.setTime(new Date());
            meetingDetail.setPlace(place);
            int updateResult=meetingDetailService.update(meetingDetail);
            returnData.put("status",String.valueOf(updateResult));
            returnData.put("data", "");
            returnData.put("token", request.getHeader("Authorization"));
            returnData.put("text", "");
            return JSON.toJSONString(returnData);
        }
    }

    @RequestMapping("/getMDByPerson")
    public String getMDByPerson(HttpServletRequest request,String Initiator,String participant,int page, int rows){
        Map<String, Object> returnData = new HashMap<>();
        Map<String, Object> returnDataBak = new HashMap<>();
        List<MeetingDetail> apList = new ArrayList<>();
        if (Initiator==null && participant==null){
            apList=meetingDetailService.findAll((page-1)*rows,rows);
        }else{
            apList=meetingDetailService.findByInitiator(Initiator,participant,(page-1)*rows,rows);
        }
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
