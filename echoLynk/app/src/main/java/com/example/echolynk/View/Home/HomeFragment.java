package com.example.echolynk.View.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.echolynk.R;
import com.example.echolynk.View.Blog.HomeBlog;
import com.example.echolynk.View.LiveConversation.VideoChat;
import com.example.echolynk.View.MainActivity;

public class HomeFragment extends Fragment {

    LinearLayout videoCallBtn,voiceAssistent,games,blog01,blog02;
    TextView blogBtn;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        videoCallBtn = view.findViewById(R.id.goto_video_call);
        voiceAssistent = view.findViewById(R.id.goto_voice_assistent);
        games = view.findViewById(R.id.goto_games);
        blog01 = view.findViewById(R.id.blog1);
        blogBtn = view.findViewById(R.id.textView23);

        blogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HomeBlog.class);
                startActivity(intent);
            }
        });

        videoCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VideoChat.class);
                startActivity(intent);
            }
        });

        voiceAssistent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("load_fragment", "speech");
                startActivity(intent);
            }
        });
        
        return view;
    }
}