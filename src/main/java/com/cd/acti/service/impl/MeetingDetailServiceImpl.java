package com.cd.acti.service.impl;

import com.cd.acti.dao.MeetingDetailMapper;
import com.cd.acti.model.MeetingDetail;
import com.cd.acti.service.MeetingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingDetailServiceImpl implements MeetingDetailService {
    @Autowired
    private MeetingDetailMapper dao;

    @Override
    public Integer delete(Integer id){
        return dao.deleteByPrimaryKey(id);
    }

    @Override
    public Integer save(MeetingDetail meetingDetail){
        return dao.insert(meetingDetail);
    }

    @Override
    public Integer update(MeetingDetail meetingDetail){
        return dao.updateByPrimaryKeySelective(meetingDetail);
    }

    @Override
    public List<MeetingDetail> findByInitiator(String Initiator,String participant, int page, int rows){
        MeetingDetail meetingDetail=new MeetingDetail();
        meetingDetail.setInitiator(Initiator);
        meetingDetail.setParticipant(participant);
        meetingDetail.setPage(page);
        meetingDetail.setRows(rows);
        return dao.selectByInitiator(meetingDetail);
    }

    @Override
    public List<MeetingDetail> findAll(int page, int rows){
        MeetingDetail meetingDetail=new MeetingDetail();
        meetingDetail.setPage(page);
        meetingDetail.setRows(rows);
        return dao.selectAll(meetingDetail);
    }
}
