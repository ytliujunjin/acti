package com.cd.acti.dao;

import com.cd.acti.model.MeetingPlace;

import java.util.List;

public interface MeetingPlaceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MeetingPlace record);

    int insertSelective(MeetingPlace record);

    MeetingPlace selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MeetingPlace record);

    int updateByPrimaryKey(MeetingPlace record);

    List<MeetingPlace> selectAll();
}