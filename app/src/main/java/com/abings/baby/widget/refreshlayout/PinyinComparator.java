package com.abings.baby.widget.refreshlayout;

import com.abings.baby.data.model.IndexModel;

import java.util.Comparator;

/**
 * 作者：黄斌 on 2016/2/21 14:19
 * 说明：
 */
public class PinyinComparator implements Comparator<IndexModel> {

    public int compare(IndexModel o1, IndexModel o2) {
        if (o1.topc.equals("@") || o2.topc.equals("#")) {
            return -1;
        } else if (o1.topc.equals("#") || o2.topc.equals("@")) {
            return 1;
        } else {
            return o1.topc.compareTo(o2.topc);
        }
    }

}