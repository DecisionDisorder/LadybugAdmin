package com.sweteam5.ladybugadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

public class NoticeGroup extends LinearLayout {

    private TextView noticeTitleTextView;
    private TextView noticeDateTimeTextView;
    private ImageButton deleteNoticeButton;

    private String title = "6월 10일 셔틀버스 운영중지 안내";
    private String dateTime = "2022.06.10 11:20";
    private String content = "2022년 6월 10일 우천으로 인하여 셔틀버스 운영을 중지합니다.";
    private int index = 0;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getContent() {
        return content;
    }

    public NoticeGroup(Context context, AttributeSet attrs, NoticeMngActivity noticeMngActivity) {
        super(context, attrs);

        init(context, noticeMngActivity);
    }

    public NoticeGroup(Context context, NoticeMngActivity noticeMngActivity) {
        super(context);

        init(context, noticeMngActivity);
    }

    private void init(Context context, NoticeMngActivity noticeMngActivity){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.notice_group_layout, this, true);

        noticeTitleTextView = findViewById(R.id.noticeTitleTextView);
        noticeTitleTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 서버로부터 공지 내용을 받아와서 수정 화면 인플레이션
                noticeMngActivity.openNoticeModification(NoticeGroup.this);
            }
        });

        noticeDateTimeTextView = findViewById(R.id.noticeDateTimeTextView);

        deleteNoticeButton = findViewById(R.id.deleteNoticeButton);
        deleteNoticeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeMngActivity.deleteNotice(NoticeGroup.this);
            }
        });
    }

    public void setContents(String title, String dateTime, String content) {
        this.title = title;
        this.dateTime = dateTime;
        this.content = content;
        noticeTitleTextView.setText(title);
        noticeDateTimeTextView.setText(dateTime);
    }

}
