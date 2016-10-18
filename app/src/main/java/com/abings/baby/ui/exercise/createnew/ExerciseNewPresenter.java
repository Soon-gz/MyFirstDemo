package com.abings.baby.ui.exercise.createnew;

import com.abings.baby.data.DataManager;
import com.abings.baby.ui.base.Presenter;

import javax.inject.Inject;

/**
 * Created by HaomingXu on 2016/7/11.
 */
public class ExerciseNewPresenter implements Presenter<ExerciseNewMvpView> {

    private DataManager dataManager;
    private ExerciseNewMvpView mvpView;

    @Inject
    public ExerciseNewPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ExerciseNewMvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {

    }

    public void ucrateNewExercise(){
        mvpView.updateNew();
    }
}
