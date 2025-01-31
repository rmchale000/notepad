package com.notepad.garian.notepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class DeleteDialogFragment extends DialogFragment {

    public interface DeleteDialogListener{
        public void onDeletePositiveClick(DialogFragment dialog);
        public void onDeleteNegativeClick(DialogFragment dialog);
    }

    DeleteDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete "+getArguments().get("filename")+"?")
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("setting positive button",DeleteDialogFragment.this.toString());
                        listener.onDeletePositiveClick(DeleteDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDeleteNegativeClick(DeleteDialogFragment.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (DeleteDialogFragment.DeleteDialogListener)context;
        }catch(ClassCastException cce){
            throw new ClassCastException("activity must implement DeleteDialogListener");
        }
    }
}
