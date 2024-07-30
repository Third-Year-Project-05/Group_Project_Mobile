package com.example.echolynk.View.LiveConversation;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.example.echolynk.Model.MassageModel;
import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.AnswerAdapter;
import com.example.echolynk.View.Adapter.LiveUserAdapter;
import com.example.echolynk.View.Adapter.ReceiverAdapter;
import com.example.echolynk.View.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public RecyclerView chatRecycleView;
    private RecyclerView suggestion_answer;
    private EditText massageBox;
    private SpeechRecognizer speechRecognizer;
    private ImageButton keyboardBtn,sendButton;
    private ImageButton mikeBtn;
    private ImageButton pauseBtn;
    private ImageButton closeBtn;
    private ProgressBar progressBar;
    public List<MassageModel> massageList = new ArrayList<>();
    private List<String> suggestions = new ArrayList<>();
    private static final int RecodeAudioRequestCode = 1;
    private TextToSpeech tts;
    private UserModel user;
    private static final String endPoint= "https://python-backend-8k9v-oushx2d3n-dilum-induwaras-projects.vercel.app/predict/";
//    ApiServices apiServices = ApiClient.getInstance().create(ApiServices.class);

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_conversation_chat);

        massageLayout=findViewById(R.id.sender_massage_laout);
        relativeLayout = findViewById(R.id.live_conversation_chat);
        recyclerView = findViewById(R.id.live_users);
        chatRecycleView = findViewById(R.id.chat_view);
        suggestion_answer = findViewById(R.id.suggestion_answers);
        keyboardBtn = findViewById(R.id.keyboard_icon);
        mikeBtn = findViewById(R.id.speech_icon);
        pauseBtn = findViewById(R.id.pause_icon);
        closeBtn = findViewById(R.id.close_icon);
        massageBox = findViewById(R.id.write_massage);
        sendButton = findViewById(R.id.send_massage);
        progressBar=findViewById(R.id.progress_bar);

        List<Integer> live_users = new ArrayList<>();
        // Initialize current user
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    user=task.getResult().toObject(UserModel.class);
                }
            }
        });

        // Initialize TextToSpeech
        tts = new TextToSpeech(LiveConversationChat.this, this::onInit);



        pauseBtn.setOnClickListener(view -> {

            /*ArrayList<HistoryMassage> history=new ArrayList<>();
            ArrayList<PersonalData> personalData=new ArrayList<>();

            history.add(new HistoryMassage("user","How old are you?"));
            history.add(new HistoryMassage("assistant","I am 30 years old."));

            personalData.add(new PersonalData("Saman"));

           // SpeechToTextMassage massage = new SpeechToTextMassage("what is your name", history, personalData);


            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            try {
                jsonObject1.put("role","user");
                jsonObject1.put("role","assistant");
                jsonObject1.put("content","How old are you?");
                jsonObject1.put("content","I am 30 years old.");
                jsonObject2.put("name","sdad");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject1);

            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.put(jsonObject2);

            JSONObject paramObject = new JSONObject();
            try {
                paramObject.put("question","what is your name");
                paramObject.put("history",jsonArray);
                paramObject.put("personal_data",jsonArray2);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.d("request one", paramObject.toString());

            Handler handler = new Handler(Looper.getMainLooper());

            Call<ResponseBody> call=apiServices.postSpeechToTextMassage(paramObject);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    Log.d("request----1", "request: "+response.message()+" "+response.code());
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        Log.d("response ok", "Created User ID: " + body.toString());
                    }else {
                        Log.d("response not", "Created User ID: "+response.body() );
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("response failed", "Created User ID: " + t.getLocalizedMessage());
                    Log.d("response failed", "Created User ID: " + t.getMessage());
                    Log.d("response failed", "Created User ID: " + call.isCanceled());
                    Log.d("response failed", "Created User ID: " + call.isExecuted());
                    Log.d("response failed", "Created User ID: " + call.timeout());
                    Log.d("response failed", "Created User ID: " + call.request().body());
                }
            });

           // handler.postDelayed(()->,6000);
            */

        });


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

        sendButton.setOnClickListener(view -> {
            String massage=massageBox.getText().toString();
            if (!massage.isEmpty()) {
                massageList.add(new MassageModel(massage,0));
                setUpLiveChat(chatRecycleView,massageList);
                textToSpeech(massage);
                massageBox.setText("");
            }
        });



        // check the audio recoding permission
        if (ContextCompat.checkSelfPermission(LiveConversationChat.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }


        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(LiveConversationChat.this);
        final Intent speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
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

                massageList.add(new MassageModel(arrayList.get(0),1));

                setUpLiveChat(chatRecycleView, massageList);

                setndPostMethod(arrayList.get(0));


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
            Intent intent=new Intent(LiveConversationChat.this, MainActivity.class);
            intent.putExtra("load_fragment","speech");
            startActivity(intent);
        });

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


    private void setndPostMethod(String massage) {

        suggestions.clear();
        progressBar.setVisibility(View.VISIBLE);
        suggestion_answer.setVisibility(View.GONE);

            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2 = new JSONObject();
            try {
                jsonObject1.put("role","user");
                jsonObject1.put("content","How old are you?");
                jsonObject2.put("name",user.getUserName());
                jsonObject2.put("email",user.getEmail());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject1);

            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.put(jsonObject2);

            JSONObject paramObject = new JSONObject();
            try {
                paramObject.put("question", massage);
                paramObject.put("history",jsonArray);
                paramObject.put("personal_data",jsonArray2);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Log.d(TAG, "Request to GPT-3: " + paramObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, endPoint, paramObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display the response

                            if (response==null ) {
                                progressBar.setVisibility(View.GONE);
                                suggestion_answer.setVisibility(View.VISIBLE);
                                Toast.makeText(LiveConversationChat.this, "Response is null",Toast.LENGTH_SHORT).show();
                            }else {
                                try {
                                    progressBar.setVisibility(View.GONE);
                                    suggestion_answer.setVisibility(View.VISIBLE);
                                    JSONArray suggetions = response.getJSONArray("suggetions");
                                    for (int i = 0; i < suggetions.length(); i++) {
                                        String suggestion = suggetions.getString(i);
                                        suggestions.add(suggestion);
                                        setSuggestions(suggestions,suggestion_answer);

                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Log.d(TAG, "Request to GPT-3: " + response);
                            }


                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Request to GPT-3: " + error.getMessage());

                }
            }){
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
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecodeAudioRequestCode);
        }
    }

    public void setUpLiveChat (RecyclerView chatRecycleView, List<MassageModel> massageList){

        // type 0 is sender and type 1 is receiver

        chatRecycleView.setAdapter(new ReceiverAdapter(getApplicationContext(),massageList));

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

        if (view instanceof Button) {
            Button button = (Button) view;
            String buttonText = button.getText().toString();
            massageList.add(new MassageModel(buttonText,0));
            setUpLiveChat(chatRecycleView, massageList);
            textToSpeech(buttonText);
            suggestions.clear();
            setSuggestions(suggestions,suggestion_answer);
        }

    }

    public void textToSpeech(String text){

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





