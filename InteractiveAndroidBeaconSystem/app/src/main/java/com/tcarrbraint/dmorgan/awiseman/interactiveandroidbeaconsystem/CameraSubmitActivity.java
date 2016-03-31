package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class CameraSubmitActivity extends Activity
{

    private ImageView mUserImage;
    private Button mSubmitButton;
    private Button mRedoButton;
    private boolean[] mComplete = new boolean[3];
    private int mStudentID;
    private int mScore;
    private String mLocation = "Statue of Liberty";
    private String mImageEncoded;
    private Bitmap mB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_submit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            mComplete = getIntent().getBooleanArrayExtra("GamesComplete");
        }
        mStudentID = getIntent().getIntExtra("gamePickerID", 0);
        mScore = getIntent().getIntExtra("gamePickerScore", 0);
        Log.d("GamePickerActivity", "Student ID Camera: " + mStudentID);

        mUserImage = (ImageView) findViewById(R.id.userImage);
        mSubmitButton = (Button) findViewById(R.id.submit_picture);
        mRedoButton = (Button) findViewById(R.id.redo_picture);

        mB = CommonResources.photo;
        if (mB != null)
        {
            mUserImage.setImageBitmap(mB);
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mImageEncoded = getStringImage(mB);
                addImage();
            }
        });

        mRedoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent redoIntent = new Intent(CameraSubmitActivity.this, CameraActivity.class);
                redoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                redoIntent.putExtra("GamesComplete", mComplete);
                redoIntent.putExtra("gamePickerID", mStudentID);
                redoIntent.putExtra("gamePickerScore", mScore);
                startActivity(redoIntent);
            }
        });
    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
                loading = ProgressDialog.show(CameraSubmitActivity.this, "Loading...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(CameraSubmitActivity.this, s, Toast.LENGTH_LONG).show();
                Intent submitIntent = new Intent(CameraSubmitActivity.this, GamePickerActivity.class);
                submitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                submitIntent.putExtra("GamesComplete", mComplete);
                submitIntent.putExtra("gamePickerID", mStudentID);
                submitIntent.putExtra("gamePickerScore", mScore);
                startActivity(submitIntent);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID, Integer.toString(mStudentID));
                hashMap.put(Config.KEY_EMP_DESG, mLocation);
                hashMap.put(Config.KEY_EMP_SAL, Integer.toString(mScore));

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP, hashMap);

                return s;
            }
        }

        UpdateStudent ue = new UpdateStudent();
        ue.execute();
    }

    private void addImage()
    {

        class AddImage extends AsyncTask<Void, Void, String>
        {

            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(CameraSubmitActivity.this, "Adding...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                mComplete[1] = true;
                mScore = mScore + 1;
                updateStudent();
            }

            @Override
            protected String doInBackground(Void... v)
            {
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_EMP_NAME, System.currentTimeMillis() + ".jpg");
                params.put(Config.KEY_EMP_IMG, mImageEncoded);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_IMAGE, params);
                return res;
            }
        }

        AddImage ae = new AddImage();
        ae.execute();
    }

}
