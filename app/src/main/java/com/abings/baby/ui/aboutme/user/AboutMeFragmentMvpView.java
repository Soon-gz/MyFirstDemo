package com.abings.baby.ui.aboutme.user;

import android.graphics.Color;
import android.widget.TextView;

import com.abings.baby.data.model.RelativePerson;
import com.abings.baby.data.model.TeacherDetail;
import com.abings.baby.data.model.UserDetail;
import com.abings.baby.ui.base.IBaseView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zwj on 2016/7/22.
 */
public interface AboutMeFragmentMvpView extends IBaseView {
    //没有选中的颜色
    int  unAbleEditColor = Color.parseColor("#e0e0e0");
    /**
     * 显示用户信息
     * @param userDetail 用户资料
     */
    void showUserDetail(UserDetail userDetail);

    /**
     * 显示关系人
     * @param resultBeans 关系人资料列表
     */
    void showUserRelative(List<RelativePerson.ResultBean> resultBeans);

    /**
     * 修改结果
     */
    void updateDetailResult();

    void setHeaderImageView(CircleImageView headerImageView);

    /**
     * Adapter使用的
     * @param delIndex
     */
    void deleteRelativeItem(int delIndex);

    /**
     * 更新删除成功后的界面
     * @param delIndex
     */
    void updateRelativeByDel(int delIndex);

    /**
     * 刷新添加用户
     */
    void refreshAddRelative();

    /**
     * 右上角按钮编辑-保存的变化
     * @param textView
     */
    void setEditOrSave(TextView textView);

    /**
     * 显示老师的详细信息
     * @param detail
     */
    void showTeacherDetail(TeacherDetail detail);

    /**
     *获得是否可编辑状态
     *@author Shuwen
     *created at 2016/8/3 10:36
     */
    boolean isEdit();
}
