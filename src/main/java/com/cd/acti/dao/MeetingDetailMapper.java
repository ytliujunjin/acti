package com.cd.acti.dao;

import com.cd.acti.model.MeetingDetail;

import java.util.List;

public interface MeetingDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MeetingDetail record);

    int insertSelective(MeetingDetail record);

    MeetingDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MeetingDetail record);

    int updateByPrimaryKey(MeetingDetail record);

    List<MeetingDetail> selectByInitiator(MeetingDetail record);

    List<MeetingDetail> selectAll(MeetingDetail record);
}