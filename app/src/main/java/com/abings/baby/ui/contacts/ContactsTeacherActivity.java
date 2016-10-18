package com.abings.baby.ui.contacts;

import android.os.Bundle;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.data.model.Contact;
import com.abings.baby.ui.main.ListPopup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zwj on 16/9/1.
 * 教师端联系人
 */
public class ContactsTeacherActivity extends ContactsActivity {

    @Bind(R.id.title_center)
    TextView tvTitleCenter;



    enum contactConst {
        classContact("本班通讯录"), schoolContact("全校通讯录");

        private String name;

        contactConst(String str) {
            this.name = str;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        super.initViewsAndEvents(savedInstanceState);
        tvTitleCenter.setText(contactConst.classContact.getName());
    }

    @OnClick(R.id.title_center)
    public void onClick() {
        showPopup();
    }


    private void showPopup() {
        ListPopup.Builder builder = new ListPopup.Builder(this);
        builder.addItem(0, contactConst.classContact.getName());
        builder.addItem(1, contactConst.schoolContact.getName());

        final ListPopup mListPopup = builder.build();

        mListPopup.setOnListPopupItemClickListener(new ListPopup.OnListPopupItemClickListener() {
            @Override
            public void onItemClick(int what) {

                mListPopup.dismiss();

                String title;
                if (what == 0) {
                    //本班
                    if (contactConst.classContact.getName().equals(tvTitleCenter.getText().toString().trim())) {
                        return;
                    }
                    title = contactConst.classContact.getName();
                    mDatas.clear();
                    mDatas.addAll(mClassContacts);
                    mAdapter.setDatas(mDatas);
                } else {
                    //全校
                    if (contactConst.schoolContact.getName().equals(tvTitleCenter.getText().toString().trim())) {
                        return;
                    }
                    title = contactConst.schoolContact.getName();
                    mDatas.clear();
                    mDatas.addAll(mSchoolContacts);
                    mAdapter.setDatas(mDatas);
                }
                tvTitleCenter.setText(title);
            }
        });
        mListPopup.showPopupWindow(tvTitleCenter);
    }
}
