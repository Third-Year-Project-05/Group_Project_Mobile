package com.example.echolynk.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.ChatroomModel;
import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.AndroidUtils;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.ChatActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {

    Context context;
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatroomModel model) {
        FirebaseUtils.getOtherUserFromChatRoomModel(model.getUserIds()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtils.currentUserId());

                    UserModel otherUserModel = task.getResult().toObject(UserModel.class);
                    holder.otherUserUserName.setText(otherUserModel.getUserName());
                    holder.lastMessageTime.setText(FirebaseUtils.timeStampToString(model.getLastMessageTimestamp()));

                    if (lastMessageSentByMe){
                        holder.lastMessage.setText("You : "+model.getLastMessage());
                    }
                    else {
                        holder.lastMessage.setText(model.getLastMessage());
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            AndroidUtils.passUserModelAsIntent(intent, otherUserModel);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.call_item_view,parent,false);
        return new ChatRoomModelViewHolder(view);

    }

    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder{
        CircleImageView otherUserProfilePicture;
        TextView otherUserUserName;
        TextView lastMessage;
        TextView lastMessageTime;
        TextView count;


        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);

         //   otherUserProfilePicture = itemView.findViewById(R.id.contactImageView);
            otherUserUserName = itemView.findViewById(R.id.contactNameView);
            lastMessage = itemView.findViewById(R.id.lastMessageTextView);
            lastMessageTime = itemView.findViewById(R.id.dateTextView);
          //  count = itemView.findViewById(R.id.countTextView);



        }
    }
}
