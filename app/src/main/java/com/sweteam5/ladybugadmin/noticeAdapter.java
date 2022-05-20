package com.sweteam5.ladybugadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.ArrayList;

public class noticeAdapter extends RecyclerView.Adapter<noticeAdapter.noticeViewHolder> {
    Context context;
    ArrayList<NoticeInfo> noticeArrayList;
    FirebaseFirestore db;

    public noticeAdapter(Context context, ArrayList<NoticeInfo> noticeArrayList){
        this.context = context;
        this.noticeArrayList = noticeArrayList;
    }


    @NonNull
    @Override
    public noticeAdapter.noticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.notice_group_layout, parent, false);
        return new noticeViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull noticeAdapter.noticeViewHolder holder, int position) {
        NoticeInfo notice = noticeArrayList.get(position);

        holder.title.setText(notice.title);
        holder.date.setText(notice.date);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //notice를 자세히 보는 창으로 이동
                Intent intent = new Intent(context, NoticeMngActivity.class);
                Log.d("noticeAdapter", "err:"+notice.title);
                intent.putExtra("modify", notice.title);
                context.startActivity(intent);
            }
        });


        //delete_button을 누를 시 삭제
        holder.delete_button.setOnClickListener(v->{
            db = FirebaseFirestore.getInstance();
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle("Delete notice").setMessage("Will you delete this notice?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // TODO: 게시글 삭제 서버 요청

                            Intent intent = new Intent(context, NoticeMngActivity.class);
                            Log.d("noticeAdapter", "err:"+notice.title);
                            intent.putExtra("delete", notice.title);
                            context.startActivity(intent);
                        }


                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
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
            //content = itemView.findViewById(R.id.noticeWriteContentEditText);
        }
    }
}
