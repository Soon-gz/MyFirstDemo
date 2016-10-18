package com.abings.baby.ui.contacts;

import com.abings.baby.data.model.Contact;
import com.abings.baby.ui.base.IBaseView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public interface ContactsMvpView extends IBaseView {
    void showSchoolContacts(List<Contact> schoolContacts);
}
