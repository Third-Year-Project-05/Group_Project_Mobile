package com.example.echolynk.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.echolynk.Model.Call_Item;
import com.example.echolynk.R;
import com.example.echolynk.ViewModel.MyCallsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CallFragment extends Fragment {


    private RecyclerView recyclerView;
    private ImageView searchUserBtn;
    private EditText searchUserEditText;
    private String searchText;
    private FloatingActionButton newConversationBtn;

    public CallFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchUserEditText = view.findViewById(R.id.searchUserEditText);
        newConversationBtn = view.findViewById(R.id.newConversationButton);
        newConversationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                startActivity(intent);
            }
        });

        List<Call_Item> items = new ArrayList<>();
        items.add(new Call_Item("Nick Jonas", "hellow", "Yesterday", "4", R.drawable.dummyprofileimage));
        items.add(new Call_Item("John Doe", "Hi there", "Today", "3", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Jane Smith", "Good morning", "2 days ago", "2", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Alice Johnson", "How are you?", "Last week", "1", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Bob Brown", "Nice to meet you", "A month ago", "5", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Charlie Black", "See you soon", "A year ago", "10", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Dave White", "What's up?", "Yesterday", "4", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Emma Green", "Hello!", "An hour ago", "6", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Frank Blue", "Goodbye", "Last night", "7", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Grace Pink", "Take care", "3 days ago", "8", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Hank Yellow", "Call me", "4 days ago", "9", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Ivy Orange", "See you later", "5 days ago", "11", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Jack Red", "Thanks!", "6 days ago", "12", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Karen Gray", "You're welcome", "Last month", "13", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Leo Violet", "Good night", "2 weeks ago", "14", R.drawable.dummyprofileimage));
        items.add(new Call_Item("Mia Cyan", "Good evening", "3 weeks ago", "15", R.drawable.dummyprofileimage));


        recyclerView = view.findViewById(R.id.callsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        MyCallsAdapter myCallsAdapter = new MyCallsAdapter(getContext(), items);
        recyclerView.setAdapter(myCallsAdapter);
        myCallsAdapter.notifyDataSetChanged();


    }
}