package com.sweteam5.ladybugadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference_admin;
    private DatabaseReference databaseReference_driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference_admin = FirebaseDatabase.getInstance().getReference("admin");
        databaseReference_driver = FirebaseDatabase.getInstance().getReference("driver");

        EditText checkId = findViewById(R.id.checkId);

        Button loginButton = findViewById(R.id.loginButton);
        RadioGroup loginType = findViewById(R.id.enterModeType);
        RadioButton radAdminMode = findViewById(R.id.radAdmin);
        RadioButton radDriverMode = findViewById(R.id.radDriver);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radAdminMode.isChecked()) {
                    databaseReference_admin.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                            while (child.hasNext()) {
                                if (child.next().getKey().equals(checkId.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "로그인!", Toast.LENGTH_LONG).show();

                                    Intent intent = null;
                                    intent = new Intent(getApplicationContext(), AdminMainActivity.class);

                                    if (intent != null)
                                        startActivity(intent);
                                    else
                                        Toast.makeText(getApplicationContext(), "로그인 종류를 정해주세요.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            Toast.makeText(getApplicationContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                else if(radDriverMode.isChecked()){
                    databaseReference_driver.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                            while (child.hasNext()) {
                                if (child.next().getKey().equals(checkId.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "로그인!", Toast.LENGTH_LONG).show();

                                    Intent intent = null;
                                    intent = new Intent(getApplicationContext(), DriverActivity.class);

                                    if (intent != null)
                                        startActivity(intent);
                                    else
                                        Toast.makeText(getApplicationContext(), "로그인 정류를 정해주세요.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            Toast.makeText(getApplicationContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}