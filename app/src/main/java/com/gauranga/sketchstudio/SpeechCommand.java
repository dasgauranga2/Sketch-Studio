package com.gauranga.sketchstudio;

public class SpeechCommand {

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
        }

        return "";
    }

    // command value
    int command_value(String speech) {
        String[] words = speech.split(" ");

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
        }

        return -1;
    }
}
