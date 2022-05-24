package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


public class FirebaseParser<T> {

    FirebaseFirestore fsdb= FirebaseFirestore.getInstance();

    public boolean setDataOnFirebase (String collectionPath, String DocumentID, T content) {
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

    public T getFromFirebase (String collectionPath, String documentID, String equal, T content) {
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

    //equalkey는 찾을 data의 field값이고 equal은 값 db에서 equal과 같은 값을 가진 dataID를 찾아서 가져온다
    public T getfromFirebase (String collectionPath, String documentID, T content) {
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

    //equalkey는 찾을 data의 field값이고 equal은 값 db에서 equal과 같은 값을 가진 dataID를 찾아서 가져온다
    public String getidfromFirebase (String collectionPath, String equalfield,String equal, T content) {
        ResultReturn resultReturn = new ResultReturn();
        fsdb.collection(collectionPath).whereEqualTo(equalfield, equal).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    resultReturn.id = documentID;
                } else {
                    resultReturn.id = null;
                }
            }
        });
        return resultReturn.id;
    }
    //delete data using ID
    public boolean deleteFromFirebase (String collectionPath, String documentID) {
        ResultReturn resultReturn = new ResultReturn();
        fsdb.collection(collectionPath).document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
    //add data
    public boolean addToFirebase(String collectionPath,  T content) {
        ResultReturn resultReturn = new ResultReturn();
        fsdb.collection(collectionPath).add(content)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        resultReturn.result = true;}
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultReturn.result = false;
                    }
                });
        return resultReturn.result;
    }

    class ResultReturn {
        public boolean result;
        public T resultObject;
        public String id;
    }
}