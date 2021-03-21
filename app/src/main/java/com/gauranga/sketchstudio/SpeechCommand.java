package com.gauranga.sketchstudio;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpeechCommand {

    Map<String,Integer> color_codes = Stream.of(new Object[][] {
            {"light blue", Color.parseColor("#ADD8E6")},
            {"blue", Color.BLUE},
            {"red", Color.RED},
            {"orange", Color.parseColor("#FFA500")},
            {"brown", Color.parseColor("#A52A2A")},
            {"light green", Color.parseColor("#90EE90")},
            {"dark green", Color.parseColor("#006400")},
            {"green", Color.GREEN},
            {"light grey", Color.parseColor("#D3D3D3")},
            {"grey", Color.GRAY},
            {"yellow", Color.YELLOW},
            {"pink", Color.parseColor("#FFC0CB")},
            {"purple", Color.parseColor("#800080")},
            {"magenta", Color.MAGENTA},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    Map<String,Integer> dm_codes = Stream.of(new Object[][] {
            {"brush", 0},
            {"line", 1},
            {"rectangle", 2},
            {"circle", 3},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    Map<String,Integer> mc_codes = Stream.of(new Object[][] {
            {"undo", 0},
            {"redo", 1},
            {"clear", 2},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    // detect the type of command
    String command_type(String speech) {
        String[] words = speech.toLowerCase().split(" ");
        HashSet<String> word_set = new HashSet<>(Arrays.asList(words));

            if (word_set.contains("thickness") || word_set.contains("width")) {
                // change stroke width
                return "brush_width";
            }
            else if (word_set.contains("brush") || word_set.contains("line") || word_set.contains("rectangle") || word_set.contains("circle")) {
                // change the drawing mode
                return "drawing_mode";
            }
            else if (word_set.contains("undo") || word_set.contains("redo") || word_set.contains("clear")) {
                // undo/redo/clear canvas
                return "modify_canvas";
            }
            else if (word_set.contains("eraser") || word_set.contains("rubber")) {
                // toggle eraser mode
                return "eraser";
            }
            else if (word_set.contains("background")) {
                // change background image
                return "background";
            }

        return "";
    }

    // command value
    int command_value(String speech) {
        String[] words = speech.toLowerCase().split(" ");
        HashSet<String> word_set = new HashSet<>(Arrays.asList(words));

        if (word_set.contains("thickness") || word_set.contains("width")) {
            for (String nw : word_set) {
                try {
                    int sw = Integer.parseInt(nw);
                    return Math.min(sw, 50);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (word_set.contains("brush") || word_set.contains("line") || word_set.contains("rectangle") || word_set.contains("circle")) {
            for (String dm : word_set) {
                if (dm_codes.containsKey(dm)) {
                    return dm_codes.get(dm);
                }
            }
        }
        else if (word_set.contains("undo") || word_set.contains("redo") || word_set.contains("clear")) {
            for (String mc : word_set) {
                if (mc_codes.containsKey(mc)) {
                    return mc_codes.get(mc);
                }
            }
        }
        else if (word_set.contains("background")) {
            ArrayList<String> colors = new ArrayList<>(Arrays.asList("light blue",
                                                                     "blue",
                                                                     "orange",
                                                                     "brown",
                                                                     "red",
                                                                     "light green",
                                                                     "dark green",
                                                                     "green",
                                                                     "light grey",
                                                                     "grey",
                                                                     "yellow",
                                                                     "pink",
                                                                     "purple",
                                                                     "magenta"));
            for (String cc : colors) {
                if (speech.toLowerCase().contains(cc)) {
                    return color_codes.get(cc);
                }
            }
        }

        return -1;
    }
}
