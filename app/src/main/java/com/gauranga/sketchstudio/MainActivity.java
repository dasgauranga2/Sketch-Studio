package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    File[] files;
    ArrayList<String> file_names;

    // add a sketch
    public void add_sketch(View view) {
        Intent intent = new Intent(this, AddSketchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        ContextWrapper wrapper = new ContextWrapper(this);
        // directory where all image files are stored
        File file = wrapper.getDir("test", MODE_PRIVATE);
        // list of all image files
        files = file.listFiles();
        // array list of image file names
        file_names = new ArrayList<>();
        for (File image_file : files) {
            file_names.add(image_file.getName());
            setup_recyclerview();
        }
    }

    // setup the recycler view
    public void setup_recyclerview() {
        SketchListAdapter adapter = new SketchListAdapter(this, file_names, files);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}