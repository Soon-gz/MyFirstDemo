package com.abings.baby.ui.aboutme.association;

import com.abings.baby.data.model.RelativePerson;
import com.abings.baby.ui.base.IBaseView;

/**
 * Created by zwj on 2016/7/26.
 */
public interface AssociationMvpView extends IBaseView {

    void addSuccess(RelativePerson.ResultBean resultBean);
}
