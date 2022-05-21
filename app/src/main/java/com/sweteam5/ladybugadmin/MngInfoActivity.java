package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MngInfoActivity extends AppCompatActivity {

    private ArrayList<CodeInfo> driverInfoList;
    private ArrayList<CodeInfo> busInfoList;
    private EditText driver_code;
    private EditText admin_code;
    private DatabaseReference db;


    private String[] codeTypes = {"driver", "bus"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_info);
        driverInfoList = new ArrayList<CodeInfo>();
        busInfoList = new ArrayList<CodeInfo>();
        admin_code = findViewById(R.id.adminCodeEditText);
        driver_code = findViewById(R.id.DriverCodeEditText);
        ImageButton del_driver_code = findViewById(R.id.deleteDriverCodeButton);
        ImageButton del_admin_code = findViewById(R.id.deleteAdminCodeButton);
        Button saveInfoButton = findViewById(R.id.saveInfoButton);
        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MngInfoActivity.this);
                builder.setTitle("Save Information").setMessage("Will you save management information?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db = FirebaseDatabase.getInstance().getReference();
                                String drivercode = driver_code.getText().toString();
                                String toast=null;
                                if(drivercode.equals("")){

                                }
                                else{
                                    db.child("driver").child(drivercode).setValue("");
                                    toast+="drivercode "+drivercode+" added";
                                }
                                String admincode = admin_code.getText().toString();
                                if(admincode.equals("")){
                                    Log.d("MngInfoActivity", "null!!!!");
                                }
                               else{
                                    db.child("admin").child(admincode).setValue("");
                                    toast+="admincode "+admincode+" added";
                                }
                                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        del_driver_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String drivercode = driver_code.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MngInfoActivity.this);
                builder.setTitle("Delete Code").setMessage("Will you delete the code?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!drivercode.equals("")) {
                                    db = FirebaseDatabase.getInstance().getReference("driver");
                                    db.child(drivercode).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(MngInfoActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(MngInfoActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(getApplicationContext(), "Write the code to delete!", Toast.LENGTH_LONG).show();
                                }
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
        });
        del_admin_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                del_admin_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String admincode = admin_code.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MngInfoActivity.this);
                        builder.setTitle("Delete Code").setMessage("Will you delete the code?")
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(!admincode.equals("")) {
                                            db = FirebaseDatabase.getInstance().getReference("admin");
                                            db.child(admincode).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(MngInfoActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(MngInfoActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Write the code to delete!", Toast.LENGTH_LONG).show();
                                        }
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
                });
            }
        });
    }



    private void updateTitleAll(ArrayList<CodeInfo> list, String title) {
        for(int i = 0; i < list.size(); i++) {
            list.get(i).setTitle(title + " " + i);
        }
    }
}