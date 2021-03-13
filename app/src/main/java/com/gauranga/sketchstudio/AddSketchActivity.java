package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AddSketchActivity extends AppCompatActivity {

    CanvasView canvas;
    EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sketch);

        canvas = findViewById(R.id.canvas);
        title = findViewById(R.id.titleText);

        // set the stroke color
        canvas.setPaintStrokeColor(Color.BLUE);
        // set the stroke width
        canvas.setPaintStrokeWidth(10);
    }

    public void save_sketch(View view) {

        String title_text = title.getText().toString();
        Bitmap bitmap = canvas.getBitmap();

        ContextWrapper wrapper = new ContextWrapper(this);
        File file = wrapper.getDir("test", MODE_PRIVATE);
        File image_file = new File(file, title_text + ".jpg");
        try {
            OutputStream stream = null;
            // an output stream that writes bytes to a file
            stream = new FileOutputStream(image_file);
            // write a compressed version of the bitmap to the specified output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}