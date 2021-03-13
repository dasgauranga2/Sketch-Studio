package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class DetailSketchActivity extends AppCompatActivity {

    ImageView image;
    TextView title;
    Intent intent;
    String file_path;

    // delete the image
    public void delete_image(View view) {
        // launch an alert dialog
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.alert_triangle)
                .setTitle("Are you sure you want to delete ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File image_file = new File(file_path);
                        image_file.delete();
                        // go back to the main activity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sketch);

        image = findViewById(R.id.imageView);
        title = findViewById(R.id.sketchTitle);

        intent = getIntent();
        // get the path of the image
        file_path = intent.getStringExtra("FILE_PATH");
        // get the image bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(file_path);
        image.setImageBitmap(bitmap);
        // get the file name
        title.setText(intent.getStringExtra("FILE_NAME"));
    }
}