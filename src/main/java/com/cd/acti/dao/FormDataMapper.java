package com.cd.acti.dao;

import com.cd.acti.model.FormData;
import com.cd.acti.model.FormDataWithBLOBs;

public interface FormDataMapper {
    int deleteByPrimaryKey(String id);

    int insert(FormDataWithBLOBs record);

    int insertSelective(FormDataWithBLOBs record);

    FormDataWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FormDataWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(FormDataWithBLOBs record);

    int updateByPrimaryKey(FormData record);

    FormDataWithBLOBs selectByFormKey(String modelKey);
}