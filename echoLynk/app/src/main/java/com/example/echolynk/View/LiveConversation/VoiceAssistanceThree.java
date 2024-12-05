package com.example.echolynk.View.LiveConversation;

import static com.example.echolynk.Utils.TetFilter.filterTextMassage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.MassageModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.DB.DBHelper;
import com.example.echolynk.Utils.ImageGenerator;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.DifficultWordsAdapter;
import com.example.echolynk.View.Adapter.ReceiverAdapter;
import com.example.echolynk.View.MainActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VoiceAssistanceThree extends AppCompatActivity implements onClickListener {

    private TextView day,timeDurationTextView;
    private RecyclerView chat_view,difficultWordsRecycleView;
    private TextToSpeech tts;
    private ImageButton backBtn;
    private ProgressBar progressBar2;
    private ImageGenerator imageGenerator=new ImageGenerator();
    private Dialog dialog;

    private DBHelper dbHelper=new DBHelper(VoiceAssistanceThree.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voice_assistance_three);

        Intent intent = getIntent();

        // Initialize TextToSpeech
        tts = new TextToSpeech(VoiceAssistanceThree.this, this::onInit);
        dialog=new Dialog(VoiceAssistanceThree.this);

        int conversationId=intent.getIntExtra("conversationId",-1);
        String date=intent.getStringExtra("date");
        String timeDuration=intent.getStringExtra("timeDuration");

        day=findViewById(R.id.conversation_day);
        timeDurationTextView=findViewById(R.id.timeDurationTW);
        chat_view=findViewById(R.id.chat_view);
        backBtn=findViewById(R.id.back_button);
        progressBar2 = findViewById(R.id.progressConversationHistory);

        //set date and time duration
        setTimeAndDate(date,timeDuration);

        //get massage list
        ArrayList<MassageModel> massageList = dbHelper.getMassageList(conversationId);

        //setUpLiveChat(chatRecycleView, massageList);
        setUpLiveChat(chat_view,massageList);

        // set up back button
        backBtn.setOnClickListener(view -> {
            Intent intent0 = new Intent(this, MainActivity.class);
            intent0.putExtra("load_fragment", "speech");
            startActivity(intent0);
        });


    }

    private void setUpLiveChat(RecyclerView chat_view, List<MassageModel> massageList) {
        chat_view.setAdapter(new ReceiverAdapter(getApplicationContext(), massageList,  this));
    }

    private void setTimeAndDate(String date, String timeDuration) {
        //date convert to week day
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate dateLocal = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            String dayName = dateLocal.getDayOfWeek().name();
            day.setText(dayName);
            timeDurationTextView.setText(timeDuration);
        }
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

    @Override
    public void onClick(int position, View view) {
        if (view instanceof TextView) {
            TextView textView=(TextView) view;
            String text = textView.getText().toString().trim();
            textToSpeech(text);
        }

    }

    @Override
    public void onClickDifficultWord(int position, View view) {
        dialog.dismiss();

        if (view instanceof TextView) {
            TextView textView=(TextView) view;
            String text = textView.getText().toString().trim();
            imageGenerator.Generate(text+text+"related by student",progressBar2,dialog.getContext());
        }

    }

    @Override
    public boolean onLongClick(int position, View view) {

        // setup difficult words dialog box
        dialog.setContentView(R.layout.popup_difficult_words_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bgckground));
        dialog.setCancelable(true);

        difficultWordsRecycleView=dialog.findViewById(R.id.difficultWords);
        if (view instanceof TextView) {
            TextView textView=(TextView) view;
            String text = textView.getText().toString().trim();
            String[] s = text.split(" ");
            String[] strings = filterTextMassage(s);
            difficultWordsRecycleView.setAdapter(new DifficultWordsAdapter(VoiceAssistanceThree.this,strings,this));
        }
        dialog.show();

        return true;
    }


    public void textToSpeech(String text) {

        float pitch = (1.0f);
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (9.5f / 10.0f);
        if (speed < 0.1) speed = 0.1f;  // Minimum speech rate value

        try {
            tts.setPitch(pitch);
            tts.setSpeechRate(speed);
        }catch (NullPointerException e){
            tts = new TextToSpeech(getApplicationContext(), this::onInit);
        }

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }
}