package com.abings.baby.ui.exercise.exerciseDetail;

import com.abings.baby.data.DataManager;
import com.abings.baby.ui.base.Presenter;

import javax.inject.Inject;

/**
 * Created by HaomingXu on 2016/7/11.
 */
public class ExerciseDetailPrecenter implements Presenter<ExerciseDetailMvpView> {

    private DataManager dataManager;
    private ExerciseDetailMvpView mvpView;

    @Inject
    public ExerciseDetailPrecenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ExerciseDetailMvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {

    }
}
