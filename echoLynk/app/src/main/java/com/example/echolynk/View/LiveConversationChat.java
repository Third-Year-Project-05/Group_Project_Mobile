package com.example.echolynk.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.echolynk.R;
import com.example.echolynk.Utils.RecycleViewCustomItemDirection;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.AnswerAdapter;
import com.example.echolynk.View.Adapter.LiveUserAdapter;
import com.example.echolynk.View.Adapter.ReceiverAdapter;
import com.example.echolynk.View.Adapter.SenderAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LiveConversationChat extends AppCompatActivity implements onClickListener {


    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    RecyclerView chatRecycleView;
    RecyclerView suggestion_answer;
    private EditText massageBox;
    private SpeechRecognizer speechRecognizer;
    private ImageButton keyboardBtn;
    private ImageButton mikeBtn;
    private ImageButton pauseBtn;
    private ImageButton closeBtn;
    private boolean isRecording = false;
    AlertDialog.Builder alertSpeechDialog;
    AlertDialog alertDialog;


    private static final int RecodeAudioRequestCode = 1;


    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_conversation_chat);


        relativeLayout = findViewById(R.id.live_conversation_chat);
        recyclerView = findViewById(R.id.live_users);
        chatRecycleView = findViewById(R.id.chat_view);
        suggestion_answer = findViewById(R.id.suggestion_answers);
        keyboardBtn = findViewById(R.id.keyboard_icon);
        mikeBtn = findViewById(R.id.speech_icon);
        pauseBtn = findViewById(R.id.pause_icon);
        closeBtn = findViewById(R.id.close_icon);
        massageBox = findViewById(R.id.write_massage);


        List<Integer> live_users = new ArrayList<>();
        List<String> massageList = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();


        // set suggestions
        setSuggestions(suggestions, suggestion_answer);


        // set data for live users icon tab
        setLiveUsers(live_users, recyclerView);

        // click keyboard btn
        keyboardBtn.setOnClickListener(view -> {
            massageBox.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(massageBox, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        // set data for live chat view
      //  setLiveChat(chatRecycleView, massageList,0);

        //sender click the sent Btn
        massageBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP){
                    Drawable drawableEnd=massageBox.getCompoundDrawables()[2];

                    if (drawableEnd!=null){
                        int drawableWidth=drawableEnd.getBounds().width();
                        if (event.getRawX() >= (massageBox.getRight() - drawableWidth)) {
                            // handel the sender massage
                            String massage=massageBox.getText().toString();
                            if (!massage.isEmpty()) {
                                massageList.add(massage);
                                setLiveChat(chatRecycleView,massageList,0);
                                return true;
                            }
                        }
                    }
                }

                return false;
            }
        });


        // check the audio recoding permission
        if (ContextCompat.checkSelfPermission(LiveConversationChat.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }


        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                /*ViewGroup viewGroup=findViewById(android.R.id.content);
                View dialogView= LayoutInflater.from(LiveConversationChat.this).inflate(R.layout.alert_custom,viewGroup,false);*/

                /*alertSpeechDialog=new AlertDialog.Builder(LiveConversationChat.this);
                alertSpeechDialog.setMessage("Listening....");
                alertSpeechDialog.setView(dialogView);
                alertDialog=alertSpeechDialog.create();
                alertDialog.show();*/
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> arrayList=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                massageList.add(arrayList.get(0));
                setLiveChat(chatRecycleView, massageList,1);

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        //click the mike btn
        mikeBtn.setOnClickListener(view -> {
            speechRecognizer.startListening(speechIntent);
        });

        closeBtn.setOnClickListener(view -> {
            speechRecognizer.stopListening();
        });


    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecodeAudioRequestCode);
        }
    }




    private void setLiveChat (RecyclerView chatRecycleView, List < String > massageList,int type){

        // type 0 is sender and type 1 is receiver

        if (chatRecycleView != null) {

            if (type==1){
                chatRecycleView.setAdapter(new ReceiverAdapter(getApplicationContext(),massageList));
            }else {
                chatRecycleView.addItemDecoration(new RecycleViewCustomItemDirection(300));
                chatRecycleView.setAdapter(new SenderAdapter(getApplicationContext(), massageList));
            }
        }
    }

    private void setLiveUsers (List < Integer > live_users, RecyclerView recyclerView){
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

    private void setSuggestions (List < String > suggestions, RecyclerView suggestionAnswer){
        suggestions.add("Yes");
        suggestions.add("No");
        suggestions.add("No");
        if (suggestionAnswer != null) {
            suggestionAnswer.setAdapter(new AnswerAdapter(getApplicationContext(), suggestions, this));
        }
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecodeAudioRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(this, "Audio Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(int position, View view) {

    }
}





