package com.example.echolynk.View.LiveConversation;

import static android.content.ContentValues.TAG;

import static com.example.echolynk.Utils.FirebaseUtils.isUserPremium;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.example.echolynk.Model.ConversationModel;
import com.example.echolynk.Model.MassageModel;
import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.Conversations;
import com.example.echolynk.Utils.DB.DBHelper;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.Utils.ImageGenerator;
import com.example.echolynk.Utils.PaymentMethod;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.AnswerAdapter;
import com.example.echolynk.View.Adapter.DifficultWordsAdapter;
import com.example.echolynk.View.Adapter.LiveUserAdapter;
import com.example.echolynk.View.Adapter.ReceiverAdapter;
import com.example.echolynk.View.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private TextView save_prompt, conversation_title_text;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private LinearLayout massageLayout;
    private RecyclerView chatRecycleView, difficultWordsRecycleView;
    private RecyclerView suggestion_answer;
    private EditText massageBox, conversationTitle;
    private SpeechRecognizer speechRecognizer;
    private ImageButton keyboardBtn, sendButton;
    private ImageButton mikeBtn, muteMike;
    private ImageButton closeBtn;
    private Button conversationYesBtn, conversationNoBtn, conversationSaveBtn;
    private ProgressBar progressBar, progressBar2;
    private List<MassageModel> massageList = new ArrayList<>();
    private List<String> suggestions = new ArrayList<>();
    private static final int RecodeAudioRequestCode = 1;
    private TextToSpeech tts;
    private UserModel user;
    private FirebaseFirestore db;
    private String conversationStartTime;
    private Dialog dialog;
    private final DBHelper dbHelper = new DBHelper(LiveConversationChat.this);

    private final Conversations conversations = new Conversations();

    private final ImageGenerator imageGenerator = new ImageGenerator();
    private PaymentMethod paymentMethod;


    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String currentUserId=currentUser.getUid();

    private static final String endPoint = "https://python-backend-taupe.vercel.app/predict";

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_conversation_chat);

        paymentMethod=new PaymentMethod(LiveConversationChat.this);
        //load conversation
        conversations.loadConversations(currentUser);


        // setup dialog box
        dialog = new Dialog(LiveConversationChat.this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bgckground));
        dialog.setCancelable(false);

        //initialize cloud firestore
        db = FirebaseFirestore.getInstance();
        conversationStartTime = getCurrentTime();

        massageLayout = findViewById(R.id.sender_massage_laout);
        relativeLayout = findViewById(R.id.live_conversation_chat);
