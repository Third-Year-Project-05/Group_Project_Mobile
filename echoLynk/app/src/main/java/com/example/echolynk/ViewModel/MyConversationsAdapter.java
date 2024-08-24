package com.example.echolynk.ViewModel;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.echolynk.Model.Conversation_Item;
import com.example.echolynk.R;
import com.example.echolynk.View.LiveConversation.VoiceAssistanceThree;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyConversationsAdapter extends RecyclerView.Adapter<MyConversationsViewHolder> {

    Context context;


    public MyConversationsAdapter(Context context, List<Conversation_Item> conversationItems) {
        this.context = context;
        this.conversationItems = conversationItems;
    }

    List<Conversation_Item> conversationItems;
    @NonNull
    @Override
    public MyConversationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyConversationsViewHolder(LayoutInflater.from(context).inflate(R.layout.conversation_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyConversationsViewHolder holder, int position) {
        holder.TitleTextView.setText(conversationItems.get(position).getTitle());
        holder.DescriptionTextView.setText(conversationItems.get(position).getConversationLastMassage());
        holder.DateTextView.setText(conversationItems.get(position).getDate());
        String timeDifference = calculateTimeDifference(conversationItems.get(position).getTime(), conversationItems.get(position).getEndTime());
        if (timeDifference==null) {
            holder.TimeTextView.setText(conversationItems.get(position).getTime());
        }{
            holder.TimeTextView.setText(timeDifference);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, VoiceAssistanceThree.class);
            intent.putExtra("conversationId",conversationItems.get(position).getConversationId());
            intent.putExtra("date",conversationItems.get(position).getDate());
            intent.putExtra("timeDuration",timeDifference);
            context.startActivity(intent);
        });
    }

    private String calculateTimeDifference(String startTime,String endTime){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

            LocalTime sTime = LocalTime.parse(startTime, timeFormatter);
            LocalTime eTime = LocalTime.parse(endTime, timeFormatter);

            Duration duration = Duration.between(sTime, eTime);

            // Get the difference in minutes and seconds
            long minutes = duration.toMinutes();

            return minutes+" mins";
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return conversationItems.size();
    }
}
