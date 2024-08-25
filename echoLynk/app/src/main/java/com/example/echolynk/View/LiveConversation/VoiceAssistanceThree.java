package com.example.echolynk.View.LiveConversation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.MassageModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.DB.DBHelper;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.ReceiverAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VoiceAssistanceThree extends AppCompatActivity {

    private TextView day,timeDurationTextView;
    private RecyclerView chat_view;

    private ImageButton backBtn;

    private DBHelper dbHelper=new DBHelper(VoiceAssistanceThree.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voice_assistance_three);

        Intent intent = getIntent();

        int conversationId=intent.getIntExtra("conversationId",-1);
        String date=intent.getStringExtra("date");
        String timeDuration=intent.getStringExtra("timeDuration");

        day=findViewById(R.id.conversation_day);
        timeDurationTextView=findViewById(R.id.timeDurationTW);
        chat_view=findViewById(R.id.chat_view);
        backBtn=findViewById(R.id.back_button);

        //set date and time duration
        setTimeAndDate(date,timeDuration);

        //get massage list
        ArrayList<MassageModel> massageList = dbHelper.getMassageList(conversationId);

        //setUpLiveChat(chatRecycleView, massageList);
        setUpLiveChat(chat_view,massageList);

        // set up back button
        backBtn.setOnClickListener(view -> {
            /*Intent intent1 = new Intent(VoiceAssistanceThree.this, SpeechFragment.class);
            startActivity(intent1);

            // Create an instance of the SpeechFragment
            SpeechFragment speechFragment = new SpeechFragment();

            // Get the FragmentManager and start a transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the existing fragment with SpeechFragment
            fragmentTransaction.replace(R.id.speechFragmentLayout, speechFragment);

            // Optionally, add the transaction to the back stack so the user can navigate back
            fragmentTransaction.addToBackStack(null);

            // Commit the transaction
            fragmentTransaction.commit();*/
        });

    }

    private void setUpLiveChat(RecyclerView chat_view, List<MassageModel> massageList) {
        chat_view.setAdapter(new ReceiverAdapter(getApplicationContext(), massageList, (onClickListener) this));
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
}