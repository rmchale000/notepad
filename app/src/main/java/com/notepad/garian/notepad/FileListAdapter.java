package com.notepad.garian.notepad;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.FileListViewHolder> {
    private String[] filenames;

    public static class FileListViewHolder extends RecyclerView.ViewHolder{
        public TextView filenameView;
        public FileListViewHolder(View v){
            super(v);
            filenameView = (TextView) v.findViewById(R.id.filenameLI);
        }
    }

    public FileListAdapter(String[] filenamesIn){
        filenames = filenamesIn;
    }

    @Override
    public FileListAdapter.FileListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View tv = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_item, parent, false);
        FileListViewHolder vh = new FileListViewHolder(tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(FileListViewHolder holder, int position){
        holder.filenameView.setText(filenames[position]);
    }

    @Override
    public int getItemCount(){
        return filenames.length;
    }
}
