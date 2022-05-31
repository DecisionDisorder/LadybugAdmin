package com.sweteam5.ladybugadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class noticeAdapter extends RecyclerView.Adapter<noticeAdapter.noticeViewHolder> {
    Context context;
    ArrayList<NoticeInfo> noticeArrayList;
    private AppCompatActivity activity;

    public noticeAdapter(Context context, ArrayList<NoticeInfo> noticeArrayList, AppCompatActivity activity){
        this.context = context;
        this.noticeArrayList = noticeArrayList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public noticeAdapter.noticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notice_group_layout, parent, false);
        return new noticeViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull noticeAdapter.noticeViewHolder holder, int position) {//notice object
        NoticeInfo notice = noticeArrayList.get(position);
        holder.title.setText(notice.title);
        if(notice.date.length() > 14)
            holder.date.setText(notice.date.substring(0, notice.date.length() - 3));
        else
            holder.date.setText(notice.date);
        //dm = new DataManage();

        holder.title.setOnClickListener(new View.OnClickListener() {//click the title to modify the notice
            @Override
            public void onClick(View view) {
                NoticeMngActivity.dm.findModifyNotice(activity, context, notice.title);
                //activity.finish();
                /**Intent intent = new Intent(context, NoticeWriteActivity.class);//error
                intent.putExtra("contentBundle", contentBundle);
                context.startActivity(intent);**/
            }
        });

        //delete_button을 누를 시 삭제
        holder.delete_button.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("공지 삭제").setMessage("이 공지를 삭제하겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //String id = dm.getidfromFireBase("notice", "title", notice.title);

                            NoticeMngActivity.dm.deleteNotice(activity, notice.title);//delete the notice from server
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

                });
        //holder.content.setText(notice.content);

    }


    @Override
    public int getItemCount() {
        return noticeArrayList.size();
    }

    public static class noticeViewHolder extends RecyclerView.ViewHolder{
        TextView title, date;
        ImageButton delete_button;

        public noticeViewHolder(@NonNull View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.noticeTitleTextView);
            date = itemView.findViewById(R.id.noticeDateTimeTextView);
            delete_button = itemView.findViewById(R.id.deleteNoticeButton);
        }
    }
}
