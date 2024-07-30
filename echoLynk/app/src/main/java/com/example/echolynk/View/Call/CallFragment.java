package com.example.echolynk.View.Call;
import android.annotation.SuppressLint;
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

import com.example.echolynk.Model.ChatroomModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.Adapter.RecentChatRecyclerAdapter;
import com.example.echolynk.View.SearchUserActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class CallFragment extends Fragment {


     RecyclerView recyclerView;
     ImageView searchUserBtn;
     EditText searchUserEditText;
     String searchText;
     FloatingActionButton newConversationBtn;
     RecentChatRecyclerAdapter adapter;

    public CallFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        searchUserEditText = view.findViewById(R.id.searchUserEditText);
        newConversationBtn = view.findViewById(R.id.newConversationButton);
        newConversationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment

        recyclerView = view.findViewById(R.id.callsRecyclerView);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        Query query = FirebaseUtils.allChatroomCollectionReference()
                .whereArrayContains("userIds", FirebaseUtils.currentUserId())
                .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(query,ChatroomModel.class).build();

        adapter = new RecentChatRecyclerAdapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null){
            adapter.stopListening();
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}