package com.abings.baby.ui.waterfall;

import com.abings.baby.data.model.WaterFallGridItem;
import com.abings.baby.ui.base.IBaseView;

import java.util.List;


public interface WaterfallMvpView extends IBaseView {
    void setImgS(List<WaterFallGridItem> list);
}
