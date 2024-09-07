package com.example.echolynk.Utils;

import java.util.ArrayList;

public class TetFilter {

    private static final String[] defaultRemoveWords={" ","a","and","the","of","to","from","he","she","they","can",
            "is","an ","was","were","did","do","done","in","on","put","there","when","where","then","it's",
            "well","for","not","yet","you","i'm","That’s","Yeah","been","I’ve","how's","how","i"};

    public static String[] filterTextMassage(String[] massage){
        ArrayList<String>  filterArray=new ArrayList<>();

      L1:  for (int i = 0; i < massage.length; i++) {
            for (int j = 0; j <defaultRemoveWords.length; j++) {
                if (massage[i].equalsIgnoreCase(defaultRemoveWords[j])){
                    continue L1;
                }
            }
            filterArray.add(massage[i]);
        }

        return filterArray.toArray(new String[0]);
    }
}
