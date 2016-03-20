package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.HashMap;

public class CatchPaintingActivity extends Activity
{

    protected GameSurfaceView gameView;
    private boolean[] complete = new boolean[3];
    private int studentID;
    private int score;
    private String location = "Mona Lisa";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        if (getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            complete = getIntent().getBooleanArrayExtra("GamesComplete");
        }

        studentID = getIntent().getIntExtra("gamePickerID", 0);
        score = getIntent().getIntExtra("gamePickerScore", 0);
        Log.d("GamePickerActivity", "Student ID: " + studentID);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameView = new GameSurfaceView(this);
        setContentView(gameView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gameView.resume();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gameView.pause();
        score = score + 1;
        updateStudent();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void updateStudent()
    {

        class UpdateStudent extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                Toast.makeText(CatchPaintingActivity.this, s, Toast.LENGTH_LONG).show();
                Intent submitIntent = new Intent(CatchPaintingActivity.this, GamePickerActivity.class);
                submitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                complete[2] = true;
                submitIntent.putExtra("GamesComplete", complete);
                submitIntent.putExtra("gamePickerID", studentID);
                submitIntent.putExtra("gamePickerScore", score);
                startActivity(submitIntent);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID, Integer.toString(studentID));
                hashMap.put(Config.KEY_EMP_DESG, location);
                hashMap.put(Config.KEY_EMP_SAL, Integer.toString(score));

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP, hashMap);

                return s;
            }
        }

        UpdateStudent ue = new UpdateStudent();
        ue.execute();
    }


}
