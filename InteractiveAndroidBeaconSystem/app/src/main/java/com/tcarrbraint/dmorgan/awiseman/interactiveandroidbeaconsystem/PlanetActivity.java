package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PlanetActivity extends Activity
{
    private Button mButtons[] = new Button[8];
    private Button mSubmitButton, mClearButton;
    private int mPlanetNumber;
    private List<Integer> mPlanetList;
    private HashMap mHm;
    private boolean[] mComplete = new boolean[3];
    private int mStudentID;
    private int mScores;
    private String mLocation = "Planets";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);

        if (getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            mComplete = getIntent().getBooleanArrayExtra("GamesComplete");
        }

        mStudentID = getIntent().getIntExtra("gamePickerID", 0);
        mScores = getIntent().getIntExtra("gamePickerScore", 0);

        mButtons[0] = (Button) findViewById(R.id.b1);
        mButtons[1] = (Button) findViewById(R.id.b2);
        mButtons[2] = (Button) findViewById(R.id.b3);
        mButtons[3] = (Button) findViewById(R.id.b4);
        mButtons[4] = (Button) findViewById(R.id.b5);
        mButtons[5] = (Button) findViewById(R.id.b6);
        mButtons[6] = (Button) findViewById(R.id.b7);
        mButtons[7] = (Button) findViewById(R.id.b8);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mClearButton = (Button) findViewById(R.id.clearButton);

        mPlanetNumber = 1;

        mHm = new HashMap();
        mHm.put(R.drawable.mercury, "1");
        mHm.put(R.drawable.venus, "2");
        mHm.put(R.drawable.earth, "3");
        mHm.put(R.drawable.mars, "4");
        mHm.put(R.drawable.jupiter, "5");
        mHm.put(R.drawable.saturn, "6");
        mHm.put(R.drawable.uranus, "7");
        mHm.put(R.drawable.neptune, "8");

        // Initialize order of planets
        mPlanetList = Arrays.asList(R.drawable.mercury, R.drawable.venus, R.drawable.earth, R.drawable.mars, R.drawable.jupiter, R.drawable.saturn, R.drawable.uranus, R.drawable.neptune);
        Collections.shuffle(mPlanetList);

        mButtons[0].setBackground(getResources().getDrawable(mPlanetList.get(0)));
        mButtons[1].setBackground(getResources().getDrawable(mPlanetList.get(1)));
        mButtons[2].setBackground(getResources().getDrawable(mPlanetList.get(2)));
        mButtons[3].setBackground(getResources().getDrawable(mPlanetList.get(3)));
        mButtons[4].setBackground(getResources().getDrawable(mPlanetList.get(4)));
        mButtons[5].setBackground(getResources().getDrawable(mPlanetList.get(5)));
        mButtons[6].setBackground(getResources().getDrawable(mPlanetList.get(6)));
        mButtons[7].setBackground(getResources().getDrawable(mPlanetList.get(7)));

        mButtons[0].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPlanetNumber < 9 && mButtons[0].getText().toString() == "")
                {
                    mButtons[0].setText(Integer.toString(mPlanetNumber));
                    mPlanetNumber++;
                }
            }
        });

        mButtons[1].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPlanetNumber < 9 && mButtons[1].getText().toString() == "")
                {
                    mButtons[1].setText(Integer.toString(mPlanetNumber));
                    mPlanetNumber++;
                }
            }
        });

        mButtons[2].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPlanetNumber < 9 && mButtons[2].getText().toString() == "")
                {
                    mButtons[2].setText(Integer.toString(mPlanetNumber));
                    mPlanetNumber++;
                }
            }
        });

        mButtons[3].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPlanetNumber < 9 && mButtons[3].getText().toString() == "")
                {
                    mButtons[3].setText(Integer.toString(mPlanetNumber));
                    mPlanetNumber++;
                }
            }
        });

        mButtons[4].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPlanetNumber < 9 && mButtons[4].getText().toString() == "")
                {
                    mButtons[4].setText(Integer.toString(mPlanetNumber));
                    mPlanetNumber++;
                }
            }
        });

        mButtons[5].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPlanetNumber < 9 && mButtons[5].getText().toString() == "")
                {
                    mButtons[5].setText(Integer.toString(mPlanetNumber));
                    mPlanetNumber++;
                }
            }
        });

        mButtons[6].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPlanetNumber < 9 && mButtons[6].getText().toString() == "")
                {
                    mButtons[6].setText(Integer.toString(mPlanetNumber));
                    mPlanetNumber++;
                }
            }
        });

        mButtons[7].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPlanetNumber < 9 && mButtons[7].getText().toString() == "")
                {
                    mButtons[7].setText(Integer.toString(mPlanetNumber));
                    mPlanetNumber++;
                }
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPlanetNumber = 1;
                mButtons[0].setText("");
                mButtons[1].setText("");
                mButtons[2].setText("");
                mButtons[3].setText("");
                mButtons[4].setText("");
                mButtons[5].setText("");
                mButtons[6].setText("");
                mButtons[7].setText("");
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println(v.getBackground());
                int score = getGrade();
                System.out.println("GRADE = " + score + "/8");
                mScores = mScores + 1;
                updateStudent();
            }
        });

    }

    private int getGrade()
    {
        int score = 0;

        for (int i = 0; i < 8; i++)
        {
            if (mHm.get(mPlanetList.get(i)) == mButtons[i].getText())
            {
                score++;
            }
        }

        return score;
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
                loading = ProgressDialog.show(PlanetActivity.this, "Loading...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(PlanetActivity.this, s, Toast.LENGTH_LONG).show();
                Intent submitIntent = new Intent(PlanetActivity.this, GamePickerActivity.class);
                submitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mComplete[0] = true;
                submitIntent.putExtra("GamesComplete", mComplete);
                submitIntent.putExtra("gamePickerID", mStudentID);
                submitIntent.putExtra("gamePickerScore", mScores);
                startActivity(submitIntent);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID, Integer.toString(mStudentID));
                hashMap.put(Config.KEY_EMP_DESG, mLocation);
                hashMap.put(Config.KEY_EMP_SAL, Integer.toString(mScores));

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP, hashMap);

                return s;
            }
        }

        UpdateStudent ue = new UpdateStudent();
        ue.execute();
    }

}


