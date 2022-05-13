package com.sweteam5.ladybugadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeWriteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);

        titleEditText = findViewById(R.id.noticeWriteTitleEditText);
        contentEditText = findViewById(R.id.noticeWriteContentEditText);

        Button uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 서버에 수정/작성된 내용 업로드
                Intent noticeIntent = new Intent();
                Bundle noticeContentBundle = new Bundle();
                noticeContentBundle.putString("title", titleEditText.getText().toString());
                noticeContentBundle.putString("content", contentEditText.getText().toString());
                noticeContentBundle.putString("dateTime", getDateTime());
                if(index > 0)
                    noticeContentBundle.putInt("index", index);
                noticeIntent.putExtra("noticeContentBundle", noticeContentBundle);
                setResult(NoticeWriteType.UPLOAD.ordinal(), noticeIntent);
                finish();
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