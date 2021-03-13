package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class DetailSketchActivity extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sketch);

        image = findViewById(R.id.imageView);

        Intent intent = getIntent();
        String file_path = intent.getStringExtra("FILE_NAME");
        //File image_file = new File(file_name);
        Bitmap bitmap = BitmapFactory.decodeFile(file_path);
        image.setImageBitmap(bitmap);
    }
}