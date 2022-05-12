package com.sweteam5.ladybugadmin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CodeInfo extends LinearLayout {

    private MngInfoActivity mngInfoActivity;
    private String codeType;
    public EditText codeEditText;
    public ImageButton deleteCodeButton;
    private TextView codeTextView;

    public CodeInfo(Context context, AttributeSet attrs, String title,
                    String codeType, MngInfoActivity mngInfoActivity) {
        super(context, attrs);

        this.codeType = codeType;
        this.mngInfoActivity = mngInfoActivity;

        init(context, title);
    }

    public CodeInfo(Context context, String title,
                    String codeType, MngInfoActivity mngInfoActivity) {
        super(context);

        this.codeType = codeType;
        this.mngInfoActivity = mngInfoActivity;

        init(context, title);
    }

    private void init(Context context, String title){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.info_code_layout, this, true);

        codeTextView = findViewById(R.id.codeTextView);
        setTitle(title);
        codeEditText = findViewById(R.id.codeEditText);

        deleteCodeButton = findViewById(R.id.deleteCodeButton);
        deleteCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mngInfoActivity.deleteCode(codeType, CodeInfo.this);
            }
        });
    }

    public void setTitle(String title) {
        codeTextView.setText(title);
    }
}
