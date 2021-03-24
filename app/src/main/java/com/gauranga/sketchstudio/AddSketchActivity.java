
package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

// GITHUB PROJECT LINK - https://github.com/Korilakkuma/CanvasView
// GITHUB PROJECT LINK - https://github.com/skydoves/PowerMenu#menu-effect
public class AddSketchActivity extends AppCompatActivity {

    CanvasView canvas;
    EditText title;
    PowerMenu drawingmode_menu;
    SpeechRecognizer speechRecognizer;
    Intent intentRecognizer;
    ImageButton dm_button;
    String IMG_FILE_SEP = "______";

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
        dm_button = findViewById(R.id.drawingmodeButton);

        // set the stroke color
        canvas.setPaintStrokeColor(Color.BLUE);
        // set the stroke width
        canvas.setPaintStrokeWidth(10);
        // set the background color
        canvas.setBaseColor(BACKGROUND_COLOR);

        setup_drawingmode_menu();
    }

    public void save_sketch(View view) {
        // get the title entered by the user
        String title_text = title.getText().toString();
        // get the current time
        String time = String.valueOf(System.currentTimeMillis());
        // get the image btmap from the canvas
        Bitmap bitmap = canvas.getBitmap();
        // check for empty title
        if (title_text.length()==0) {
            Toast toast = Toasty.custom(this, "TITLE EMPTY", R.drawable.error_icon, R.color.purple_500, 500, true, true);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, (int) canvas.getY()+canvas.getHeight());
            toast.show();
            return;
        }

        // save the image file
        ContextWrapper wrapper = new ContextWrapper(this);
        File file = wrapper.getDir("test", MODE_PRIVATE);
        File image_file = new File(file, time + IMG_FILE_SEP + title_text + ".jpg");
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
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        // set the layout file of the popup window
        View popupView = inflater.inflate(R.layout.sw_menu, null);
        // specify the height and width of the popup window
        int width = 700;
        int height = 150;
        // make inactive items outside of popup window
        boolean focusable = true;
        // create the popup window and set its properties
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        // set the location of the popup window
        popupWindow.showAsDropDown(view, 50, -50);

        // display stroke width
        TextView sw_text = popupView.findViewById(R.id.sw_text);
        sw_text.setText(String.valueOf(STROKE_WIDTH));
        // seekbar for changing stroke width
        SeekBar sw_seekbar = popupView.findViewById(R.id.sw_seekbar);
        sw_seekbar.setMin(1);
        sw_seekbar.setMax(50);
        sw_seekbar.setProgress(STROKE_WIDTH);
        sw_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sw_text.setText(String.valueOf(progress));
                STROKE_WIDTH = progress;
                canvas.setPaintStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // change the stroke color
    public void change_strokecolor(View view) {
        // use color picker dialog
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose stroke color")
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(null)
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        STROKE_COLOR = selectedColor;
                        canvas.setPaintStrokeColor(selectedColor);
                        ImageButton sc_button = (ImageButton) view;
                    }
                })
                .setNegativeButton("cancel", null)
                .build()
                .show();
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
        // display the popup menu
        drawingmode_menu.showAsAnchorLeftTop(view,0,-drawingmode_menu.getContentViewHeight());
    }
    // setup drawing mode menu
    public void setup_drawingmode_menu() {
        // create the popup menu
        // an set its properties
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

    // set the background color
    public void set_background(View view) {
        // use color picker dialog
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose background color")
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(null)
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        canvas.setBaseColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", null)
                .build()
                .show();
    }

    // use voice commands
    public void voice_command(View view) {
        // initialize object
        SpeechCommand command = new SpeechCommand();
        // add permissions in the manifest file
        // check for permissions from the user
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
        }

        // create the speech recognizer object
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        // create an intent to recognize the speech
        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        // set a speech recognition listener
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}
            // function is called when the user starts speaking
            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {}

            @Override
            public void onResults(Bundle results) {
                // get the recognized speech
                ArrayList<String> result_list = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String speech =  result_list.get(0);
                // display the recognized speech
                Toast.makeText(getApplicationContext(), speech, Toast.LENGTH_SHORT).show();
                // detect appropriate command
                String ct = command.command_type(speech);
                int cv = command.command_value(speech);
                // execute the command
                if (ct.equals("drawing_mode")) {
                    switch (cv) {
                        case 0:
                            canvas.setDrawer(CanvasView.Drawer.PEN);
                            dm_button.setImageResource(R.drawable.pen_mode);
                            drawingmode_menu.setSelectedPosition(cv);
                            DRAWING_MODE = CanvasView.Drawer.PEN;
                            break;
                        case 1:
                            canvas.setDrawer(CanvasView.Drawer.LINE);
                            dm_button.setImageResource(R.drawable.line_mode);
                            drawingmode_menu.setSelectedPosition(cv);
                            DRAWING_MODE = CanvasView.Drawer.LINE;
                            break;
                        case 2:
                            canvas.setDrawer(CanvasView.Drawer.RECTANGLE);
                            dm_button.setImageResource(R.drawable.rectangle_mode);
                            drawingmode_menu.setSelectedPosition(cv);
                            DRAWING_MODE = CanvasView.Drawer.RECTANGLE;
                            break;
                        case 3:
                            canvas.setDrawer(CanvasView.Drawer.ELLIPSE);
                            dm_button.setImageResource(R.drawable.ellipse_mode);
                            drawingmode_menu.setSelectedPosition(cv);
                            DRAWING_MODE = CanvasView.Drawer.ELLIPSE;
                            break;
                    }
                }
                else if (ct.equals("modify_canvas")) {
                    switch (cv) {
                        case 0:
                            canvas.undo();
                            break;
                        case 1:
                            canvas.redo();
                            break;
                        case 2:
                            canvas.clear();
                            break;
                    }
                }
                else if (ct.equals("eraser")) {
                    // select buttons to disable while in eraser mode
                    ImageButton sw_button = findViewById(R.id.strokewidthButton);
                    ImageButton sc_button = findViewById(R.id.strokecolorButton);
                    ImageButton er_button = findViewById(R.id.eraserButton);
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
                else if (ct.equals("background")) {
                    if (cv != -1) {
                        canvas.setBaseColor(cv);
                    }
                    else {
                        Toast toast = Toasty.custom(getApplicationContext(), "INVALID COLOR", R.drawable.error_icon, R.color.purple_500, 500, true, true);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, (int) canvas.getY()+canvas.getHeight());
                        toast.show();
                    }
                }
                else if (ct.equals("brush_width")) {
                    STROKE_WIDTH = cv;
                    canvas.setPaintStrokeWidth(cv);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        // start the speech recognition
        speechRecognizer.startListening(intentRecognizer);
    }
}