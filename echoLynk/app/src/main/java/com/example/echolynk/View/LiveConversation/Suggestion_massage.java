package com.example.echolynk.View.LiveConversation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.echolynk.Model.MassageModel;
import com.example.echolynk.R;

public class Suggestion_massage extends AppCompatActivity {
    private final LiveConversationChat liveConversationChat=new LiveConversationChat();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggestion_massage);


    }

}