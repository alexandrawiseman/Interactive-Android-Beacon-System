package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CameraInstructionActivity extends Activity
{

    private Button mBeginButton;
    private int studentID;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_instruction);

        studentID = getIntent().getIntExtra("gamePickerID", 0);
        score = getIntent().getIntExtra("gamePickerScore", 0);

        mBeginButton = (Button) findViewById(R.id.begin_button);
        mBeginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(CameraInstructionActivity.this, CameraActivity.class);
                cameraIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                cameraIntent.putExtra("gamePickerID", studentID);
                cameraIntent.putExtra("gamePickerScore", score);
                startActivity(cameraIntent);
            }
        });
    }
}
