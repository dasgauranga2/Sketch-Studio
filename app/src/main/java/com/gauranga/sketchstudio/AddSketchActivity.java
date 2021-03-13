package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import es.dmoral.toasty.Toasty;

// GITHUB PROJECT LINK - https://github.com/Korilakkuma/CanvasView
// GITHUB PROJECT LINK - https://github.com/skydoves/PowerMenu#menu-effect
public class AddSketchActivity extends AppCompatActivity {

    CanvasView canvas;
    EditText title;
    PowerMenu strokewidth_menu;

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
        // set the stroke color
        canvas.setPaintStrokeColor(Color.parseColor("#8F6450"));
        // set the background color
        canvas.setBaseColor(Color.parseColor("#CAF3BB"));
    }

    public void save_sketch(View view) {
        // get the title entered by the user
        String title_text = title.getText().toString();
        // get the image btmap from the canvas
        Bitmap bitmap = canvas.getBitmap();
        // check for empty title
        if (title_text.length()==0) {
            Toast toast = Toasty.custom(this, "TITLE EMPTY", R.drawable.error_icon, R.color.purple_500, 500, true, true);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 0);
            toast.show();
            return;
        }

        // save the image file
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
            // go back to the main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // change the stroke width
    public void change_strokewidth(View view) {
        // create the popup menu
        // an set its properties
        if (strokewidth_menu == null) {
            strokewidth_menu = new PowerMenu.Builder(this)
                    .addItem(new PowerMenuItem("1"))
                    .addItem(new PowerMenuItem("3"))
                    .addItem(new PowerMenuItem("6"))
                    .addItem(new PowerMenuItem("9"))
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(30f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextGravity(Gravity.CENTER)
                    .setWidth(200)
                    .setTextSize(15)
                    .setMenuColor(Color.WHITE)
                    .setTextColor(ContextCompat.getColor(this, R.color.purple_500))
                    .setSelectedTextColor(Color.WHITE)
                    .setSelectedMenuColor(Color.BLUE)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                        @Override
                        public void onItemClick(int position, PowerMenuItem item) {
                            int new_sw = Integer.parseInt((String) item.getTitle());
                            canvas.setPaintStrokeWidth(new_sw);
                            strokewidth_menu.setSelectedPosition(position);
                            strokewidth_menu.dismiss();
                        }
                    })
                    .build();
        }
        // display the popup menu
        strokewidth_menu.showAsAnchorLeftTop(view,0,-strokewidth_menu.getContentViewHeight());
    }
}