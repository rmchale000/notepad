package com.notepad.garian.notepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

public class SaveNameDialogFragment extends DialogFragment {

    public interface SaveNameDialogListener{
        public void onSavePositiveClick(DialogFragment dialog);
        public void onSaveNegativeClick(DialogFragment dialog);
    }

    SaveNameDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_filename)
                .setView(inflater.inflate(R.layout.dialog_filename, null))
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("setting positive button",SaveNameDialogFragment.this.toString());
                        listener.onSavePositiveClick(SaveNameDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onSaveNegativeClick(SaveNameDialogFragment.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener = (SaveNameDialogListener)context;
        }catch(ClassCastException cce){
            throw new ClassCastException("activity must implement SaveNameDialogListener");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        EditText titleField = (EditText) this.getDialog().findViewById(R.id.filenameField);
        String currentName = (String)getArguments().get("currentName");
        if(currentName.endsWith(".txt"))
            currentName = currentName.substring(0,currentName.length()-4);
        titleField.setText((CharSequence) currentName);
    }
}
