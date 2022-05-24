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

public class CodeInfo {

    private MngInfoActivity mngInfoActivity;

    String codeType;
    String code;
    public EditText codeEditText;
    private TextView codeTextView;

    public CodeInfo(String codeType, String code) {
        this.codeType = codeType;
        this.mngInfoActivity = mngInfoActivity;
    }
    public String getCodeType(){return codeType;}
    public void setCodeType(String codeType){this.codeType = codeType;}
    public String getcode(){return this.code;}
    public void setcode(String code){this.code = code;}
}
