package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirebaseParser<T> {

    FirebaseFirestore fsdb= FirebaseFirestore.getInstance();
    DatabaseReference fbdb;

    public boolean setDataOnFireBase (String collectionPath, String DocumentID, T content) {
        ResultReturn resultReturn = new ResultReturn();
        fsdb.collection(collectionPath).document(DocumentID).set(content).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                resultReturn.result = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultReturn.result = false;
            }
        });

        return resultReturn.result;
    }

    public T getFromFireBase (String collectionPath, String documentID, String equal, T content) {
        ResultReturn resultReturn = new ResultReturn();
        fsdb.collection("notice").document(documentID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                resultReturn.resultObject = (T) snapshot.toObject(content.getClass());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultReturn.resultObject = null;
            }
        });
        return resultReturn.resultObject;
    }

    class ResultReturn {
        public boolean result;
        public T resultObject;
    }
}