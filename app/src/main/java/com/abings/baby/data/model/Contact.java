package com.abings.baby.data.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/1.
 */
public class Contact implements Comparable<Contact>, Serializable {
    private String pinyin;
    private char firstChar;

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(boolean isTeacher) {
        this.isTeacher = isTeacher;
    }

    private boolean isTeacher = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public ClassTeacherItem getClassTeacherItem() {
        return classTeacherItem;
    }

    public void setClassTeacherItem(ClassTeacherItem classTeacherItem) {
        this.classTeacherItem = classTeacherItem;
    }

    private ClassTeacherItem classTeacherItem;

    public UserContact getUserContact() {
        return userContact;
    }

    public void setUserContact(UserContact userContact) {
        this.userContact = userContact;
    }

    private UserContact userContact;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
        String first = pinyin.substring(0, 1);
        if (first.matches("[A-Za-z]")) {
            firstChar = first.toUpperCase().charAt(0);
        } else {
            firstChar = '#';
        }
    }

    public char getFirstChar() {
        return firstChar;
    }

    @Override
    public int compareTo(Contact another) {
        return this.pinyin.compareTo(another.getPinyin());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Contact) {
            if (((Contact) o).isTeacher) {
                return this.getName() == ((Contact) o).getName();
            } else {
                return this.getUserContact().getPk_baby_id() == ((Contact) o).getUserContact().getPk_baby_id();
            }

        } else {
            return super.equals(o);
        }
    }
}