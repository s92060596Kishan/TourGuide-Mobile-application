package com.example.tourguide;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBHelper {

    private static final String USERS_NODE = "users";
    private static DBHelper instance;
    private DatabaseReference databaseReference;

    private DBHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(USERS_NODE);
    }

    public static synchronized DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    public void saveUser(User user) {
        DatabaseReference userRef = databaseReference.child(user.getEmail().replace(".", "_")); // Use the user's email as the child key, replacing '.' with '_'

        // Save the user details to the userRef
        userRef.setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User details saved successfully
                        Log.d("DBHelper", "User details saved successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while saving user details
                        Log.e("DBHelper", "Failed to save user details", e);
                    }
                });
    }


}
