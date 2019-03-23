package com.cd.acti.service.impl;

import com.cd.acti.dao.FormDataMapper;
import com.cd.acti.model.FormDataWithBLOBs;
import com.cd.acti.service.FormDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormDataServiceImpl implements FormDataService {
    @Autowired
    private FormDataMapper dao;

    @Override
    public FormDataWithBLOBs findByFormKey(String formKey) {
        return dao.selectByFormKey(formKey);
    }
}
