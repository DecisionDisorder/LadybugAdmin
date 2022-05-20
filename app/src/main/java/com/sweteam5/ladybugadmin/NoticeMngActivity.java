package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NoticeMngActivity extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;
    ArrayList<NoticeInfo> noticeArrayList;
    private ArrayList<NoticeGroup> noticeList;
    private LinearLayout noticeContainer;
    noticeAdapter noticeAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;





    /**@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == NoticeWriteType.WRITE_NEW.ordinal()) {
            if(data != null) {
                // TODO: title, content, datetime 서버 전송

                Bundle noticeBundle = data.getBundleExtra("noticeContentBundle");
                String title = noticeBundle.getString("title");
                String dateTime = noticeBundle.getString("dateTime");
                String content = noticeBundle.getString("content");

                //addNoticeGroup(title, dateTime, content);
            }
        }
        else if(requestCode == NoticeWriteType.MODIFICATION.ordinal()) {
            if(data != null) {
                Bundle noticeBundle = data.getBundleExtra("noticeContentBundle");
                String title = noticeBundle.getString("title");
                String dateTime = noticeBundle.getString("dateTime");
                String content = noticeBundle.getString("content");

                int index = noticeBundle.getInt("index");
                //modifyNoticeGroup(index, title, dateTime, content);
            }
        }
    }**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoticeInfo notice = new NoticeInfo();
        setContentView(R.layout.activity_notice_mng);
        String del_title = (String) getIntent().getSerializableExtra("delete");

        if(del_title!=null){

            Log.d("NoticeMngActivity", "title:"+del_title);
            String del_notice_title = del_title;
            DeleteData(del_notice_title);
            Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
            startActivity(intent);
        }

        String modi_title = (String) getIntent().getSerializableExtra("modify");
        if(modi_title!=null){
            //작성중인 곳
            Log.d("NoticeMngActivity", "title:"+del_title);
            modiData(modi_title);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        noticeArrayList = new ArrayList<NoticeInfo>();
        noticeAdapter = new noticeAdapter(NoticeMngActivity.this, noticeArrayList);
        recyclerView.setAdapter(noticeAdapter);

        EventChangeListener();

        Button writeBtn = findViewById(R.id.writeButton);

        writeBtn.setOnClickListener(new View.OnClickListener() {//공지 쓰는 곳으로 가는 곳
           @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
                startActivityForResult(intent, NoticeWriteType.WRITE_NEW.ordinal());
            }
        });

        //createNoticeGroups();
    }
    private void DeleteData(String title){
        db = FirebaseFirestore.getInstance();
        db.collection("notice").whereEqualTo("title", title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()&&!task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    db.collection("notice").document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(NoticeMngActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NoticeMngActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(NoticeMngActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void modiData(String title){
        db = FirebaseFirestore.getInstance();
        db.collection("notice").whereEqualTo("title", title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()&&!task.getResult().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    db.collection("notice").document(documentID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            NoticeInfo notice = documentSnapshot.toObject(NoticeInfo.class);
                            Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
                            Bundle contentBundle = new Bundle();
                            contentBundle.putString("title", notice.getTitle());
                            contentBundle.putString("content", notice.getContent());
                            intent.putExtra("contentBundle", contentBundle);
                            db.collection("notice").document(documentID).delete();
                            startActivityForResult(intent, NoticeWriteType.MODIFICATION.ordinal());
                        }
                    });
                }
            }
        });
    }
    private void EventChangeListener(){//서버에서 데이터 가져오는 곳
        db.collection("notice").addSnapshotListener(new EventListener<QuerySnapshot>() {
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



    public void deleteNotice(NoticeGroup noticeGroup)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete notice").setMessage("Will you delete this notice?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: 게시글 삭제 서버 요청
                        //removeNoticeGroup(noticeGroup);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openNoticeModification(NoticeGroup noticeGroup) {

        Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
        Bundle contentBundle = new Bundle();
        //contentBundle.putString("title", noticeGroup.getTitle());
        //contentBundle.putString("content", noticeGroup.getContent());
        //contentBundle.putInt("index", noticeGroup.getIndex());
        intent.putExtra("contentBundle", contentBundle);
        startActivityForResult(intent, NoticeWriteType.MODIFICATION.ordinal());
    }

    @Override
    public void onItemClick(int position) {

    }
}