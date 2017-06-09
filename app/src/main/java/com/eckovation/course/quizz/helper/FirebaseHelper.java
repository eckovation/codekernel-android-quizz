package com.eckovation.course.quizz.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by codekernel on 07/06/17.
 */

public class FirebaseHelper {

    public static DatabaseReference getTopicsRefs() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("topics");
    }

    public static DatabaseReference getQuestionRefs(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("quiz").child(key);
    }
}
