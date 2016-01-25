package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CameraSubmitActivity extends Activity
{

    private ImageView mUserImage;
    private Button mSubmitButton;
    private Button mRedoButton;
    private boolean[] complete = new boolean[3];
    private int studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_submit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if(getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            complete = getIntent().getBooleanArrayExtra("GamesComplete");
        }

        studentID = getIntent().getIntExtra("gamePickerID", 0);
        Log.d("GamePickerActivity", "Student ID: " + studentID);

        mUserImage = (ImageView) findViewById(R.id.userImage);
        mSubmitButton = (Button) findViewById(R.id.submit_picture);
        mRedoButton = (Button) findViewById(R.id.redo_picture);

        Bitmap b = CommonResources.photo;
        if (b != null)
        {
            mUserImage.setImageBitmap(b);
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent submitIntent = new Intent(CameraSubmitActivity.this, GamePickerActivity.class);
                submitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                complete[1] = true;
                submitIntent.putExtra("GamesComplete", complete);
                submitIntent.putExtra("gamePickerID", studentID);
                startActivity(submitIntent);
            }
        });

        mRedoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent redoIntent = new Intent(CameraSubmitActivity.this, CameraActivity.class);
                redoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(redoIntent);
            }
        });
    }

}
