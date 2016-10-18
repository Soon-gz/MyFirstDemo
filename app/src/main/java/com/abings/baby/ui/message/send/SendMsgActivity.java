package com.abings.baby.ui.message.send;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.abings.baby.R;
import com.abings.baby.WineApplication;
import com.abings.baby.data.model.ClassItem;
import com.abings.baby.data.model.ClassTeacherItem;
import com.abings.baby.data.model.Contact;
import com.abings.baby.data.model.UserContact;
import com.abings.baby.ui.base.BaseActivity;
import com.abings.baby.ui.exercise.createnew.ChooseClassDialog;
import com.abings.baby.ui.publish.GridAdapter;
import com.abings.baby.utils.CustomAlertDialog;
import com.abings.baby.utils.KeyBoardUtils;
import com.abings.baby.utils.ProgressDialogHelper;
import com.abings.baby.utils.StringUtils;
import com.abings.baby.utils.TLog;
import com.abings.baby.widget.CustomEditText;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.socks.library.KLog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created on 16/2/19.
 * 提示：注释的代码是教师端向单个客户端发送消息的逻辑   2016/07/27
 */
public class SendMsgActivity extends BaseActivity implements SendMsgMvpView {

    @Inject
    SendMsgPresenter sendMsgPresenter;


    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.send)
    TextView send;
    @Bind(R.id.target)
    CustomEditText target;
    @Bind(R.id.add_contact)
    ImageView addContact;
    @Bind(R.id.msg_title)
    CustomEditText msgTitle;
    @Bind(R.id.input_text)
    EditText inputText;
    @Bind(R.id.add_img)
    ImageView addImg;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.clearMsg)
    ImageView clearMsg;

    private int Type = 1;

    private GridAdapter gridAdapter;
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;

    private static final int REQUEST_CAMERA_CONTACTS = 30;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private StringBuilder classesIds;
    private UserContact.ImagesBean mContact;
    private Contact contact;
    private List<ClassItem>clases;
    private List<ClassTeacherItem>teachers;
    private boolean isToTeacher = true;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_sendmsg;
    }

    @Override
    protected void iniInjector() {
        activityComponent().inject(this);
        sendMsgPresenter.attachView(this);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }


    protected boolean group_send;
    protected ArrayList<Contact> contacts;

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        KLog.e("initViewsAndEvents ");

        classesIds = new StringBuilder();
        String type = getIntent().getStringExtra("type");
        switch (type){
            case "classes":
                TLog.getInstance().i("给班级发送消息");
                isToTeacher = false;
                clases = WineApplication.getInstance().getMyClasss();
                break;
            case "teacher":
                TLog.getInstance().i("给老师发送消息");
                isToTeacher = true;
                sendMsgPresenter.loadSchoolContacts();
                break;
        }

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    public boolean bindEvents() {
        return false;
    }

    @Override
    public void showData(List data, boolean canNext) {

    }

    @Override
    public void showLoadingProgress(boolean show) {

    }

    @Override
    public void showMessage(String mes) {
        showToast(mes);
    }

    @Override
    public void showError(String mes) {
        showToast(mes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;

                case REQUEST_CAMERA_CONTACTS:
                    group_send = getIntent().getBooleanExtra("group_send", false);
                    if (group_send) {
                        contacts = (ArrayList<Contact>) getIntent().getSerializableExtra("contacts");
                        String temp = "";
                        for (Contact c : contacts) {
                            temp += c.getName() + ", ";
                        }
                        target.setText(temp);
                    }
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        if (paths.contains(GridAdapter.ADD)) {
            paths.remove(GridAdapter.ADD);
        }
        paths.add(GridAdapter.ADD);
        imagePaths.addAll(paths);
        gridAdapter = new GridAdapter(this, imagePaths);
        gridView.setAdapter(gridAdapter);
        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProgressDialog pd;

    public void showPd() {
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading Picture...");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    public void showDialog(String msg) {
        ProgressDialogHelper.getInstance().showProgressDialog(this, msg);
    }

    @Override
    public void dialogDismiss() {
        ProgressDialogHelper.getInstance().hideProgressDialog();
    }


    @Override
    public void onTokenError() {
        startLoginActivity();
    }

    @Override
    public void finishActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void showSchoolContacts(List<ClassTeacherItem> teacherItems) {
        teachers = teacherItems;
    }

    @OnClick({R.id.btn_back, R.id.add_img, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (!StringUtils.isEmpty(target.getText().toString())||!StringUtils.isEmpty(inputText.getText().toString())||!StringUtils.isEmpty(msgTitle.getText().toString())){
                    CustomAlertDialog.dialogWithSureCancel("确定放弃编辑页面？", this);
                }else{
                    finish();
                }
                break;
//            case R.id.add_contact:
//                Intent intentoContacts = new Intent(this, ContactsActivity.class);
//                intentoContacts.putExtra("sendMsg","0");
//                startActivity(intentoContacts);
//                finish();
//                break;
            case R.id.add_img:
                ArrayList<String> images = new ArrayList<>();
                images.addAll(imagePaths);
                PhotoPickerIntent intent = new PhotoPickerIntent(SendMsgActivity.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true); // 是否显示拍照
                intent.setMaxTotal(9); // 最多选择照片数量，默认为6
                intent.setSelectedPaths(images); // 已选中的照片地址， 用于回显选中状态
                startActivityForResult(intent, REQUEST_CAMERA_CODE);
                break;
            case R.id.send:
                KeyBoardUtils.closeKeybord(this);
                sendClassMsg();
                break;
        }
    }

    //发送班级消息
    private void sendClassMsg() {
        if (checkInput()){
            String mes = target.getText().toString().trim();
            String[]mess = mes.split(",");
            if (isToTeacher){
                for (int i = 0; i < mess.length; i++) {
                    for (int j = 0; j < teachers.size(); j++) {
                        if (mess[i].equals(teachers.get(j).getName())){
                            classesIds.append(teachers.get(j).getPk_teacher_id());
                            if (i < mess.length - 1){
                                classesIds.append(",");
                            }
                        }
                    }
                }
            }else{
                for (int i = 0; i < mess.length; i++) {
                    for (int j = 0; j < clases.size(); j++) {
                        if (mess[i].equals(clases.get(j).getClass_name())){
                            classesIds.append(clases.get(j).getPk_grade_class_id());
                            if (i < mess.length - 1){
                                classesIds.append(",");
                            }
                        }
                    }
                }
            }
            sendMsgPresenter.sendClassMsg(classesIds.toString(),"2",msgTitle.getText().toString(),inputText.getText().toString(),isToTeacher);
        }
    }

    @OnClick(R.id.clearMsg)
    public void clear(){
        target.setText("");
        addContact.setVisibility(View.VISIBLE);
        clearMsg.setVisibility(View.GONE);
    }

    public boolean checkInput() {
        if (StringUtils.isEmpty(msgTitle.getText().toString())) {
            showToast("请输入消息标题");
            return false;
        }

        if (StringUtils.isEmpty(target.getText().toString())){
            showToast("请选择要发送消息的班级");
            return false;
        }

        if (StringUtils.isEmpty(inputText.getText().toString())) {
            showToast("请输入消息内容");
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendMsgPresenter.detachView();
    }

    //点击弹出班级列表选择
    @OnClick(R.id.add_contact)
    public void addcontact(){
        WineApplication.getInstance().getMyClasss();
        if (isToTeacher){
            final String[] stringItems = new String[teachers.size()];
            for (int i = 0; i < teachers.size(); i++) {
                stringItems[i] = teachers.get(i).getName();
            }
            selectItes(stringItems);
        }else{
            final String[] stringItems = new String[clases.size()];
            for (int i = 0; i < clases.size(); i++) {
                stringItems[i] = clases.get(i).getClass_name();
            }
            selectItes(stringItems);
        }
    }

    public void selectItes(String[] stringItems){
        final ChooseClassDialog dialog = new ChooseClassDialog(this, stringItems, null,target,addContact,clearMsg);
        final int[]flags = new int[stringItems.length];
        for (int i = 0; i < flags.length; i++) {
            flags[i] = 0;
        }
        dialog.isTitleShow(true).isCancelShow(false)
                .title("请选择")
                .titleBgColor(getResources().getColor(R.color.ese_orange))
                .titleTextColor(getResources().getColor(R.color.ese_white))
                .itemTextColor(getResources().getColor(R.color.ese_white))
                .setFlags(flags)
                .lvBgColor(getResources().getColor(R.color.baby_orange))
                .show();
    }

}
