package com.gauranga.sketchstudio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VoiceCommandAdapter extends RecyclerView.Adapter<VoiceCommandAdapter.MyViewHolder> {

    String[] titles;
    String[] descriptions;
    Context context;

    // the context and the data is passed to the adapter
    public VoiceCommandAdapter(Context ct, String t[], String d[]) {
        context = ct;
        titles = t;
        descriptions = d;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // The 'voice_command_row.xml' file in layout folder defines
        // the style for each row
        View view = inflater.inflate(R.layout.voice_command_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // use the data passed to the adapter above
        // and set the data to the text views below
        holder.title.setText(titles[position]);
        holder.description.setText(descriptions[position]);
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return titles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will contain two text views
        // for displaying the data
        TextView title,description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Set the text view from the 'row.xml' file in layout folder
            title = itemView.findViewById(R.id.vcTitleText);
            description = itemView.findViewById(R.id.vcDescriptionText);
        }
    }
}