package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    File[] files;
    ArrayList<String> file_names;
    String IMG_FILE_SEP = "______";

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

        // array list containing each image file
        ArrayList<ImageFile> img_files = new ArrayList<>();
        for (int i=0; i<file_names.size(); i++) {
            // each object will have both file name and file object
            ImageFile img_file = new ImageFile(files[i], file_names.get(i));
            img_files.add(img_file);
        }
        // sort the array list using the timestamp as the key
        Collections.sort(img_files, new CustomComparator());

        SketchListAdapter adapter = new SketchListAdapter(this, img_files);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class ImageFile {
        long key;
        String file_name;
        File file;

        ImageFile(File f, String fn) {
            key = Long.parseLong(fn.split(IMG_FILE_SEP)[0]);
            file_name = fn;
            file = f;
        }

        @Override
        public String toString() {
            return file_name;
        }
    }

    public class CustomComparator implements Comparator<ImageFile> {

        @Override
        public int compare(ImageFile img1, ImageFile img2) {
            if (img1.key > img2.key) {
                return -1;
            }
            else if (img1.key < img2.key) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }
}