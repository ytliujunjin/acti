package com.cd.acti.service;

import com.cd.acti.model.FormDataWithBLOBs;

public interface FormDataService {
    FormDataWithBLOBs findByFormKey(String formKey);
}
