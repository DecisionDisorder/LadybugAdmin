package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class NoticeMngActivity extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;
    ArrayList<NoticeInfo> noticeArrayList;
    noticeAdapter noticeAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    private DataManage dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_mng);
        dm = new DataManage();

        String refe = (String) getIntent().getSerializableExtra("refresh");//command recyclerview refresh

        //attach notice list
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noticeArrayList = new ArrayList<NoticeInfo>();
        noticeAdapter = new noticeAdapter(NoticeMngActivity.this, noticeArrayList);
        recyclerView.setAdapter(noticeAdapter);
        dm.showNoticeList(progressDialog, noticeArrayList, noticeAdapter);//get noticeList from server

        Button writeBtn = findViewById(R.id.writeButton);
        writeBtn.setOnClickListener(new View.OnClickListener() {//click write button
           @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
                startActivityForResult(intent, NoticeWriteType.WRITE_NEW.ordinal());
                noticeAdapter.notifyDataSetChanged();//refresh notice list
            }
        });

        if(refe!=null){
            Log.d("NoticeMngActivity", refe);
            noticeAdapter.notifyDataSetChanged();//refresh notice list
        }

    }


    /**public void openNoticeModification(NoticeGroup noticeGroup) {

        Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
        Bundle contentBundle = new Bundle();
        //contentBundle.putString("title", noticeGroup.getTitle());
        //contentBundle.putString("content", noticeGroup.getContent());
        //contentBundle.putInt("index", noticeGroup.getIndex());
        intent.putExtra("contentBundle", contentBundle);
        startActivityForResult(intent, NoticeWriteType.MODIFICATION.ordinal());
    }**/
    @Override
    public void onItemClick(int position){}
}