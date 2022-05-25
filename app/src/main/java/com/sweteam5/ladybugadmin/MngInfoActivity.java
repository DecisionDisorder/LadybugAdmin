package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MngInfoActivity extends AppCompatActivity{
    private ArrayList<CodeInfo> driverInfoList;
    private ArrayList<CodeInfo> adminInfoList;
    private LinearLayout driverCodeContainer;
    private LinearLayout adminCodeContainer;

    private DatabaseReference db;
    private DataManage dm;

    public enum CodeType {ADMIN, DRIVER}

    private ArrayList<String> adminCodes;
    private ArrayList<String> driverCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_info);

        adminCodeContainer = findViewById(R.id.adminCodeContainer);
        driverCodeContainer = findViewById(R.id.driverCodeContainer);

        adminInfoList = new ArrayList<CodeInfo>();
        driverInfoList = new ArrayList<CodeInfo>();
        adminCodes = new ArrayList<>();
        driverCodes = new ArrayList<>();

        Button addAdminButton = findViewById(R.id.addAdminButton);
        addAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = new EditText(MngInfoActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MngInfoActivity.this);
                dlg.setTitle("Add admin Code");
                dlg.setView(input);
                dlg.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addAdminCodeOnServer(input.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = dlg.create();
                alert.show();
            }
        });

        Button addDriverButton = findViewById(R.id.addDriverButton);
        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText input = new EditText(MngInfoActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MngInfoActivity.this);
                dlg.setTitle("Add driver Code").setView(input).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addDriverCodeOnServer(input.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = dlg.create();
                alert.show();
            }
        });

        FirebaseDatabase.getInstance().getReference("admin").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    System.out.println(task.getResult().getValue().toString());
                    String[] codes = getCodeList(task.getResult().getValue().toString());

                    for(int i = 0; i < codes.length; i++) {
                        adminCodes.add(codes[i]);
                    }

                    initAdminCode();
                }
            }
        });

        FirebaseDatabase.getInstance().getReference("driver").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    System.out.println(task.getResult().getValue().toString());
                    String[] codes = getCodeList(task.getResult().getValue().toString());

                    for(int i = 0; i < codes.length; i++) {
                        driverCodes.add(codes[i]);
                    }

                    initDriverCode();
                }
            }
        });
    }

    private void initAdminCode() {
        for(int i = 0; i < adminCodes.size(); i++) {
            addAdminCode();
            adminInfoList.get(i).initCode(adminCodes.get(i));
        }
    }

    private void initDriverCode() {
        for(int i = 0; i < driverCodes.size(); i++) {
            addDriverCode();
            driverInfoList.get(i).initCode(driverCodes.get(i));
        }
    }

    private String[] getCodeList(String codeInDictionary) {
        String[] list = codeInDictionary.split(",");
        for(int i = 0; i < list.length; i++) {
            list[i] = list[i].replaceAll("[^0-9]", "");
        }
        return list;
    }

    private void addDriverCode() {
        CodeInfo newDriverCode = new CodeInfo(getApplicationContext(), getResources().getString(R.string.driver)
                + " " + driverInfoList.size(), CodeType.DRIVER, this);
        driverInfoList.add(newDriverCode);
        driverCodeContainer.addView(newDriverCode);
    }
    private void addDriverCode(String code) {
        addDriverCode();
        driverCodes.add(code);
        driverInfoList.get(driverInfoList.size() - 1).initCode(code);
    }


    private void addAdminCode() {
        CodeInfo newBusCode = new CodeInfo(getApplicationContext(), getResources().getString(R.string.admin_abbrev)
                + " " + adminInfoList.size(), CodeType.ADMIN, this);
        adminInfoList.add(newBusCode);
        adminCodeContainer.addView(newBusCode);
    }
    private void addAdminCode(String code) {
        addAdminCode();
        adminCodes.add(code);
        adminInfoList.get(adminInfoList.size() - 1).initCode(code);
    }

    public void deleteCode(MngInfoActivity.CodeType type, CodeInfo codeInfo) {
        if(type == CodeType.ADMIN && adminCodes.size() <= 1) {
            Toast.makeText(this, "There must be at least one admin code.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Code").setMessage("Will you delete the code?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(type == CodeType.DRIVER) {
                            db = FirebaseDatabase.getInstance().getReference("driver");
                            db.child(codeInfo.getCodeOnEditText()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MngInfoActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();

                                        driverCodeContainer.removeView(codeInfo);
                                        driverInfoList.remove(codeInfo);
                                        driverCodes.remove(codeInfo.getCodeOnEditText());
                                        updateTitleAll(driverInfoList, getResources().getString(R.string.driver));
                                    }
                                    else{
                                        Toast.makeText(MngInfoActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else if(type == CodeType.ADMIN) {
                            db = FirebaseDatabase.getInstance().getReference("admin");
                            db.child(codeInfo.getCodeOnEditText()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MngInfoActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();

                                        adminCodeContainer.removeView(codeInfo);
                                        adminInfoList.remove(codeInfo);
                                        adminCodes.remove(codeInfo.getCodeOnEditText());
                                        updateTitleAll(adminInfoList, getResources().getString(R.string.admin_abbrev));
                                    }
                                    else{
                                        Toast.makeText(MngInfoActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
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

    private void updateTitleAll(ArrayList<CodeInfo> list, String title) {
        for(int i = 0; i < list.size(); i++) {
            list.get(i).setTitle(title + " " + i);
        }
    }

    public void addAdminCodeOnServer(String code) {
        db = FirebaseDatabase.getInstance().getReference();
        db.child("admin").child(code).setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                addAdminCode(code);

                String toast = "Admin code " + code + " added";

                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addDriverCodeOnServer(String code) {
        db = FirebaseDatabase.getInstance().getReference();
        db.child("driver").child(code).setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                addDriverCode(code);
                String toast = "Driver code " + code + " added";

                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
            }
        });

    }
}