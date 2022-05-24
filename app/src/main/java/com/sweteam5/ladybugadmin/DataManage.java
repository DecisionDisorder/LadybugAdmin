package com.sweteam5.ladybugadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;


public class DataManage {
    private Context context;
    FirebaseFirestore fsdb= FirebaseFirestore.getInstance();
    //FirebaseDatabase db = FirebaseDatabase.getInstance();


    public void deleteNotice(Context context, String title){
        fsdb = FirebaseFirestore.getInstance();
        fsdb.collection("notice").whereEqualTo("title", title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {//id를 가져옴
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()&&!task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    fsdb.collection("notice").document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(context, NoticeMngActivity.class);
                            intent.putExtra("refresh", "refresh");
                            context.startActivity(intent);//refresh the list after delete
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { }
                    });
                }else{}
            }
        });
    }

    public void findmodifyNotice(Context context, String title){//find existing notice for modifying
        this.context = context;//NoticeMng
        fsdb = FirebaseFirestore.getInstance();
        Bundle contentBundle = new Bundle();
        fsdb.collection("notice").whereEqualTo("title", title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()&&!task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                            NoticeInfo notice = documentSnapshot.toObject(NoticeInfo.class);
                            contentBundle.putString("title", notice.getTitle());
                            contentBundle.putString("content", notice.getContent());
                            contentBundle.putString("documentid", (String)documentID);
                            Intent intent = new Intent(context, NoticeWriteActivity.class);//error
                            intent.putExtra("contentBundle", contentBundle);
                            context.startActivity(intent);
                            //return contentBundle;
                            //startActivityForResult(intent, NoticeWriteType.MODIFICATION.ordinal());
                }



            }
        });
    }




    public void uploadmodification(Context context, String title,String date,String content,String DocumentID){
        NoticeInfo noticeInfo = new NoticeInfo(title,date, content);
        fsdb.collection("notice").document(DocumentID).set(noticeInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(context, NoticeMngActivity.class);
                intent.putExtra("refresh", "refresh");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);//refresh the list after delete
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void showNoticeList(ProgressDialog progressDialog, ArrayList<NoticeInfo> noticeArrayList, noticeAdapter noticeAdapter){
        fsdb.collection("notice").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for(DocumentChange dc  :value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        noticeArrayList.add(dc.getDocument().toObject(NoticeInfo.class));
                    }
                    noticeAdapter.notifyDataSetChanged();
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });
    }

    public void uploadnotice(String title, String date, String content){
        NoticeInfo noticeInfo = new NoticeInfo(title,date, content);
        Log.d("DataManage", title+date+content);
        fsdb.collection("notice").add(noticeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) { }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { }
                });
    }

    /**public void showCodeList(ProgressDialog progressDialog, ArrayList<CodeInfo> codeArrayList,codeAdapter codeAdapter){
        fbdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    CodeInfo code = dataSnapshot.getValue(CodeInfo.class);
                    codeArrayList.add(code);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }**/
}
