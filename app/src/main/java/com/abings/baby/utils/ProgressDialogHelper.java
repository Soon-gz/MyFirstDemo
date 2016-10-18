package com.abings.baby.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;


public class ProgressDialogHelper {
    private ProgressDialog progressDialog = null;

    private AlertDialog alertDialog = null;
    private static final ProgressDialogHelper mInstance = new ProgressDialogHelper();
    private String content;

    private ProgressDialogHelper() {

    }

    public static ProgressDialogHelper getInstance() {
        return mInstance;
    }

    synchronized public boolean isWorking() {
        if (progressDialog == null || !progressDialog.isShowing()) return false;
        return true;
    }

    synchronized public void showProgressDialog(Activity act, String title, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(act);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (title != null) {
                progressDialog.setTitle(title);
            }
            if (message == null) {
                message = "Wait for process...";
            }
            progressDialog.setMessage(message);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    synchronized public void showProgressDialog(Context act, String message) {
        Log.e("ProgressDialogHelper", "showProgressDialog()  message = " + message);
        if (progressDialog != null && progressDialog.isShowing() && content.equals(message)) {
            Log.e("ProgressDialogHelper", "showProgressDialog()  ..............");
            return;
        } else if (progressDialog != null && progressDialog.isShowing() && !content.equals(message)) {
            hideProgressDialog();
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(act);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (message == null) {
                message = "Wait for process...";
            }
            content = message;
            progressDialog.setMessage(content);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    synchronized public void showProgressDialog(Activity act, String message, OnCancelListener listener) {
        Log.e("ProgressDialogHelper", "showProgressDialog()  message = " + message);
        if (progressDialog != null && progressDialog.isShowing() && content.equals(message)) {
            Log.e("ProgressDialogHelper", "showProgressDialog()  ..............");
            return;
        } else if (StringUtils.isEmpty(content) || progressDialog != null && progressDialog.isShowing() && !content
                .equals(message)) {
            hideProgressDialog();
        }

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(act);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            if (message == null) {
                message = "Wait for process...";
            }
            content = message;
            progressDialog.setMessage(content);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(listener);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }


    synchronized public void setMessage(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
        }
    }

    synchronized public void hideProgressDialog() {
        Log.e("ProgressDialogHelper", "hideProgressDialog()  ..............");
        if (progressDialog != null) {
            progressDialog.dismiss();
            content = "";
        }

        progressDialog = null;
    }
}
