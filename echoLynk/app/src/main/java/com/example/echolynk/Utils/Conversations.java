package com.example.echolynk.Utils;

import com.example.echolynk.Model.ConversationModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class Conversations {

    private final ArrayList<ConversationModel> conversations=new ArrayList<>();

    public Conversations() {
    }

    public void loadConversations(FirebaseUser currentUser) {
        conversations.add(new ConversationModel(
                "what is your name",
                Arrays.asList("My name is "+currentUser.getDisplayName()+". What about you?",
                        "I'm "+currentUser.getDisplayName()+". Nice to meet you!",
                        "I'm "+currentUser.getDisplayName()+". How about you?")
        ));

        conversations.add(new ConversationModel(
                "what's your name",
                Arrays.asList("My name is "+currentUser.getDisplayName()+". What about you?",
                        "I'm "+currentUser.getDisplayName()+". Nice to meet you!",
                        "I'm "+currentUser.getDisplayName()+". How about you?")
        ));
        conversations.add(new ConversationModel(
                "how are you today",
                Arrays.asList("I'm doing great, thanks! How about you?",
                        "Not bad, just a little tired.",
                        "I'm okay, but I have a lot of homework to do.")
        ));
        conversations.add(new ConversationModel(
                "did you finish the homework",
                Arrays.asList("Yes, I finished it last night.",
                        "Not yet, I’m planning to do it after school.",
                        "No, I forgot. Do you think I can do it quickly now?")
        ));
        conversations.add(new ConversationModel(
                "what’s your favorite subject",
                Arrays.asList("I love math, it’s really interesting.",
                        "Science is my favorite.",
                        "I enjoy history the most. What about you?")
        ));
        conversations.add(new ConversationModel(
                "do you want to sit together at lunch",
                Arrays.asList("Sure, that sounds great!",
                        "Yeah, let’s sit near the window.",
                        "I already made plans with someone else, but we can sit together tomorrow.")
        ));
        conversations.add(new ConversationModel(
                "are you coming to the school trip",
                Arrays.asList("Yes, I can’t wait! It’s going to be fun.",
                        "I’m not sure yet, I need to check with my parents.",
                        "No, I can’t make it this time.")
        ));
        conversations.add(new ConversationModel(
                "what did you do over the weekend",
                Arrays.asList("I went to the movies with my family.",
                        "I just stayed home and relaxed.",
                        "I visited my grandparents. It was nice!")
        ));
        conversations.add(new ConversationModel(
                "do you play any sports",
                Arrays.asList("Yeah, I play football after school.",
                        "I’m on the basketball team.",
                        "No, but I’m thinking of joining the tennis club.")
        ));
        conversations.add(new ConversationModel(
                "do you like reading books",
                Arrays.asList("Yes, I love reading mystery novels.",
                        "Not really, but I enjoy comic books.",
                        "Sometimes, especially when it’s a good story.")
        ));
        conversations.add(new ConversationModel(
                "what’s your favorite food",
                Arrays.asList("I love pizza, it’s my favorite!",
                        "I could eat Rice and Curry every day.",
                        "Probably spaghetti. How about you?")
        ));
        conversations.add(new ConversationModel(
                "do you have any pets",
                Arrays.asList("Yes, I have a dog named Max.",
                        "No, but I really want a cat.",
                        "I have a fish, but that’s all.")
        ));
        conversations.add(new ConversationModel(
                "do you want to study together after school",
                Arrays.asList("That’s a great idea! Let’s meet at the library.",
                        "Sure, I could use some help with math.",
                        "I already have plans today, but maybe tomorrow?")
        ));
        conversations.add(new ConversationModel(
                "what are your hobbies",
                Arrays.asList("I like painting and playing video games.",
                        "I enjoy playing guitar in my free time.",
                        "I love baking and trying new recipes.")
        ));
        conversations.add(new ConversationModel(
                "have you seen the new movie",
                Arrays.asList("Yes, it was amazing! Did you watch it too?",
                        "Not yet, but I really want to.",
                        "No, is it worth watching?")
        ));
        conversations.add(new ConversationModel(
                "do you take the bus to school",
                Arrays.asList("Yes, I take the bus every day.",
                        "No, my parents drop me off.",
                        "Sometimes, but I usually walk.")
        ));
        conversations.add(new ConversationModel(
                "do you have any plans this weekend",
                Arrays.asList("I’m going to a friend’s birthday party.",
                        "Not really, just staying home and relaxing.",
                        "I’m planning to go hiking with my family.")
        ));
        conversations.add(new ConversationModel(
                "what’s your favorite tv show",
                Arrays.asList("I really like watching 'Stranger Things'.",
                        "I don’t watch much TV, but I love cartoons.",
                        "I’m a big fan of 'Friends'.")
        ));
        conversations.add(new ConversationModel(
                "how do you usually spend your free time",
                Arrays.asList("I like to hang out with friends or play video games.",
                        "I usually read books or watch TV.",
                        "I spend most of my free time playing sports.")
        ));
        conversations.add(new ConversationModel(
                "do you understand this problem",
                Arrays.asList("Not really, I’m stuck on this part. Do you get it?",
                        "Yeah, I think so. Let me show you how I solved it.",
                        "I’m confused too. Maybe we should ask the teacher for help.")
        ));
        conversations.add(new ConversationModel(
                "can i borrow your notes from yesterday",
                Arrays.asList("Sure, just make sure to give them back before the next class.",
                        "I didn’t take many notes, but I’ll share what I have.",
                        "I’ll send them to you after class.")
        ));
        conversations.add(new ConversationModel(
                "do you want to study together for the test",
                Arrays.asList("Definitely, let’s meet after school.",
                        "I’m already studying with someone else, but we can all join up.",
                        "I prefer studying alone, but thanks for asking!")
        ));
        conversations.add(new ConversationModel(
                "can you explain this part to me",
                Arrays.asList("Sure, here’s how I understand it.",
                        "I’m not sure I get it either. Let’s figure it out together.",
                        "I’m a little busy right now, but I can help you after class.")
        ));
        conversations.add(new ConversationModel(
                "do you think we need to know this for the quiz",
                Arrays.asList("Probably. The teacher spent a lot of time on it.",
                        "I don’t think so, but I’ll study it just in case.",
                        "I’m not sure. Let’s ask someone who knows.")
        ));
        conversations.add(new ConversationModel(
                "want to work together on the group project",
                Arrays.asList("Yeah, sounds good! Let’s figure out who’s doing what.",
                        "I already have a group, but maybe we can collaborate on some parts.",
                        "I was thinking about working solo, but thanks for asking!")
        ));
        conversations.add(new ConversationModel(
                "do you think we’ll have time to finish this in class",
                Arrays.asList("I don’t think so. We might have to finish it as homework.",
                        "If we work quickly, we should be able to get it done.",
                        "I’m not sure, but I’ll try to get as much done as possible.")
        ));
        conversations.add(new ConversationModel(
                "did you understand the teacher’s explanation",
                Arrays.asList("Yeah, it made sense to me. Do you want me to explain it?",
                        "Not really, I’m still confused about it.",
                        "A little bit, but I might need to go over it again later.")
        ));
        conversations.add(new ConversationModel(
                "are you ready for the test",
                Arrays.asList("I think so, I studied a lot. How about you?",
                        "Not yet, I’m still reviewing some topics.",
                        "I’m feeling pretty nervous, honestly.")
        ));
        conversations.add(new ConversationModel(
                "do you want to come over after school",
                Arrays.asList("Sure, we can study together!",
                        "I can’t today, maybe another time?",
                        "I’m busy today, but let’s hang out tomorrow.")
        ));
        conversations.add(new ConversationModel(
                "do you want to swap snacks",
                Arrays.asList("Sure!",
                        "I’m okay, but thanks for offering!",
                        "Yeah, let’s trade. What do you have?")
        ));
        conversations.add(new ConversationModel(
                "did you do well on the test",
                Arrays.asList("I think so. I studied a lot, so I hope it paid off!",
                        "I did alright, but I could’ve done better.",
                        "Not really, I didn’t study enough. How about you?")
        ));
        conversations.add(new ConversationModel(
                "are you nervous about the presentation",
                Arrays.asList("A little, but I practiced a lot. What about you?",
                        "Yeah, I always get nervous speaking in front of the class.",
                        "Not really. I just want to get it over with!")
        ));
        conversations.add(new ConversationModel(
                "are you going to study for the quiz",
                Arrays.asList("Yeah, I’m planning to study after school. Do you want to study together?",
                        "I think I already know the material, so I’ll just review my notes.",
                        "I’ll probably cram in the morning, as usual!")
        ));
        conversations.add(new ConversationModel(
                "can you help me with this problem",
                Arrays.asList("Sure, I’ll show you how I did it.",
                        "I’m not sure either, but let’s figure it out together.",
                        "I’m not great at this topic, maybe we should ask the teacher.")
        ));
        conversations.add(new ConversationModel(
                "what are you doing after school today",
                Arrays.asList("I’m going to the library to study. How about you?",
                        "Nothing much, just heading home.",
                        "I have basketball practice. What about you?")
        ));
        conversations.add(new ConversationModel(
                "what did you think of the book",
                Arrays.asList("I thought it was really interesting. What about you?",
                        "It was alright, but not my favorite.",
                        "I didn’t like it much. It wasn’t my type of story.")
        ));
    }



    public ArrayList<ConversationModel> getConversationForCheck(){
        return conversations;
    }
}
