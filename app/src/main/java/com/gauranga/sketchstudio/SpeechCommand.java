package com.gauranga.sketchstudio;

public class SpeechCommand {

    // detect the type of command
    String command_type(String speech) {
        String[] words = speech.split(" ");

        for (String w : words) {
            if (w.equals("pen") || w.equals("line") || w.equals("rectangle") || w.equals("circle")) {
                return "drawing_mode";
            }
        }

        return "";
    }

    // command value
    int command_value(String speech) {
        String[] words = speech.split(" ");

        for (String w : words) {
            if (w.equals("pen")) {
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
        }

        return -1;
    }
}
