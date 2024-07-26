package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.AndroidUtils;
import com.example.echolynk.View.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUseRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUseRecyclerAdapter.UserModelViewHolder> {

    Context context;
    public SearchUseRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        holder.userName.setText(model.getUserName());
        //profile picture ekat metanin connect kranna
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                AndroidUtils.passUserModelAsIntent(intent,model);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        ImageView profilePicture;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.searchUserRecyclerRowUserName);
            profilePicture = itemView.findViewById(R.id.searchUserRecyclerRowImage);

        }
    }
}
