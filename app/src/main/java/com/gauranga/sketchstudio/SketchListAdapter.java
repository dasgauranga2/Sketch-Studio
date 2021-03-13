package com.gauranga.sketchstudio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class SketchListAdapter extends RecyclerView.Adapter<SketchListAdapter.MyViewHolder> {

    ArrayList<String> file_names;
    File[] files;
    Context context;

    // the context and the data is passed to the adapter
    public SketchListAdapter(Context ct, ArrayList<String> fn, File[] f) {
        context = ct;
        file_names = fn;
        files = f;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // The 'sketch_list_row.xml' file in layout folder defines the style for each row
        View view = inflater.inflate(R.layout.sketch_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // use the data passed to the adapter above
        // and set the data to the text views below
        holder.title.setText(file_names.get(position));

        // detect if an item is clicked using the root layout of the 'row.xml' file
        holder.row_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an intent to show the sketch
                Intent intent = new Intent(context, DetailSketchActivity.class);
                intent.putExtra("FILE_PATH", files[position].getAbsolutePath());
                intent.putExtra("FILE_NAME", file_names.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return file_names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will contain one text view for displaying the data
        TextView title;
        ConstraintLayout row_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sketchTitleText);
            row_layout = itemView.findViewById(R.id.sr_layout);
        }
    }
}
