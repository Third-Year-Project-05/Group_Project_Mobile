package com.example.echolynk.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;



import com.example.echolynk.R;
import com.example.echolynk.Utils.RecycleViewCustomItemDirection;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.AnswerAdapter;
import com.example.echolynk.View.Adapter.LiveUserAdapter;
import com.example.echolynk.View.Adapter.SenderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class LiveConversationChat extends AppCompatActivity implements onClickListener {


    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    RecyclerView chatRecycleView;
    RecyclerView suggestion_answer;
    private EditText massageBox;
    private ImageButton keyboardBtn;
    private ImageButton mikeBtn;
    private ImageButton pauseBtn;
    private ImageButton closeBtn;

    private static final int REQUEST_AUDIO_PERMISSION_CODE=1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_conversation_chat);


        relativeLayout=findViewById(R.id.live_conversation_chat);
        recyclerView = findViewById(R.id.live_users);
        chatRecycleView = findViewById(R.id.chat_view);
        suggestion_answer=findViewById(R.id.suggestion_answers);
        keyboardBtn=findViewById(R.id.keyboard_icon);
        mikeBtn=findViewById(R.id.speech_icon);
        pauseBtn=findViewById(R.id.pause_icon);
        closeBtn=findViewById(R.id.close_icon);
        massageBox=findViewById(R.id.write_massage);



        List<Integer> live_users = new ArrayList<>();
        List<String> massageList = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();



        // set suggestions
        setSuggestions(suggestions,suggestion_answer);



        // set data for live users icon tab
        setLiveUsers(live_users,recyclerView);



        // set data for live chat view
        setLiveChat(chatRecycleView,massageList);


        // check the audio recoding permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION_CODE);
        }

        mikeBtn.setOnClickListener(view -> {
            promptSpeechInput();
        });


        keyboardBtn.setOnClickListener(view -> {
            massageBox.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(massageBox, InputMethodManager.SHOW_IMPLICIT);
            }
        });


    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Speech not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String spokenText = result.get(0);
                    // Do something with the spoken text
                    Toast.makeText(this, spokenText, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void setSuggestions(List<String> suggestions, RecyclerView suggestionAnswer) {
        suggestions.add("Yes");
        suggestions.add("No");
        if (suggestionAnswer!=null) {
            suggestionAnswer.setAdapter(new AnswerAdapter(getApplicationContext(),suggestions,this));
        }
    }



    private void setLiveChat(RecyclerView chatRecycleView, List<String> massageList) {
        massageList.add("abc");
        massageList.add("10");
        massageList.add("safafa");
        massageList.add("abcasdsdasd");

        if (chatRecycleView != null) {
            chatRecycleView.addItemDecoration(new RecycleViewCustomItemDirection(116));
            chatRecycleView.setAdapter(new SenderAdapter(getApplicationContext(), massageList));
            // chatRecycleView.setAdapter(new ReceiverAdapter(getApplicationContext(),massageList));
        }
    }

    private void setLiveUsers(List<Integer> live_users, RecyclerView recyclerView) {
        live_users.add(R.drawable.dummy_user_image);
        live_users.add(R.drawable.dummyprofileimage);
        live_users.add(R.drawable.dummy_blog_img2);
        live_users.add(R.drawable.facebook);
        live_users.add(R.drawable.dummy_user_image);
        live_users.add(R.drawable.dummy_user_image);
        live_users.add(R.drawable.dummy_user_image);

        if (recyclerView != null) {
            recyclerView.setAdapter(new LiveUserAdapter(getApplicationContext(), live_users, this));
        }
    }

    @Override
    public void onClick(int position, View view) {
        //set up on click function using live users
    }

}