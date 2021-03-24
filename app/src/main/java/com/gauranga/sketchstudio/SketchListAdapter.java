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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SketchListAdapter extends RecyclerView.Adapter<SketchListAdapter.MyViewHolder> {

    ArrayList<MainActivity.ImageFile> img_files;
    Context context;
    String IMG_FILE_SEP = "______";

    // the context and the data is passed to the adapter
    public SketchListAdapter(Context ct, ArrayList<MainActivity.ImageFile> ifs) {
        context = ct;
        img_files = ifs;
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
        String file_name = img_files.get(position).file_name.substring(19, img_files.get(position).file_name.length()-4);
        holder.title.setText(file_name);

        // set the date of each image file
        Date date = new Date(img_files.get(position).key);
        SimpleDateFormat date_format = new SimpleDateFormat("dd MMM yyyy");
        holder.date.setText(date_format.format(date));

        // detect if an item is clicked using the root layout of the 'sketch_list_row.xml' file
        holder.row_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an intent to show the sketch
                Intent intent = new Intent(context, DetailSketchActivity.class);
                intent.putExtra("FILE_PATH", img_files.get(position).file.getAbsolutePath());
                intent.putExtra("FILE_NAME", file_name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return img_files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will contain one text view for displaying the data
        TextView title;
        TextView date;
        ConstraintLayout row_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sketchTitleText);
            date = itemView.findViewById(R.id.sketchDateText);
            row_layout = itemView.findViewById(R.id.sr_layout);
        }
    }
}
