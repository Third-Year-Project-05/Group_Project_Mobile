package com.example.echolynk.View;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.ChatMessageModel;
import com.example.echolynk.Model.ChatroomModel;
import com.example.echolynk.Model.UserModel;
import com.example.echolynk.R;
import com.example.echolynk.Utils.AndroidUtils;
import com.example.echolynk.Utils.FirebaseUtils;
import com.example.echolynk.View.Adapter.ChatRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Array;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private UserModel otherUser;
    private ImageView backBtn;
    private CircleImageView otherUserProfilePicture;
    private TextView otherUserName;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private ImageButton sendMessageBtn;
    private String chatRoomId;
    private ChatroomModel chatroomModel;
    private ChatRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chat_activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setKeyboardListener();

        otherUser = AndroidUtils.getUserModelFromIntent(getIntent());
        backBtn = findViewById(R.id.chat_back_button);
        otherUserProfilePicture = findViewById(R.id.other_user_image);
        otherUserName = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);
        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);

        chatRoomId = FirebaseUtils.getChatRoomId(FirebaseUtils.currentUserId(), otherUser.getUserId());

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        FirebaseUtils.getOtherUserProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtils.setProfilePic(ChatActivity.this,uri,otherUserProfilePicture);
                    }
                });

        //set user name
        otherUserName.setText(otherUser.getUserName());

        getOrCreateChatRoomModel(FirebaseUtils.currentUserId(), otherUser.getUserId());

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (message.isEmpty()){
                    return;
                }
                else{
                    sendMessageToUser(message);
                }
            }
        });
        setUpChatRecyclerView();
    }

    private void setUpChatRecyclerView() {
        Query query = FirebaseUtils.getChatroomMessageReference(chatRoomId).orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void sendMessageToUser(String message) {
        chatroomModel.setLastMessage(message);
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtils.currentUserId());
        FirebaseUtils.getChatRoomReference(chatRoomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtils.currentUserId(),Timestamp.now());
        FirebaseUtils.getChatroomMessageReference(chatRoomId).add(chatMessageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    messageInput.setText("");
                }
            }
        });
    }

    private void getOrCreateChatRoomModel(String user1Id, String user2Id) {
        FirebaseUtils.getChatRoomReference(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    chatroomModel = task.getResult().toObject(ChatroomModel.class);

                    if (chatroomModel == null){
                        // first time chat;
                        chatroomModel = new ChatroomModel(
                                chatRoomId,
                                Arrays.asList(FirebaseUtils.currentUserId(), otherUser.getUserId()),
                                Timestamp.now(),
                                ""
                        );
                        FirebaseUtils.getChatRoomReference(chatRoomId).set(chatroomModel);
                    }
                }
            }
        });
    }

    private void setKeyboardListener() {
        final View rootLayout = findViewById(R.id.chat_activity_main); // Replace with your root layout id
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    rootLayout.setPadding(0, 75, 0, keypadHeight);
                }
            }
        });
    }




}