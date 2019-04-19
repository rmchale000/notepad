package com.notepad.garian.notepad;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {
    private String[] filenames;
    private File[] files;
    private DateFormat dateFormat;

    public static class FileListViewHolder extends RecyclerView.ViewHolder{
        public TextView filenameView,filetimeView;
        public FileListViewHolder(View v){
            super(v);
            filenameView = (TextView) v.findViewById(R.id.filenameLI);
            filetimeView = (TextView) v.findViewById(R.id.filetimeLI);
        }
    }

    public FileListAdapter(String[] filenamesIn){
        filenames = filenamesIn;
    }
    public FileListAdapter(File[] filesIn) {
        files=filesIn;
        dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
    }

    public void setFileList(File[] filesIn){
        files=filesIn;
    }

    @Override
    public FileListAdapter.FileListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View tv = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_item, parent, false);
        FileListViewHolder vh = new FileListViewHolder(tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(FileListViewHolder holder, int position){
        holder.filenameView.setText(files[position].getName());
        String dateString;
        try{
            dateString = dateFormat.format(files[position].lastModified());
        } catch(Exception e){dateString = "";}
        holder.filetimeView.setText(dateString);
    }

    @Override
    public int getItemCount(){
        return files.length;
    }
}
