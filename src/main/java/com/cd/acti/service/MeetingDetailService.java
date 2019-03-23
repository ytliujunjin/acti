package com.cd.acti.service;

import com.cd.acti.model.MeetingDetail;

import java.util.List;

public interface MeetingDetailService {
    Integer delete(Integer id);
    Integer save(MeetingDetail meetingDetail);
    Integer update(MeetingDetail meetingDetail);
    List<MeetingDetail> findByInitiator(String Initiator, String participant,int page, int rows);
    List<MeetingDetail> findAll(int page, int rows);
}