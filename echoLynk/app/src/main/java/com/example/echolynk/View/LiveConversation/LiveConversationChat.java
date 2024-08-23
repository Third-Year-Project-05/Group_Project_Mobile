package com.example.echolynk.View.LiveConversation;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.echolynk.Model.LiveChatRoomModel;
import com.example.echolynk.Model.MassageModel;
import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.DB.DBHelper;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.AnswerAdapter;
import com.example.echolynk.View.Adapter.LiveUserAdapter;
import com.example.echolynk.View.Adapter.ReceiverAdapter;
import com.example.echolynk.View.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class LiveConversationChat extends AppCompatActivity implements onClickListener {

    // current user ge persona details tika ganna one
    // sql light dala history eka ganna one


    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private LinearLayout massageLayout;
    private RecyclerView chatRecycleView;
    private RecyclerView suggestion_answer;
    private EditText massageBox;
    private SpeechRecognizer speechRecognizer;
    private ImageButton keyboardBtn, sendButton;
    private ImageButton mikeBtn,muteMike;
    private ImageButton pauseBtn;
    private ImageButton closeBtn;
    private ProgressBar progressBar;
    private List<MassageModel> massageList = new ArrayList<>();
    private List<String> suggestions = new ArrayList<>();
    private static final int RecodeAudioRequestCode = 1;
    private TextToSpeech tts;
    private UserModel user;
    private FirebaseFirestore db;

    private String conversationStartTime;

    private DBHelper dbHelper=new DBHelper(LiveConversationChat.this);
    private static final String endPoint = "https://python-backend-8k9v-oushx2d3n-dilum-induwaras-projects.vercel.app/predict/";
//    ApiServices apiServices = ApiClient.getInstance().create(ApiServices.class);

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_conversation_chat);

        //initialize cloud firestore
        db = FirebaseFirestore.getInstance();
        conversationStartTime=getCurrentTime();

        massageLayout = findViewById(R.id.sender_massage_laout);
        relativeLayout = findViewById(R.id.live_conversation_chat);
        recyclerView = findViewById(R.id.live_users);
        chatRecycleView = findViewById(R.id.chat_view);
        suggestion_answer = findViewById(R.id.suggestion_answers);
        keyboardBtn = findViewById(R.id.keyboard_icon);
        mikeBtn = findViewById(R.id.speech_icon);
        muteMike = findViewById(R.id.speech_icon_mute);
        pauseBtn = findViewById(R.id.pause_icon);
        closeBtn = findViewById(R.id.close_icon);
        massageBox = findViewById(R.id.write_massage);
        sendButton = findViewById(R.id.send_massage);
        progressBar = findViewById(R.id.progress_bar);

        List<Integer> live_users = new ArrayList<>();

        // Initialize current user
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    user = task.getResult().toObject(UserModel.class);
                }
            }
        });

        // Initialize TextToSpeech
        tts = new TextToSpeech(LiveConversationChat.this, this::onInit);


        pauseBtn.setOnClickListener(view -> {});


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


        //sender click the sent Btn

        sendButton.setOnClickListener(view -> {
            String massage = massageBox.getText().toString();
            if (!massage.isEmpty()) {
                massageList.add(new MassageModel(massage, 0));
                setUpLiveChat(chatRecycleView, massageList);
                textToSpeech(massage);
                massageBox.setText("");
            }
        });


        // check the audio recoding permission
        if (ContextCompat.checkSelfPermission(LiveConversationChat.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(LiveConversationChat.this);
        final Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {

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

                muteMike.setVisibility(View.GONE);
                mikeBtn.setVisibility(View.VISIBLE);

                ArrayList<String> arrayList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                massageList.add(new MassageModel(arrayList.get(0), 1));

                setUpLiveChat(chatRecycleView, massageList);

                getSuggestions(arrayList.get(0));


            }

            @Override
            public void onPartialResults(Bundle bundle) {
                /*ArrayList<String> partialMatches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (partialMatches != null && !partialMatches.isEmpty()) {
                    String partialText = partialMatches.get(0);
                    Log.d(TAG, "onPartialResults: " + partialText);
                    // Handle the partial recognized text here
                }*/
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        //click the mike btn
        mikeBtn.setOnClickListener(view -> {
            mikeBtn.setVisibility(View.GONE);
            muteMike.setVisibility(View.VISIBLE);
            speechRecognizer.startListening(speechIntent);
        });

        closeBtn.setOnClickListener(view -> {



            LiveChatRoomModel conversation=null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate currentDate = LocalDate.now();

              //  conversation = new LiveChatRoomModel(currentDate,conversationStartTime,getCurrentTime(),massageList,massageList.get(massageList.size()-1).getMassage());
                if (dbHelper.insertConversation(currentDate.toString(),conversationStartTime,getCurrentTime(),"title",massageList.get(massageList.size()-1).getMassage(),massageList)) {
                    Log.d(TAG, "Success the save the massage list");
                }else {
                    Log.d(TAG, "Unsaved the massage list");
                }
            }

        });

    }

    private String getCurrentTime() {

        // Specify the time zone for Sri Lanka
        ZoneId zoneId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zoneId = ZoneId.of("Asia/Colombo");
        }

        // Get the current date and time in the specified time zone
        ZonedDateTime zonedDateTime = null;
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zonedDateTime = ZonedDateTime.now(zoneId);
            formatter = DateTimeFormatter.ofPattern("hh:mm a");
            return zonedDateTime.format(formatter);
        }

        return null;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                // btnSpeak.setEnabled(true);
            }
        } else {
            Toast.makeText(this, "Initialization Failed!", Toast.LENGTH_SHORT).show();
        }
    }


    private void getSuggestions(String massage) {

        suggestions.clear();
        progressBar.setVisibility(View.VISIBLE);
        suggestion_answer.setVisibility(View.GONE);

        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject1.put("role", "user");
            jsonObject1.put("content", "How old are you?");
            jsonObject2.put("name", "tharindu dakshina");
            jsonObject2.put("email", "tharindudakshina527@gmail.com");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONArray history = new JSONArray();
        history.put(jsonObject1);

        JSONArray personalData = new JSONArray();
        personalData.put(jsonObject2);

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("question", massage);
            paramObject.put("history", history);
            paramObject.put("personal_data", personalData);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, endPoint, paramObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display the response

                        if (response == null) {
                            progressBar.setVisibility(View.GONE);
                            suggestion_answer.setVisibility(View.VISIBLE);
                            Toast.makeText(LiveConversationChat.this, "Response is null", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                progressBar.setVisibility(View.GONE);
                                suggestion_answer.setVisibility(View.VISIBLE);
                                JSONArray suggetions = response.getJSONArray("suggetions");
                                for (int i = 0; i < suggetions.length(); i++) {
                                    String suggestion = suggetions.getString(i);
                                    suggestions.add(suggestion);
                                    setSuggestions(suggestions, suggestion_answer);

                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Request to GPT-3 error: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
                suggestion_answer.setVisibility(View.VISIBLE);
                Toast.makeText(LiveConversationChat.this, "Response is "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> mapHeaders = new HashMap<>();

                mapHeaders.put("Content-Type", "application/json");
                mapHeaders.put("x-vercel-protection-bypass", "WzZBi8VJg6JKhYQXapGCThQhaaNWicLQ");

                return mapHeaders;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }

        };

        int timeOutPeriod = 60000;

        RetryPolicy policy = new DefaultRetryPolicy(
                timeOutPeriod,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );

        jsonObjectRequest.setRetryPolicy(policy);

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecodeAudioRequestCode);
        }
    }

    public void setUpLiveChat(RecyclerView chatRecycleView, List<MassageModel> massageList) {
        chatRecycleView.setAdapter(new ReceiverAdapter(getApplicationContext(), massageList));

    }

    private void setLiveUsers(List<Integer> live_users, RecyclerView recyclerView) {
        live_users.add(R.drawable.user_profile_pic);
        live_users.add(R.drawable.user_profile_pic);
        live_users.add(R.drawable.user_profile_pic);
        live_users.add(R.drawable.user_profile_pic);
        live_users.add(R.drawable.user_profile_pic);

        if (recyclerView != null) {
            recyclerView.setAdapter(new LiveUserAdapter(getApplicationContext(), live_users, this));
        }
    }

    private void setSuggestions(List<String> suggestions, RecyclerView suggestionAnswer) {
        if (suggestionAnswer != null) {
            suggestionAnswer.setAdapter(new AnswerAdapter(getApplicationContext(), suggestions, this));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

        if (view instanceof Button) {
            Button button = (Button) view;
            String buttonText = button.getText().toString();
            massageList.add(new MassageModel(buttonText, 0));
            setUpLiveChat(chatRecycleView, massageList);
            textToSpeech(buttonText);
            suggestions.clear();
            setSuggestions(suggestions, suggestion_answer);
        }

    }

    public void textToSpeech(String text) {

        float pitch = (1.0f);
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (9.5f / 10.0f);
        if (speed < 0.1) speed = 0.1f;  // Minimum speech rate value

        tts.setPitch(pitch);
        tts.setSpeechRate(speed);

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        // Shutdown TTS when the activity is destroyed
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


}





