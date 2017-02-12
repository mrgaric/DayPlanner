package com.igordubrovin.dayplanner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class UpdateDialog extends DialogFragment {

    interface CallbackActivityUpdateDialog{
        void onClickPositiveBtn();
        void onClickNegativeBtn();
    }

    CallbackActivityUpdateDialog callbackActivity = null;

    protected void registerCallback(CallbackActivityUpdateDialog callbackActivity){
        try {
            this.callbackActivity = callbackActivity;
        }
        catch (NullPointerException e){
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Изменить данные?")
                .setMessage("На данный день уже имеется задача. Обновить задачу?")
                .setPositiveButton("изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        callbackActivity.onClickPositiveBtn();
                        dismiss();
                    }
                })
                .setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        callbackActivity.onClickNegativeBtn();
                        dismiss();
                    }
                });

        return builder.create();
    }
    @Override
    public void dismiss() {
        super.dismiss();
        callbackActivity = null;
    }
}
