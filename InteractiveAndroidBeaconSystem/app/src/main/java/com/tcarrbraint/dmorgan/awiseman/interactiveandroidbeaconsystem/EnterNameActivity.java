package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterNameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        Button submitname = (Button) findViewById(R.id.submitname);
        submitname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bluetooth operated menu selects game to play
                Intent pickerIntent = new Intent(EnterNameActivity.this, GamePickerActivity.class);
                startActivity(pickerIntent);
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
