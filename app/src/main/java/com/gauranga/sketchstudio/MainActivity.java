package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;

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

    ListView listView;

    // add a sketch
    public void add_sketch(View view) {
        Intent intent = new Intent(this, AddSketchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        ContextWrapper wrapper = new ContextWrapper(this);
        File file = wrapper.getDir("test", MODE_PRIVATE);
        File[] files = file.listFiles();

        ArrayList<String> file_names = new ArrayList<>();
        for (File image_file : files) {
            file_names.add(image_file.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,file_names);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailSketchActivity.class);
                intent.putExtra("FILE_NAME", files[position].getAbsolutePath());
                startActivity(intent);
            }
        });
    }
}