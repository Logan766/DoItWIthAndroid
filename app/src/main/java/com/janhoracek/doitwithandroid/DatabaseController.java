package com.janhoracek.doitwithandroid;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public final class DatabaseController {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";


    private static final DatabaseController DatabaseControllerInstance = new DatabaseController();

    private DatabaseController(){

    }

    public static DatabaseController getInstance() {
        return  DatabaseControllerInstance;
    }

    public void saveToDatabase(String title, String description) {
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, description);

        db.collection(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail())).document("Prvni note").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DIWD", "Success save");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DIWD", "FAIL save" + e.toString());
                    }
                });
    }

    public void getFromDatabase(String path) {
        DocumentReference docRef = db.document(path);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d("DIWD", "read successfull");
                    Log.d("DIWD", "Ctu: " + documentSnapshot.getString("title"));
                } else {
                    Log.d("DIWD", "document does not exist");
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DIWD","read failed with: " + e.toString());
            }
        });
    }


}
