package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

public class CameraSubmitActivity extends Activity
{

    private ImageView mUserImage;
    private Button mSubmitButton;
    private Button mRedoButton;
    private boolean[] complete = new boolean[3];
    private int studentID;
    private int score;
    private String location = "Statue of Liberty";

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
        score = getIntent().getIntExtra("gamePickerScore", 0);
        Log.d("GamePickerActivity", "Student ID Camera: " + studentID);

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
                score = score + 1;
                updateStudent();
                submitIntent.putExtra("GamesComplete", complete);
                submitIntent.putExtra("gamePickerID", studentID);
                submitIntent.putExtra("gamePickerScore", score);
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

    private void updateStudent(){

        class UpdateStudent extends AsyncTask<Void,Void,String>
        {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CameraSubmitActivity.this,"Loading...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(CameraSubmitActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID,Integer.toString(studentID));
                hashMap.put(Config.KEY_EMP_DESG,location);
                hashMap.put(Config.KEY_EMP_SAL,Integer.toString(score));

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        UpdateStudent ue = new UpdateStudent();
        ue.execute();
    }

}
