package com.cd.acti.service.impl;

import com.cd.acti.dao.MeetingPlaceMapper;
import com.cd.acti.model.MeetingPlace;
import com.cd.acti.service.MeetingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingPlaceServiceImpl implements MeetingPlaceService {
    @Autowired
    private MeetingPlaceMapper dao;

    @Override
    public Integer delete(Integer id){
        return dao.deleteByPrimaryKey(id);
    }

    @Override
    public Integer save(MeetingPlace meetingPlace){
        return dao.insertSelective(meetingPlace);
    }

    @Override
    public List<MeetingPlace> findAll(){
        return dao.selectAll();
    }
}
