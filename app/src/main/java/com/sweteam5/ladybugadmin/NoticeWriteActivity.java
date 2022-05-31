package com.sweteam5.ladybugadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeWriteActivity extends AppCompatActivity {
    //private DataManage dm;
    private static final String TAG = "NoticeWriteActivity";
    private EditText titleEditText;
    private EditText contentEditText;
    private String DocumentID=null;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);

        titleEditText = findViewById(R.id.noticeWriteTitleEditText);
        contentEditText = findViewById(R.id.noticeWriteContentEditText);
        Button uploadBtn = findViewById(R.id.uploadBtn);
        writeExistingContents();
        AppCompatActivity activity = this;
        //If click upload button
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                long now = System.currentTimeMillis();
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = (String)mFormat.format(new Date(now));
                String content = contentEditText.getText().toString();//get title, contents, date
                //dm = new DataManage();
                if(DocumentID==null) {
                    NoticeMngActivity.dm.uploadNotice(title, date, content);//push to store
                    finish();
                }
                else{
                    NoticeMngActivity.dm.uploadModification(activity, title, date, content, DocumentID);
                    finish();
                }

            }
        });

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
            DocumentID = existingBundle.getString("documentid");
            noticeWriteTitleEditText.setText(title);
            noticeWriteContentEditText.setText(content);
        }
    }
}