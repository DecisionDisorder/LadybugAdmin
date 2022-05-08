package com.sweteam5.ladybugadmin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CodeInfo extends LinearLayout {

    public EditText codeEditText;

    public CodeInfo(Context context, AttributeSet attrs, String title) {
        super(context, attrs);

        init(context, title);
    }

    public CodeInfo(Context context, String title) {
        super(context);

        init(context, title);
    }

    private void init(Context context, String title){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.info_code_layout, this, true);

        TextView codeTextView = findViewById(R.id.codeTextView);
        codeTextView.setText(title);
        codeEditText = findViewById(R.id.codeEditText);
    }
}
