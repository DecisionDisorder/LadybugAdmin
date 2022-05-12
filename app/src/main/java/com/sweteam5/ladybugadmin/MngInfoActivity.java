package com.sweteam5.ladybugadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MngInfoActivity extends AppCompatActivity {

    private ArrayList<CodeInfo> driverInfoList;
    private ArrayList<CodeInfo> busInfoList;
    private LinearLayout driverCodeContainer;
    private LinearLayout busCodeContainer;

    private String[] codeTypes = {"driver", "bus"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_info);

        driverCodeContainer = findViewById(R.id.driverCodeContainer);
        busCodeContainer = findViewById(R.id.busCodeContainer);

        driverInfoList = new ArrayList<CodeInfo>();
        busInfoList = new ArrayList<CodeInfo>();

        Button addDriverButton = findViewById(R.id.addDriverButton);
        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDriverCode();
            }
        });

        Button addBusButton = findViewById(R.id.addBusButton);
        addBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBusCode();
            }
        });

        Button saveInfoButton = findViewById(R.id.saveInfoButton);
        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSave();
            }
        });
    }

    private void addDriverCode() {
        CodeInfo newDriverCode = new CodeInfo(getApplicationContext(), getResources().getString(R.string.driver)
                + " " + driverInfoList.size(), codeTypes[0], this);
        driverInfoList.add(newDriverCode);
        driverCodeContainer.addView(newDriverCode);
    }
    private void addBusCode() {
        CodeInfo newBusCode = new CodeInfo(getApplicationContext(), getResources().getString(R.string.bus)
                + " " + busInfoList.size(), codeTypes[1], this);
        busInfoList.add(newBusCode);
        busCodeContainer.addView(newBusCode);
    }

    private void confirmSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Information").setMessage("Will you save management information?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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

    public void deleteCode(String type, CodeInfo codeInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Code").setMessage("Will you delete the code?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(type.equals("driver")) {
                            // TODO: 서버에 삭제 요청 코드
                            driverCodeContainer.removeView(codeInfo);
                            driverInfoList.remove(codeInfo);
                            updateTitleAll(driverInfoList, getResources().getString(R.string.driver));
                        }
                        else if(type.equals("bus")) {
                            // TODO: 서버에 삭제 요청 코드
                            busCodeContainer.removeView(codeInfo);
                            busInfoList.remove(codeInfo);
                            updateTitleAll(busInfoList, getResources().getString(R.string.bus));
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
}