//        recyclerView = findViewById(R.id.live_users);
        chatRecycleView = findViewById(R.id.chat_view);
        suggestion_answer = findViewById(R.id.suggestion_answers);
        keyboardBtn = findViewById(R.id.keyboard_icon);
        mikeBtn = findViewById(R.id.speech_icon);
        muteMike = findViewById(R.id.speech_icon_mute);
 //       pauseBtn = findViewById(R.id.pause_icon);
        closeBtn = findViewById(R.id.close_icon);
        massageBox = findViewById(R.id.write_massage);
        sendButton = findViewById(R.id.send_massage);
        progressBar = findViewById(R.id.progress_bar);
        progressBar2 = findViewById(R.id.progressBarLiveChatImageGenaretor);

        save_prompt = dialog.findViewById(R.id.save_prompt);
        conversation_title_text = dialog.findViewById(R.id.conversation_title_text);
        conversationYesBtn = dialog.findViewById(R.id.yes_btn);
        conversationNoBtn = dialog.findViewById(R.id.no_btn);
        conversationSaveBtn = dialog.findViewById(R.id.save_conversation);
        conversationTitle = dialog.findViewById(R.id.conversation_title);


        List<Integer> live_users = new ArrayList<>();

        //load conversation default conversations
        ArrayList<ConversationModel> conversationForCheck = conversations.getConversationForCheck();

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

        final Handler handler = new Handler();
        final Runnable logRunnable = () -> Log.d("check 3", "onCreate: ");


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
                muteMike.setVisibility(View.GONE);
                mikeBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResults(Bundle bundle) {

                final boolean[] b = {true};

                muteMike.setVisibility(View.GONE);
                mikeBtn.setVisibility(View.VISIBLE);

                ArrayList<String> arrayList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                // Get the recognized text
                String recognizedText = arrayList.get(0);

                // Example: Check and handle censored words
                if (recognizedText.contains("****")) {
                    Log.d("SpeechRecognizer", "Filtered word detected in: " + recognizedText);

                    // Optionally replace with uncensored alternatives (if known)
                    recognizedText = recognizedText.replace("***", "possibleWord");
                }

                massageList.add(new MassageModel(recognizedText, 1));

                setUpLiveChat(chatRecycleView, massageList);

                // check the user is premium
                isUserPremium(currentUserId)
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Iterate through the results
                                queryDocumentSnapshots.forEach(documentSnapshot -> {
                                    boolean isPremium = Boolean.TRUE.equals(documentSnapshot.getBoolean("isPremium"));
                                    Log.d("Bool value 1", isPremium+" dsdad");

                                    if (isPremium) {
                                        // find the default conversation list has the this massage
                                        for (ConversationModel tempModel : conversationForCheck) {
                                            if (tempModel.getQuestion().equalsIgnoreCase(arrayList.get(0))) {
                                                b[0] = false;
                                                suggestion_answer.setVisibility(View.VISIBLE);
                                                setSuggestions(tempModel.getAnswer(), suggestion_answer);
                                            }
                                        }

                                        if (b[0]) {
                                            getSuggestions(arrayList.get(0));
                                        }
                                    }else {
                                        paymentMethod.firePaymentMethod(currentUserId);
                                    }

                                });
                            }
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "Error querying users", e);
                        });
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
            mikeBtn.setVisibility(View.GONE);
            muteMike.setVisibility(View.VISIBLE);
            speechRecognizer.startListening(speechIntent);
        });

        conversationNoBtn.setOnClickListener(view -> {
            dialog.dismiss();
            //11111111111111
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("load_fragment", "speech");
            startActivity(intent);
        });

        conversationYesBtn.setOnClickListener(view -> {
            save_prompt.setVisibility(View.GONE);
            conversationYesBtn.setVisibility(View.GONE);
            conversationNoBtn.setVisibility(View.GONE);
            conversation_title_text.setVisibility(View.VISIBLE);
            conversationTitle.setVisibility(View.VISIBLE);
            conversationSaveBtn.setVisibility(View.VISIBLE);
        });

        conversationSaveBtn.setOnClickListener(View -> {
            String title = conversationTitle.getText().toString().trim();

            if (title.isEmpty()) {
                conversationTitle.setError("Please fill this field.");
            } else {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate currentDate = LocalDate.now();

                    if (dbHelper.insertConversation(currentDate.toString(), conversationStartTime, getCurrentTime(), title, massageList.get(massageList.size() - 1).getMassage(), massageList)) {
                        Log.d(TAG, "Success the save the massage list");
                        Toast.makeText(LiveConversationChat.this, "Success the save the massages", Toast.LENGTH_SHORT).show();
                        //111111111111111
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("load_fragment", "speech");
                        startActivity(intent);
                    } else {
                        Toast.makeText(LiveConversationChat.this, "Unsaved the manages.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        closeBtn.setOnClickListener(view -> {
            dialog.show();
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

        JSONArray history = new JSONArray();
        JSONArray personalData = new JSONArray();

        //get the history
        ArrayList<MassageModel> allMassages = dbHelper.getAllMassages();


        // check of the initializing stage
        if (allMassages.isEmpty()) {
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("role", "receiver");
                jsonObject1.put("content", "Initializing Stage");
            } catch (JSONException e) {
                Toast.makeText(LiveConversationChat.this, "Response is " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            history.put(jsonObject1);
        } else {
            for (MassageModel tempMassage : allMassages) {

                JSONObject jsonObject1 = new JSONObject();
                String role = "user";
                if (tempMassage.getType() == 0) {
                    role = "sender";
                } else {
                    role = "receiver";
                }

                try {
                    jsonObject1.put("role", role);
                    jsonObject1.put("content", tempMassage.getMassage());
                } catch (JSONException e) {
                    Toast.makeText(LiveConversationChat.this, "Response is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                history.put(jsonObject1);
            }
        }


        JSONObject jsonObject2 = new JSONObject();
        try {

            jsonObject2.put("name", currentUser.getDisplayName());
            jsonObject2.put("email", currentUser.getEmail());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        personalData.put(jsonObject2);


        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("question", massage);
            paramObject.put("history", history);
            paramObject.put("personal_data", personalData);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.d(TAG, "getSuggestions: ----->" + paramObject);

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
                                Log.d("suggestions------->", suggetions.toString());
                                for (int i = 0; i < suggetions.length(); i++) {
                                    String suggestion = suggetions.getString(i);
                                    suggestions.add(suggestion);
                                    setSuggestions(suggestions, suggestion_answer);

                                }
                            } catch (JSONException e) {
                                Log.d("Exception------->", e.getMessage());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Request to GPT-3 error: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
                suggestion_answer.setVisibility(View.VISIBLE);
                Toast.makeText(LiveConversationChat.this, "Response is " + error.getMessage(), Toast.LENGTH_SHORT).show();

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
                Log.d(TAG, "VolleyError" + volleyError.getMessage());
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
        chatRecycleView.setAdapter(new ReceiverAdapter(getApplicationContext(), massageList, this));

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
    public void onClickDifficultWord(int position, View view) {
        dialog.dismiss();

        //------------------

        isUserPremium(currentUserId)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Iterate through the results
                        queryDocumentSnapshots.forEach(documentSnapshot -> {
                            boolean isPremium = Boolean.TRUE.equals(documentSnapshot.getBoolean("isPremium"));
                            Log.d("Bool value 1", isPremium+" dsdad");

                            if (isPremium) {
                                if (view instanceof TextView) {
                                    TextView textView = (TextView) view;
                                    String text = textView.getText().toString().trim();
                                    imageGenerator.Generate(text, progressBar2, dialog.getContext());
                                }
                            }else {
                                paymentMethod.firePaymentMethod(currentUserId);
                            }

                        });
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Error querying users", e);
                });
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
        } else if (view instanceof TextView) {
            TextView textView = (TextView) view;
            String text = textView.getText().toString().trim();
            textToSpeech(text);
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onLongClick(int position, View view) {
        // setup difficult words dialog box
        dialog.setContentView(R.layout.popup_difficult_words_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bgckground));
        dialog.setCancelable(false);

        difficultWordsRecycleView = dialog.findViewById(R.id.difficultWords);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            String text = textView.getText().toString().trim();
            String[] s = text.split(" ");
            difficultWordsRecycleView.setAdapter(new DifficultWordsAdapter(LiveConversationChat.this, s, this));
        }

        dialog.show();

        return true;
    }


    private void textToSpeech(String text) {

        float pitch = (1.0f);
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (9.5f / 10.0f);
        if (speed < 0.1) speed = 0.1f;  // Minimum speech rate value

        try {
            tts.setPitch(pitch);
            tts.setSpeechRate(speed);
        } catch (NullPointerException e) {
            tts = new TextToSpeech(getApplicationContext(), this::onInit);
        }

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





