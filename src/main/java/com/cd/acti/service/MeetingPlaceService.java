package com.cd.acti.service;

import com.cd.acti.model.MeetingPlace;

import java.util.List;

public interface MeetingPlaceService {
    Integer delete(Integer id);
    Integer save(MeetingPlace meetingPlace);
    List<MeetingPlace> findAll();
}