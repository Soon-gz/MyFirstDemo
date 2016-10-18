package com.abings.baby.data.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2014-12-23.
 */
public class ClassItem implements Serializable {

    /**
     * fk_teacher_id : 1
     * pk_grade_class_id : 2
     * class_name : 大一班
     * grade_name : 大班
     * fk_school_id : 2
     */

    private String fk_teacher_id;
    private String pk_grade_class_id;
    private String class_name;
    private String grade_name;
    private String fk_school_id;

    public String toString()
    {
        return getGrade_name()+" - "+getClass_name();
    }

    public String getFk_teacher_id() {
        return fk_teacher_id;
    }

    public void setFk_teacher_id(String fk_teacher_id) {
        this.fk_teacher_id = fk_teacher_id;
    }

    public String getPk_grade_class_id() {
        return pk_grade_class_id;
    }

    public void setPk_grade_class_id(String pk_grade_class_id) {
        this.pk_grade_class_id = pk_grade_class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public String getFk_school_id() {
        return fk_school_id;
    }

    public void setFk_school_id(String fk_school_id) {
        this.fk_school_id = fk_school_id;
    }
}