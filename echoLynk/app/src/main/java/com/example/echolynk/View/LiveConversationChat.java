package com.example.echolynk.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.RecycleViewCustomItemDirection;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.LiveUserAdapter;
import com.example.echolynk.View.Adapter.ReceiverAdapter;
import com.example.echolynk.View.Adapter.SenderAdapter;
import com.example.echolynk.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class LiveConversationChat extends AppCompatActivity implements onClickListener {


    ActivityMainBinding binding;
    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    RecyclerView chatRecycleView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_conversation_chat);
        binding=ActivityMainBinding.inflate(getLayoutInflater());

        relativeLayout=findViewById(R.id.live_conversation_chat);
        recyclerView = findViewById(R.id.live_users);
        chatRecycleView = findViewById(R.id.chat_view);


        List<Integer> live_users = new ArrayList<>();
        List<String> massageList = new ArrayList<>();



        // handle the tool bar
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setSelectedItemId(R.id.textToSpeech);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.live_conversation_chat),(v,insets) ->{
            Insets toolBar=insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(toolBar.left,toolBar.top,toolBar.right,toolBar.bottom);
            return insets;
        });


        // set data for live users icon tab
        setLiveUsers(live_users,recyclerView);



        // set data for live chat view
        setLiveChat(chatRecycleView,massageList);









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