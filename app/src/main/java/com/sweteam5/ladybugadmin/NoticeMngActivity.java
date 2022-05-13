package com.sweteam5.ladybugadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class NoticeMngActivity extends AppCompatActivity {

    private ArrayList<NoticeGroup> noticeList;
    private LinearLayout noticeContainer;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == NoticeWriteType.WRITE_NEW.ordinal()) {
            if(data != null) {
                // TODO: title, content, datetime 서버 전송

                Bundle noticeBundle = data.getBundleExtra("noticeContentBundle");
                String title = noticeBundle.getString("title");
                String dateTime = noticeBundle.getString("dateTime");
                String content = noticeBundle.getString("content");

                addNoticeGroup(title, dateTime, content);
            }
        }
        else if(requestCode == NoticeWriteType.MODIFICATION.ordinal()) {
            if(data != null) {
                Bundle noticeBundle = data.getBundleExtra("noticeContentBundle");
                String title = noticeBundle.getString("title");
                String dateTime = noticeBundle.getString("dateTime");
                String content = noticeBundle.getString("content");

                int index = noticeBundle.getInt("index");
                modifyNoticeGroup(index, title, dateTime, content);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_mng);

        Button writeBtn = findViewById(R.id.writeButton);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
                startActivityForResult(intent, NoticeWriteType.WRITE_NEW.ordinal());
            }
        });

        createNoticeGroups();
    }

    private void createNoticeGroups()
    {
        // TODO: 서버로부터 게시글 개수와 내용, 작성시간 리스트를 받아와서 생성
        int len = 3;
        noticeContainer = findViewById(R.id.noticeContainer);
        noticeList = new ArrayList<NoticeGroup>();

        for(int i = 0; i < len; i++){
            NoticeGroup noticeGroup = new NoticeGroup(getApplicationContext(), this);
            noticeGroup.setIndex(i);
            noticeList.add(noticeGroup);
            noticeContainer.addView(noticeGroup);
        }
    }

    private NoticeGroup findNoticeByIndex(int index) {

        return null;
    }

    private void addNoticeGroup(String title, String dateTime, String content) {
        NoticeGroup noticeGroup = new NoticeGroup(getApplicationContext(), this);
        noticeGroup.setContents(title, dateTime, content);
        noticeGroup.setIndex(noticeList.get(noticeList.size() - 1).getIndex() + 1);
        noticeList.add(noticeGroup);
        noticeContainer.addView(noticeGroup);
    }

    private void modifyNoticeGroup(int index, String title, String dateTime, String content) {

        for(int i = 0; i < noticeList.size(); i++) {
            if (noticeList.get(i).getIndex() == index) {
                noticeList.get(i).setContents(title, dateTime, content);
                break;
            }
        }
    }

    private void removeNoticeGroup(NoticeGroup noticeGroup) {
        noticeContainer.removeView(noticeGroup);
        noticeList.remove(noticeGroup);
    }

    public void deleteNotice(NoticeGroup noticeGroup)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete notice").setMessage("Will you delete this notice?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: 게시글 삭제 서버 요청
                        removeNoticeGroup(noticeGroup);
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
        contentBundle.putString("title", noticeGroup.getTitle());
        contentBundle.putString("content", noticeGroup.getContent());
        contentBundle.putInt("index", noticeGroup.getIndex());
        intent.putExtra("contentBundle", contentBundle);
        startActivityForResult(intent, NoticeWriteType.MODIFICATION.ordinal());
    }
}