package com.abings.baby.injection.component;

import android.content.Context;

import com.abings.baby.injection.ActivityContext;
import com.abings.baby.injection.PerActivity;
import com.abings.baby.injection.module.ActivityModule;
import com.abings.baby.ui.aboutme.AboutMeActivity;
import com.abings.baby.ui.aboutme.association.AssociationActivity;
import com.abings.baby.ui.aboutme.teacher.AboutMeTeacherFragment;
import com.abings.baby.ui.aboutme.user.AboutMeFragment;
import com.abings.baby.ui.baby.BabyInfoActivity;
import com.abings.baby.ui.contacts.ContactsActivity;
import com.abings.baby.ui.contacts.ContactsFragment;
import com.abings.baby.ui.contacts.detail.ContactDetailActivity;
import com.abings.baby.ui.exercise.Exercise;
import com.abings.baby.ui.exercise.createnew.ExerciseNew;
import com.abings.baby.ui.exercise.exerciseDetail.ExerciseDetailActivity;
import com.abings.baby.ui.feedback.FeedBackActivity;
import com.abings.baby.ui.feedback.FeedBackFragment;
import com.abings.baby.ui.finder.FinderActivity;
import com.abings.baby.ui.finder.FinderFragment;
import com.abings.baby.ui.home2.HomeFragment2;
import com.abings.baby.ui.infolist.InfoListActivity;
import com.abings.baby.ui.infolist.news.NewsActivity;
import com.abings.baby.ui.login.LoginActivity;
import com.abings.baby.ui.login.SplashActivity;
import com.abings.baby.ui.main.MainActivity;
import com.abings.baby.ui.message.center.MsgCenterActivity;
import com.abings.baby.ui.message.center.MsgCenterListFragment;
import com.abings.baby.ui.message.center.MsgCenterListFragment_copy;
import com.abings.baby.ui.message.center.detail.MessageDetailActivity;
import com.abings.baby.ui.message.send.SendMsgActivity;
import com.abings.baby.ui.message.unread.UnreadActivity;
import com.abings.baby.ui.publish.PublishActivity;
import com.abings.baby.ui.publishvideo.PublishVideoActivity;
import com.abings.baby.ui.publishvideo.VideoPlayActivity;
import com.abings.baby.ui.register.RegisterActivity;
import com.abings.baby.ui.setting.SettingFragment;
import com.abings.baby.ui.setting.about.AboutActivity;
import com.abings.baby.ui.setting.passwd.PasswdActivity;
import com.abings.baby.ui.setting.question.QuestionActivity;
import com.abings.baby.ui.setting.version.VersionActivity;
import com.abings.baby.ui.signin.SignInActivity;
import com.abings.baby.ui.signin.SingInFragment;
import com.abings.baby.ui.signin.SingInHistoryActivity;
import com.abings.baby.ui.upapp.UpAppDialogActivity;
import com.abings.baby.ui.waterfall.WaterfallActivity;
import com.abings.baby.ui.waterfall.photoviewpagedetail.PhotoViewpageDetailActivity;
import com.abings.baby.ui.waterfall.photoviewpagedetail.fragment.PhotoDatailFragment;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SignInActivity signInActivity);

    void inject(SplashActivity splashActivity);

    void inject(PhotoDatailFragment photoDatailFragment);

    void inject(Exercise exercise);

    void inject(SingInFragment singInFragment);

    void inject(PhotoViewpageDetailActivity photoViewpageDetailActivity);

    void inject(ExerciseNew exerciseNew);

    void inject(ExerciseDetailActivity exerciseDetailActivity);

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(RegisterActivity registerActivity);

    void inject(FinderActivity finderActivity);

    void inject(QuestionActivity questionActivity);

    void inject(VersionActivity versionActivity);

    void inject(PasswdActivity passwdActivity);

    void inject(WaterfallActivity waterfallActivity);


    void inject(AboutMeActivity aboutMeActivity);

    void inject(InfoListActivity aboutMeActivity);

    void inject(PublishActivity aboutMeActivity);

    void inject(NewsActivity newsActivity);

    void inject(AboutActivity aboutActivity);

    void inject(ContactDetailActivity aboutActivity);

    void inject(SendMsgActivity aboutActivity);

    void inject(ContactsActivity aboutActivity);

    void inject(MessageDetailActivity aboutActivity);

    void inject(BabyInfoActivity aboutActivity);

    void inject(MsgCenterListFragment aboutActivity);

    void inject(MsgCenterListFragment_copy aboutActivity);

    void inject(HomeFragment2 contactsFragment);

    void inject(FinderFragment contactsFragment);

    void inject(ContactsFragment contactsFragment);

    void inject(FeedBackFragment feedBackFragment);

    void inject(SettingFragment settingFragment);

    void inject(UpAppDialogActivity upAppDialogActivity);

    void inject(VideoPlayActivity videoPlayActivity);

    void inject(FeedBackActivity feedBackActivity);

    void inject(PublishVideoActivity publishVideoActivity);

    void inject(AboutMeFragment aboutMeFragment);

    void inject(AssociationActivity associationActivity);

    void inject(AboutMeTeacherFragment aboutMeTeacherFragment);

    @ActivityContext
    Context context();

    void inject(MsgCenterActivity msgCenterActivity);

    void inject(UnreadActivity unreadActivity);
}

