package com.sweteam5.ladybugadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class NoticeMngActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_mng);

        Button writeBtn = findViewById(R.id.writeButton);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
                startActivity(intent);
            }
        });

        createNoticeGroups();
    }

    private void createNoticeGroups()
    {
        int len = 3;
        LinearLayout noticeContainer = findViewById(R.id.noticeContainer);
        NoticeGroup[] noticeGroups = new NoticeGroup[len];

        for(int i = 0; i < len; i++){
            noticeGroups[i] = new NoticeGroup(getApplicationContext(), this);
            noticeContainer.addView(noticeGroups[i]);
        }
    }

    public void deleteNotice(NoticeGroup noticeGroup)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete notice").setMessage("Will you delete this notice?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: 게시글 삭제 서버 요청
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
        contentBundle.putString("title", "6월 10일 셔틀버스 운영중지 안내");
        contentBundle.putString("content", "2022년 6월 10일 우천으로 인하여 셔틀버스 운영을 중지합니다.");
        intent.putExtra("contentBundle", contentBundle);
        startActivity(intent);
    }
}