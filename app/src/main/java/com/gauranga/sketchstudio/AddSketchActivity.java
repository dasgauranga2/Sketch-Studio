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
import android.widget.ImageButton;
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
    PowerMenu strokewidth_menu,strokecolor_menu,drawingmode_menu;

    private boolean ERASER_MODE = false;
    private int STROKE_COLOR = Color.BLUE;
    private int STROKE_WIDTH = 10;
    private int BACKGROUND_COLOR = Color.parseColor("#CAF3BB");
    private CanvasView.Drawer DRAWING_MODE = CanvasView.Drawer.PEN;

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
        // set the background color
        canvas.setBaseColor(BACKGROUND_COLOR);
        //canvas.setLayerType(View.LAYER_TYPE_HARDWARE, null);
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
                    .addItem(new PowerMenuItem("4"))
                    .addItem(new PowerMenuItem("8"))
                    .addItem(new PowerMenuItem("12"))
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(30f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextGravity(Gravity.CENTER)
                    .setWidth(200)
                    .setTextSize(15)
                    .setMenuColor(Color.WHITE)
                    .setTextColor(ContextCompat.getColor(this, R.color.purple_500))
                    .setSelectedTextColor(ContextCompat.getColor(this, R.color.purple_500))
                    .setSelectedMenuColor(Color.LTGRAY)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                        @Override
                        public void onItemClick(int position, PowerMenuItem item) {
                            int new_sw = Integer.parseInt((String) item.getTitle());
                            STROKE_WIDTH = new_sw;
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

    // change the stroke color
    public void change_strokecolor(View view) {
        // create the popup menu
        // an set its properties
        if (strokecolor_menu == null) {
            strokecolor_menu = new PowerMenu.Builder(this)
                    .addItem(new PowerMenuItem("", R.drawable.strokecolor_black))
                    .addItem(new PowerMenuItem("", R.drawable.strokecolor_red))
                    .addItem(new PowerMenuItem("", R.drawable.strokecolor_blue))
                    .addItem(new PowerMenuItem("", R.drawable.strokecolor_green))
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(30f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextGravity(Gravity.CENTER)
                    .setWidth(170)
                    .setTextSize(15)
                    .setMenuColor(Color.WHITE)
                    .setSelectedMenuColor(Color.LTGRAY)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                        @Override
                        public void onItemClick(int position, PowerMenuItem item) {
                            ImageButton sc_button = (ImageButton) view;
                            switch (position) {
                                case 1:
                                    canvas.setPaintStrokeColor(Color.RED);
                                    STROKE_COLOR = Color.RED;
                                    sc_button.setImageResource(R.drawable.strokecolor_red);
                                    break;
                                case 2:
                                    canvas.setPaintStrokeColor(Color.BLUE);
                                    STROKE_COLOR = Color.BLUE;
                                    sc_button.setImageResource(R.drawable.strokecolor_blue);
                                    break;
                                case 3:
                                    canvas.setPaintStrokeColor(Color.GREEN);
                                    STROKE_COLOR = Color.GREEN;
                                    sc_button.setImageResource(R.drawable.strokecolor_green);
                                    break;
                                default:
                                    canvas.setPaintStrokeColor(Color.BLACK);
                                    STROKE_COLOR = Color.BLACK;
                                    sc_button.setImageResource(R.drawable.strokecolor_black);
                            }
                            strokecolor_menu.setSelectedPosition(position);
                            strokecolor_menu.dismiss();
                        }
                    })
                    .build();
        }
        // display the popup menu
        strokecolor_menu.showAsAnchorLeftTop(view,0,-strokecolor_menu.getContentViewHeight());
    }

    // toggle eraser or drawing mode
    public void toggle_eraser(View view) {
        // select buttons to disable while in eraser mode
        ImageButton sw_button = findViewById(R.id.strokewidthButton);
        ImageButton sc_button = findViewById(R.id.strokecolorButton);
        ImageButton er_button = (ImageButton)view;
        // check for eraser mode
        if (ERASER_MODE) {
            canvas.setPaintStrokeColor(STROKE_COLOR);
            canvas.setPaintStrokeWidth(STROKE_WIDTH);
            canvas.setDrawer(DRAWING_MODE);
            er_button.setImageResource(R.drawable.eraser_off);
            sw_button.setEnabled(true);
            sc_button.setEnabled(true);
        }
        else {
            canvas.setPaintStrokeColor(canvas.getBaseColor());
            canvas.setPaintStrokeWidth(20);
            canvas.setDrawer(CanvasView.Drawer.PEN);
            er_button.setImageResource(R.drawable.eraser_on);
            sw_button.setEnabled(false);
            sc_button.setEnabled(false);
        }
        ERASER_MODE = !ERASER_MODE;
    }

    // undo canvas
    public void undo(View view) {
        canvas.undo();
    }

    // redo canvas
    public void redo(View view) {
        canvas.redo();
    }

    // clear canvas
    public void clear(View view) {
        canvas.clear();
        //canvas.setBaseColor(Color.RED);
    }

    // set drawing mode
    public void switch_mode(View view) {
        // select the image button
        ImageButton dm_button = (ImageButton) view;
        // create the popup menu
        // an set its properties
        if (drawingmode_menu == null) {
            drawingmode_menu = new PowerMenu.Builder(this)
                    .addItem(new PowerMenuItem("", R.drawable.pen_mode,true))
                    .addItem(new PowerMenuItem("", R.drawable.line_mode))
                    .addItem(new PowerMenuItem("", R.drawable.rectangle_mode))
                    .addItem(new PowerMenuItem("", R.drawable.ellipse_mode))
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(30f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextGravity(Gravity.CENTER)
                    .setWidth(170)
                    .setTextSize(15)
                    .setIconColor(Color.BLUE)
                    .setMenuColor(Color.WHITE)
                    .setTextColor(ContextCompat.getColor(this, R.color.purple_500))
                    .setSelectedTextColor(ContextCompat.getColor(this, R.color.purple_500))
                    .setSelectedMenuColor(Color.LTGRAY)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                        @Override
                        public void onItemClick(int position, PowerMenuItem item) {
                            switch (position) {
                                case 0:
                                    canvas.setDrawer(CanvasView.Drawer.PEN);
                                    dm_button.setImageResource(R.drawable.pen_mode);
                                    DRAWING_MODE = CanvasView.Drawer.PEN;
                                    break;
                                case 1:
                                    canvas.setDrawer(CanvasView.Drawer.LINE);
                                    dm_button.setImageResource(R.drawable.line_mode);
                                    DRAWING_MODE = CanvasView.Drawer.LINE;
                                    break;
                                case 2:
                                    canvas.setDrawer(CanvasView.Drawer.RECTANGLE);
                                    dm_button.setImageResource(R.drawable.rectangle_mode);
                                    DRAWING_MODE = CanvasView.Drawer.RECTANGLE;
                                    break;
                                case 3:
                                    canvas.setDrawer(CanvasView.Drawer.ELLIPSE);
                                    dm_button.setImageResource(R.drawable.ellipse_mode);
                                    DRAWING_MODE = CanvasView.Drawer.ELLIPSE;
                                    break;
                            }
                            drawingmode_menu.setSelectedPosition(position);
                            drawingmode_menu.dismiss();
                        }
                    })
                    .build();
        }
        // display the popup menu
        drawingmode_menu.showAsAnchorLeftTop(view,0,-drawingmode_menu.getContentViewHeight());
    }
}