package com.sweteam5.ladybugadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        RadioGroup loginType = findViewById(R.id.enterModeType);
        RadioButton radAdminMode = findViewById(R.id.radAdmin);
        RadioButton radDriverMode = findViewById(R.id.radDriver);

        EditText enterCodeEditText = findViewById(R.id.enterCodeEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = enterCodeEditText.getText().toString();

                Intent intent = null;
                if(radAdminMode.isChecked())
                {
                    intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                }
                else if(radDriverMode.isChecked())
                {
                    intent = new Intent(getApplicationContext(), DriverActivity.class);
                }

                /* ToDo: 서버로부터 code 와 radio button 모드가 유효한지 확인 후 결과에 따라 로그인 혹은 토스트메시지 띄우기 */

                if(intent != null)
                    startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(), "Select a login type", Toast.LENGTH_SHORT).show();
            }
        });
    }
}