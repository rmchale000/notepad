package com.notepad.garian.notepad;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.notepad.garian.notepad.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                System.out.println("new page");
                return true;
            case R.id.save_page:
                try {
                    Log.e("test tag","save page");
                    if (isExternalStorageWritable()) {
                        //File filesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/notepad");
                        File filesDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/notepad");
                        //File filesDir = new File(getFilesDir()+"/notepad");
                        filesDir.mkdirs();
                        File newFile = new File(filesDir, "artnotes.txt");
                        Log.e("test tag", "newFile is " + newFile);
                        if(!newFile.exists())
                            newFile.createNewFile();
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile)));
                        EditText editText = (EditText) findViewById(R.id.editText);
                        String contents = editText.getText().toString();
                        bw.write(contents);
                        bw.close();
                    } else {
                        Log.e("test tag","popup Storage not available");
                    }
                    return true;
                }catch(IOException ioe){ioe.printStackTrace(); return false;}
            case R.id.load_page:
                System.out.println("load page");
                return true;
            case R.id.view_pages:
                System.out.println("view pages");
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

    public void savePad(View view){
        
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextOld);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
