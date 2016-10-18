package com.abings.baby.ui.exercise;

import com.abings.baby.data.DataManager;
import com.abings.baby.ui.base.Presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by HaomingXu on 2016/7/11.
 */
public class ExercisePrecenter implements Presenter<ExerciseMvpView> {

    private DataManager dataManager;
    private ExerciseMvpView mvpView;

    @Inject
    public ExercisePrecenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ExerciseMvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {

    }


    public void loadData(){
        List<ExerciseModel>list = new ArrayList<>();
        mvpView.showLoadingProgress(true);
        for (int i = 0; i < 15; i++) {
            ExerciseModel exerciseModel = new ExerciseModel();
            exerciseModel.setAddress("上海迪斯尼");
            exerciseModel.setTime("2016.07." + i);
            exerciseModel.setTitle("暑假旅游计划");
            exerciseModel.setContent("集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，集体去迪斯尼换了三天，");
            exerciseModel.setMoney("8000元/人");
            exerciseModel.setPeoples("30人");
            exerciseModel.setTeacher("李老师");
            exerciseModel.setStoptime("2016.06.12");
            if (i > 10){
                exerciseModel.setIsRead(false);
            }else{
                exerciseModel.setIsRead(true);
            }
            list.add(exerciseModel);
        }
        ExerciseModel exerciseModel = new ExerciseModel();
        exerciseModel.setAddress("北京迪斯尼");
        exerciseModel.setTime("2016.07.31");
        exerciseModel.setTitle("暑假旅游计划");
        list.add(exerciseModel);
        mvpView.showLoadingProgress(false);
        mvpView.showData(list,false);
    }
}
