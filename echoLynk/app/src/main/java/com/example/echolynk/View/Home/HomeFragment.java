package com.example.echolynk.View.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.echolynk.R;
public class HomeFragment extends Fragment {

    LinearLayout videoCallBtn,voiceAssistent,games;
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

        return view;
    }
}