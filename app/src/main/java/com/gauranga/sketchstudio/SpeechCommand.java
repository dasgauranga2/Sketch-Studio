package com.gauranga.sketchstudio;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpeechCommand {

    Map<String,Integer> color_codes = Stream.of(new Object[][] {
            {"blue", Color.BLUE},
            {"red", Color.RED},
            {"green", Color.GREEN},
            {"gray", Color.GRAY},
            {"yellow", Color.YELLOW},
            {"magenta", Color.MAGENTA},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    // detect the type of command
    String command_type(String speech) {
        String[] words = speech.toLowerCase().split(" ");

        for (String w : words) {
            if (w.equals("brush") || w.equals("line") || w.equals("rectangle") || w.equals("circle")) {
                // change the drawing mode
                return "drawing_mode";
            }
            else if (w.equals("undo") || w.equals("redo") || w.equals("clear")) {
                // undo/redo/clear canvas
                return "modify_canvas";
            }
            else if (w.equals("eraser")) {
                // toggle eraser mode
                return "eraser";
            }
            else if (w.equals("background")) {
                // toggle eraser mode
                return "background";
            }
        }

        return "";
    }

    // command value
    int command_value(String speech) {
        String[] words = speech.toLowerCase().split(" ");

        for (String w : words) {
            if (w.equals("brush")) {
                return 0;
            }
            else if (w.equals("line")) {
                return 1;
            }
            else if (w.equals("rectangle")) {
                return 2;
            }
            else if (w.equals("circle")) {
                return 3;
            }
            else if (w.equals("undo")) {
                return 0;
            }
            else if (w.equals("redo")) {
                return 1;
            }
            else if (w.equals("clear")) {
                return 2;
            }
            else if (color_codes.containsKey(w)) {
                return color_codes.get(w);
            }
        }

        return -1;
    }
}
