package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeWriteActivity extends AppCompatActivity {
    private static final String TAG = "NoticeWriteActivity";
    private EditText titleEditText;
    private EditText contentEditText;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        titleEditText = findViewById(R.id.noticeWriteTitleEditText);
        contentEditText = findViewById(R.id.noticeWriteContentEditText);

        Button uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 서버에 수정/작성된 내용 업로드
                String title = titleEditText.getText().toString();
                long now = System.currentTimeMillis();
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = (String)mFormat.format(new Date(now));
                String content = contentEditText.getText().toString();
                NoticeInfo noticeInfo = new NoticeInfo(title,date, content);
                finish();
                db.collection("notice").add(noticeInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written ID"+documentReference.getId());
                                Toast.makeText(getApplicationContext(), "업로드 성공", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(getApplicationContext(), "업로드 실패", Toast.LENGTH_LONG).show();
                            }
                        });
                Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                startActivity(intent);
            }
        });

        writeExistingContents();
    }

    private String getDateTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm");
        return format.format(date);
    }

    private void writeExistingContents()
    {
        EditText noticeWriteTitleEditText = findViewById(R.id.noticeWriteTitleEditText);
        EditText noticeWriteContentEditText = findViewById(R.id.noticeWriteContentEditText);

        Intent existingIntent = getIntent();
        Bundle existingBundle = existingIntent.getBundleExtra("contentBundle");
        if (existingBundle != null) {
            String title = existingBundle.getString("title");
            String content = existingBundle.getString("content");
            index = existingBundle.getInt("index");
            noticeWriteTitleEditText.setText(title);
            noticeWriteContentEditText.setText(content);
        }
    }
}