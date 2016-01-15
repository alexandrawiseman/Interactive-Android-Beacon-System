package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterNameActivity extends Activity {

    private EditText mNameEditText;
    private Button mSubmitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        mNameEditText = (EditText) findViewById(R.id.name_editText);

        mSubmitName = (Button) findViewById(R.id.submitname);
        mSubmitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mNameEditText.getText().toString().matches(""))
                {
                    //Bluetooth operated menu selects game to play
                    Intent pickerIntent = new Intent(EnterNameActivity.this, GamePickerActivity.class);
                    startActivity(pickerIntent);
                }
            }
        });

        Button backbutton = (Button) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
