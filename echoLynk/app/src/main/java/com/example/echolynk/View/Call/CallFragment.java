package com.example.echolynk.View.Call;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.echolynk.Model.Call_Item;
import com.example.echolynk.R;
import com.example.echolynk.ViewModel.MyCallsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallFragment extends Fragment {


    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallFragment newInstance(String param1, String param2) {
        CallFragment fragment = new CallFragment();
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
        return inflater.inflate(R.layout.fragment_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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