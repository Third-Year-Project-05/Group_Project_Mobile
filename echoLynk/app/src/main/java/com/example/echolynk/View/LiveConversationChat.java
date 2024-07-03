package com.example.echolynk.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.R;
import com.example.echolynk.Utils.onClickListener;
import com.example.echolynk.View.Adapter.LiveUserAdapter;
import com.example.echolynk.View.Adapter.ReceiverAdapter;
import com.example.echolynk.View.Adapter.SenderAdapter;

import java.util.ArrayList;
import java.util.List;

public class LiveConversationChat extends AppCompatActivity implements onClickListener {


    RecyclerView recyclerView;
    RecyclerView chatRecycleView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_conversation_chat);

        recyclerView = findViewById(R.id.live_users);
        chatRecycleView = findViewById(R.id.chat_view);

        List<Integer> live_users = new ArrayList<>();
        List<String> massageList = new ArrayList<>();

        live_users.add(R.drawable.dummy_user_image);
        live_users.add(R.drawable.dummyprofileimage);
        live_users.add(R.drawable.dummy_blog_img2);
        live_users.add(R.drawable.facebook);
        live_users.add(R.drawable.dummy_user_image);
        live_users.add(R.drawable.dummy_user_image);
        live_users.add(R.drawable.dummy_user_image);


        massageList.add("abc");
        massageList.add("10");
        massageList.add("safafa");
        massageList.add("abcasdsdasd");


        if (recyclerView != null) {
            recyclerView.setAdapter(new LiveUserAdapter(getApplicationContext(), live_users, this));
        }

        if (chatRecycleView != null) {
            chatRecycleView.setAdapter(new SenderAdapter(getApplicationContext(), massageList));
           // chatRecycleView.setAdapter(new ReceiverAdapter(getApplicationContext(),massageList));
        }

    }

    @Override
    public void onClick(int position, View view) {
        //set up on click function using live users
    }
}