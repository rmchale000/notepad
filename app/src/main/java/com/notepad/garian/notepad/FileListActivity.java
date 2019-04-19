package com.notepad.garian.notepad;

import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileListActivity extends AppCompatActivity implements DeleteDialogFragment.DeleteDialogListener {

    public static final String FILE_NAME = "com.notepad.garian.notepad.FILE_NAME";
    public static final String FILE_CONTENTS = "com.notepad.garian.notepad.FILE_CONTENTS";
    private RecyclerView filesRecyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String fileBeingDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        filesRecyclerView = (RecyclerView) findViewById(R.id.filesRecyclerView);
        filesRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        filesRecyclerView.setLayoutManager(layoutManager);

        //TODO find out if request permissions is necessary here
        File filesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/notepad/");
        File[] files = filesDir.listFiles();
        if (files == null || files.length == 0) {
            DialogFragment fragment = new BlankListDialogFragment();
            fragment.show(getSupportFragmentManager(), "Blank list");
        }
        rvAdapter = new FileListAdapter(files);
        filesRecyclerView.setAdapter(rvAdapter);
    }

    public void loadPage(View view){
        try {
            TextView titleView = (TextView) ((View) view.getParent()).findViewById(R.id.filenameLI);
            String filename = titleView.getText().toString();
            Log.e("view toString", view.toString());
            Log.e("view parent toString", view.getParent().toString());
            Log.e("list activity", "loadPage called " + filename);

            File readFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/notepad/" + filename);
            BufferedReader br = new BufferedReader(new FileReader(readFile));
            String fileContents = "";
            String line = br.readLine();
            while(line!=null){
                fileContents+= line+"\n";
                line = br.readLine();
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(FILE_NAME, filename);
            intent.putExtra(FILE_CONTENTS, fileContents);
            startActivity(intent);
        }catch(Exception e){e.printStackTrace();}
    }

    public void deletePage(View view){
        TextView titleView = (TextView) ((View) view.getParent()).findViewById(R.id.filenameLI);
        String filename = titleView.getText().toString();
        Log.e("list activity","deletePage called "+filename);
        setFileBeingDeleted(filename);
        DialogFragment fragment = new DeleteDialogFragment();
        fragment.show(getSupportFragmentManager(),"File Name");
        Bundle bundl = new Bundle();
        bundl.putCharSequence("filename",filename);
        fragment.setArguments(bundl);
    }

    public void onDeletePositiveClick(DialogFragment dialogFragment){
        try{
            File condemnedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/notepad/" + getFileBeingDeleted());
            condemnedFile.delete();
            dialogFragment.dismiss();
            File filesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/notepad/");
            File[] files = filesDir.listFiles();
            if(files==null || files.length==0){
                DialogFragment fragment = new BlankListDialogFragment();
                fragment.show(getSupportFragmentManager(),"Blank list");
            }
            ((FileListAdapter)rvAdapter).setFileList(files);
            rvAdapter.notifyDataSetChanged();
            //recreate();
            View contextViewtest = findViewById(R.id.filesRecyclerView);
            Snackbar.make(contextViewtest, getFileBeingDeleted()+" "+getBaseContext().getString(R.string.was_deleted), Snackbar.LENGTH_SHORT).show();
        }catch(Exception e){e.printStackTrace();}
    }
    public void onDeleteNegativeClick(DialogFragment dialogFragment){
        setFileBeingDeleted(null);
    }

    public void setFileBeingDeleted(String filename){
        fileBeingDeleted = filename;
    }
    public String getFileBeingDeleted(){
        return fileBeingDeleted;
    }
}
