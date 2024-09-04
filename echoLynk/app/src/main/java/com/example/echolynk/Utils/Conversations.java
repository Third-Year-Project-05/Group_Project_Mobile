package com.example.echolynk.Utils;

import com.example.echolynk.Model.ConversationModel;

import java.util.ArrayList;

public class Conversations {

    private final ArrayList<ConversationModel> conversations=new ArrayList<>();

    public Conversations() {
    }

    public void loadConversations(){
        conversations.add(new ConversationModel(
                "Good morning How are you today",
                "Good morning! I'm doing well, thanks. How about you?"
        ));
        conversations.add(new ConversationModel(
                "I'm good, thanks. Did you eat breakfast yet?",
                "Yes, I did. Just had some toast and coffee. What about you?"
        ));
        conversations.add(new ConversationModel(
                "I had a quick breakfast too—some cereal and a banana.",
                "That sounds nice. Any plans for the day?"
        ));
        conversations.add(new ConversationModel(
                "Just work, mostly. But I’m hoping to squeeze in a walk later. How about you?",
                "Pretty much the same, busy day ahead. A walk sounds like a good idea, though."
        ));
        conversations.add(new ConversationModel(
                "Yeah, it helps clear the mind. Well, let’s have a productive day!",
                "Absolutely! Let’s do it."
        ));
        conversations.add(new ConversationModel(
                "Good morning! Did you eat already?",
                "Good morning! Yes, I had breakfast. How about you?"
        ));
        conversations.add(new ConversationModel(
                "I did too. How are you feeling today?",
                "I'm feeling good, thanks! How about you?"
        ));
        conversations.add(new ConversationModel(
                "good morning",
                "Good morning! How's your day starting off?"
        ));
    }

    public ArrayList<ConversationModel> getConversationForCheck(){
        return conversations;
    }
}
