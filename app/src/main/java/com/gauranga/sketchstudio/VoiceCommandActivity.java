package com.gauranga.sketchstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class VoiceCommandActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_command);

        recyclerView = findViewById(R.id.voiceCommandsRecyclerView);

        String[] titles = {"Background color",
                           "Drawing mode",
                           "Brush thickness",
                           "Modify Canvas",
                           "Eraser mode"};

        String[] descriptions = {"Change background color to red",
                                 "Change drawing mode to circle",
                                 "Change brush thickness to 17",
                                 "Undo/Redo/Clear",
                                 "Switch to eraser mode"};

        VoiceCommandAdapter voiceCommandAdapter = new VoiceCommandAdapter(this, titles, descriptions);
        recyclerView.setAdapter(voiceCommandAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}