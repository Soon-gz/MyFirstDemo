package com.abings.baby.utils;

import com.abings.baby.data.model.Contact;

import java.util.Comparator;

public class PinyinComparator implements Comparator {

    @Override
    public int compare(Object arg0, Object arg1) {
        // 按照名字排序
        Contact contact0 = (Contact) arg0;
        Contact contact1 = (Contact) arg1;
        String catalog0 = "";
        String catalog1 = "";

        if (contact0 != null && contact0.getName() != null && contact0.getName().length() > 1)
            catalog0 = PingYinUtil.converterToFirstSpell(contact0.getName()).substring(0, 1);

        if (contact1 != null && contact1.getName() != null && contact1.getName().length() > 1)
            catalog1 = PingYinUtil.converterToFirstSpell(contact1.getName()).substring(0, 1);
        int flag = catalog0.compareTo(catalog1);
        return flag;

    }

}
