package com.example.echolynk.View.LiveConversation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.echolynk.Model.Conversation_Item;
import com.example.echolynk.R;
import com.example.echolynk.ViewModel.MyConversationsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpeechFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeechFragment extends Fragment {

    ConstraintLayout constraintLayout;

    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SpeechFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpeechFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpeechFragment newInstance(String param1, String param2) {
        SpeechFragment fragment = new SpeechFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_speech, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        constraintLayout = view.findViewById(R.id.constraintLayout001);

        List<Conversation_Item> items = new ArrayList<>();
        items.add(new Conversation_Item("University Conversation", "I am going to meet my friend jhon tomorrow. There fore i can't attend the lecture", "Yesterday", "45 min"));
        items.add(new Conversation_Item("Work Update", "I completed the report you asked for.", "Today", "10 min"));
        items.add(new Conversation_Item("Grocery Shopping", "Don't forget to buy milk and eggs.", "Yesterday", "5 min"));
        items.add(new Conversation_Item("Doctor Appointment", "My appointment is scheduled for 3 PM tomorrow.", "Last week", "30 min"));
        items.add(new Conversation_Item("Book Club Meeting", "We are discussing '1984' in the next meeting.", "Two days ago", "20 min"));
        items.add(new Conversation_Item("Dinner Plans", "Shall we go out for dinner this Friday?", "Today", "15 min"));
        items.add(new Conversation_Item("Travel Plans", "We need to book the hotel for our trip.", "Last month", "40 min"));
        items.add(new Conversation_Item("Birthday Party", "My birthday party is next Saturday at 7 PM.", "Last week", "50 min"));
        items.add(new Conversation_Item("Gym Session", "Let's try the new workout routine tomorrow.", "Today", "25 min"));
        items.add(new Conversation_Item("Movie Night", "How about watching a movie tonight?", "Yesterday", "30 min"));
        items.add(new Conversation_Item("Tech Conference", "Are you attending the tech conference next week?", "Last month", "60 min"));
        items.add(new Conversation_Item("Job Interview", "I have a job interview scheduled for Monday.", "Last week", "45 min"));
        items.add(new Conversation_Item("Family Dinner", "We have a family dinner planned for Sunday.", "Three days ago", "35 min"));
        items.add(new Conversation_Item("Library Visit", "I need to return the library books by Thursday.", "Today", "10 min"));
        items.add(new Conversation_Item("Project Deadline", "The project deadline is next Friday.", "Two weeks ago", "50 min"));
        items.add(new Conversation_Item("Meeting Reminder", "Reminder: Team meeting at 2 PM today.", "Yesterday", "20 min"));



        recyclerView = view.findViewById(R.id.conversationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        MyConversationsAdapter myConversationsAdapter = new MyConversationsAdapter(getContext(), items);
        recyclerView.setAdapter(myConversationsAdapter);
        myConversationsAdapter.notifyDataSetChanged();

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LiveConversationChat.class);
                startActivity(intent);
            }
        });


    }
}