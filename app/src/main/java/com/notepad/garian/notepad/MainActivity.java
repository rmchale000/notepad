package com.notepad.garian.notepad;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements SaveNameDialogFragment.SaveNameDialogListener, ContinueDialogFragment.ContinueDialogListener, TextWatcher {

    public static final String EXTRA_MESSAGE = "com.notepad.garian.notepad.MESSAGE";
    public static final int WRITE_EXTERNAL_STORAGE_CODE=7;
    private DialogFragment saveDialog;
    private boolean isUnsavedText=false;
    private boolean isNewPage=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(this);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Intent intent = getIntent();
        String filename = intent.getStringExtra(FileListActivity.FILE_NAME);
        String fileContents = intent.getStringExtra(FileListActivity.FILE_CONTENTS);

        if(filename!=null) {
            TextView titleField = (TextView) findViewById(R.id.titleText);
            titleField.setText(filename);
            isNewPage=false;
        }
        if(fileContents!=null){
            EditText pageContents = (EditText) findViewById(R.id.editText);
            pageContents.setText(fileContents);
        }
        isUnsavedText=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notepad_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.new_page:
                if(isUnsavedText)
                    new ContinueDialogFragment().show(getSupportFragmentManager(),"new page");
                else
                    newPage();
                return true;
            case R.id.save_page:
                TextView titleField = (TextView) findViewById(R.id.titleText);
                CharSequence currentName = "";
                if(!isNewPage)
                    currentName = titleField.getText();
                DialogFragment fragment = new SaveNameDialogFragment();
                fragment.show(getSupportFragmentManager(),"File Name");
                Bundle bundl = new Bundle();
                bundl.putCharSequence("currentName",currentName);
                fragment.setArguments(bundl);
                return true;
            case R.id.view_pages:
                Intent intent = new Intent(this, FileListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void newPage(){
        TextView titleText = (TextView)findViewById(R.id.titleText);
        titleText.setText(R.string.new_file_title);
        EditText editText = (EditText)findViewById(R.id.editText);
        editText.setText("");
        isUnsavedText=false;
        isNewPage=true;
    }
    public boolean savePad(String fileName){
        try {
            if (isExternalStorageWritable()) {

                //File filesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/notepad");
                /*File filesDirName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
                Log.e("save file","filesDirName is "+filesDirName);
                filesDirName.createNewFile();*/
                //File filesDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/notepad");
                File filesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/notepad");
                //File filesDir = new File(getFilesDir()+"/notepad");
                filesDir.mkdirs();
                File newFile = new File(filesDir, fileName);
                newFile.createNewFile();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile)));
                EditText editText = (EditText) findViewById(R.id.editText);
                String contents = editText.getText().toString();
                bw.write(contents);
                bw.close();
                isUnsavedText=false;
                isNewPage=false;
                View contextView = findViewById(R.id.editText);
                Snackbar.make(contextView, getBaseContext().getString(R.string.file_saved)+" "+fileName, Snackbar.LENGTH_SHORT).show();
            } else {
                Log.e("test tag","popup Storage not available");
            }
            return true;
        }catch(IOException ioe){ioe.printStackTrace(); return false;}
    }

    public void onSavePositiveClick(DialogFragment dialogFragment){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            setSaveDialog(dialogFragment);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
        }else {
            EditText filenameField = (EditText) dialogFragment.getDialog().findViewById(R.id.filenameField);
            String filename = filenameField.getText().toString();
            if(!filename.endsWith(".txt"))
                filename+=".txt";
            if (savePad(filename)) {
                Log.e("log", filename + " saved");
                TextView titleField = (TextView) findViewById(R.id.titleText);
                titleField.setText(filename);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){
        /*switch(requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    
                }
                return;
        }*/
    }

    public void onSaveNegativeClick(DialogFragment dialog){}

    public void onContinuePositiveClick(DialogFragment dialog){
        newPage();
    }
    public void onContinueNegativeClick(DialogFragment dialog){}

    public void onTextChanged(CharSequence c, int start, int before, int count){
        isUnsavedText=true;
    }
    public void beforeTextChanged(CharSequence c, int start, int count, int after) { }
    public void afterTextChanged(Editable c) { }

    public void setSaveDialog(DialogFragment fragment){
        saveDialog = fragment;
    }
    public DialogFragment getSaveDialog(){
        return saveDialog;
    }
    public void unsavedText(){isUnsavedText=true;}
    public boolean getUnsavedText(){return isUnsavedText;}
    /*public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextOld);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/
}
