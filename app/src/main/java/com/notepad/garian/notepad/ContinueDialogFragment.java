package com.notepad.garian.notepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class ContinueDialogFragment extends DialogFragment {

    public interface ContinueDialogListener{
        public void onContinuePositiveClick(DialogFragment dialog);
        public void onContinueNegativeClick(DialogFragment dialog);
    }

    ContinueDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Continue without Saving?")
                .setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("setting positive button",ContinueDialogFragment.this.toString());
                        listener.onContinuePositiveClick(ContinueDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onContinueNegativeClick(ContinueDialogFragment.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (ContinueDialogFragment.ContinueDialogListener)context;
        }catch(ClassCastException cce){
            throw new ClassCastException("activity must implement ContinueDialogListener");
        }
    }
}
