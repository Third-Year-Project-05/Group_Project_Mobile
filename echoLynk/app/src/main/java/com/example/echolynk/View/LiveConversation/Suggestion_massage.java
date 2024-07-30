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
    private Button suggestionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggestion_massage);

        suggestionBtn=findViewById(R.id.answer);

    }

    public void sendSuggestion(View view) {
        Log.d("sugesstion click", "onBindViewHolder: click wenwa ");
        liveConversationChat.massageList.add(new MassageModel(suggestionBtn.getText().toString(),0));
        liveConversationChat.setUpLiveChat(liveConversationChat.chatRecycleView, liveConversationChat.massageList);
    }
}