package com.abings.baby.ui.message.send;

import com.abings.baby.data.model.ClassTeacherItem;
import com.abings.baby.ui.base.IBaseView;

import java.util.List;


public interface SendMsgMvpView extends IBaseView {

    void showDialog(String msg);

    void dialogDismiss();

//    void uploadCompleted();

//    void uploadImg(String pk_message_id);

    void onTokenError();

    void finishActivity();

    void showSchoolContacts(List<ClassTeacherItem> teacherItems);
}
