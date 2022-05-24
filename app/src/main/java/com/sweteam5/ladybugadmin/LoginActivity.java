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

    private FirebaseParser<AdminCodeInfo> adminCodeInfoFirebaseParser = new FirebaseParser<>();
    private FirebaseParser<DriverCodeInfo> driverCodeInfoFirebaseParser = new FirebaseParser<>();
    private EditText enterCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        RadioButton radAdminMode = findViewById(R.id.radAdmin);
        RadioButton radDriverMode = findViewById(R.id.radDriver);

        enterCodeEditText = findViewById(R.id.enterCodeEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = enterCodeEditText.getText().toString();

                Intent intent = null;
                if(radAdminMode.isChecked())
                {
                    adminLogin(enterCodeEditText.getText().toString());
                }
                else if(radDriverMode.isChecked())
                {
                    intent = new Intent(getApplicationContext(), DriverActivity.class);
                }

                /* ToDo: 서버로부터 code 와 radio button 모드가 유효한지 확인 후 결과에 따라 로그인 혹은 토스트메시지 띄우기 */

            }
        });
    }

    private void adminLogin(String code) {
        AdminCodeInfo adminCodeInfo = new AdminCodeInfo();
        adminCodeInfo = adminCodeInfoFirebaseParser.getfromFirebase("AdminCode", "admin_code", adminCodeInfo);

        if(adminCodeInfo.getCode().equals(code)) {

            Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);

            if(intent != null)
                startActivity(intent);
            else
                Toast.makeText(getApplicationContext(), "Select a login type", Toast.LENGTH_SHORT).show();
        }
    }
    private void driverLogin() {

    }
}