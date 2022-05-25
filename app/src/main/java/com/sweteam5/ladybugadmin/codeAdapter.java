/*package com.sweteam5.ladybugadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class codeAdapter extends RecyclerView.Adapter<codeAdapter.codeViewHolder> {
    Context context;
    ArrayList<CodeInfo> codeArrayList;
    private DataManage dm;
    public codeAdapter(Context context, ArrayList<CodeInfo> codeArrayList){
        this.context = context;
        this.codeArrayList =codeArrayList;
    }


    @NonNull
    @Override
    public codeAdapter.codeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notice_group_layout, parent, false);
        return new codeAdapter.codeViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull codeAdapter.codeViewHolder holder, int position) {//notice object
        CodeInfo code = codeArrayList.get(position);
        holder.codetype.setText(code.codeType);
        holder.code.setText(code.code);
    }


    @Override
    public int getItemCount() {
        return codeArrayList.size();
    }

    public static class codeViewHolder extends RecyclerView.ViewHolder{
        TextView codetype, code;

        public codeViewHolder(@NonNull View itemView) {
            super(itemView);
            codetype =  itemView.findViewById(R.id.codenameTextView);
            code = itemView.findViewById(R.id.codeTextView);
        }
    }
}*/
