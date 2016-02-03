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
    private int planetNumber;
    private List<Integer> planetList;
    private HashMap hm;
    private boolean[] complete = new boolean[3];
    private int studentID;
    private int scores;
    private String location = "Planets";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);

        if(getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            complete = getIntent().getBooleanArrayExtra("GamesComplete");
        }

        studentID = getIntent().getIntExtra("gamePickerID", 0);
        scores = getIntent().getIntExtra("gamePickerScore", 0);

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

        planetNumber = 1;

        hm = new HashMap();
        hm.put(R.drawable.mercury, "1");
        hm.put(R.drawable.venus, "2");
        hm.put(R.drawable.earth, "3");
        hm.put(R.drawable.mars, "4");
        hm.put(R.drawable.jupiter, "5");
        hm.put(R.drawable.saturn, "6");
        hm.put(R.drawable.uranus, "7");
        hm.put(R.drawable.neptune, "8");

        // Initialize order of planets
        planetList = Arrays.asList(R.drawable.mercury, R.drawable.venus, R.drawable.earth, R.drawable.mars, R.drawable.jupiter, R.drawable.saturn, R.drawable.uranus, R.drawable.neptune);
        Collections.shuffle(planetList);

        mButtons[0].setBackground(getResources().getDrawable(planetList.get(0)));
        mButtons[1].setBackground(getResources().getDrawable(planetList.get(1)));
        mButtons[2].setBackground(getResources().getDrawable(planetList.get(2)));
        mButtons[3].setBackground(getResources().getDrawable(planetList.get(3)));
        mButtons[4].setBackground(getResources().getDrawable(planetList.get(4)));
        mButtons[5].setBackground(getResources().getDrawable(planetList.get(5)));
        mButtons[6].setBackground(getResources().getDrawable(planetList.get(6)));
        mButtons[7].setBackground(getResources().getDrawable(planetList.get(7)));

        mButtons[0].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (planetNumber < 9)
                {
                    mButtons[0].setText(Integer.toString(planetNumber));
                    planetNumber++;
                }
            }
        });

        mButtons[1].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (planetNumber < 9)
                {
                    mButtons[1].setText(Integer.toString(planetNumber));
                    planetNumber++;
                }
            }
        });

        mButtons[2].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (planetNumber < 9)
                {
                    mButtons[2].setText(Integer.toString(planetNumber));
                    planetNumber++;
                }
            }
        });

        mButtons[3].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (planetNumber < 9)
                {
                    mButtons[3].setText(Integer.toString(planetNumber));
                    planetNumber++;
                }
            }
        });

        mButtons[4].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (planetNumber < 9)
                {
                    mButtons[4].setText(Integer.toString(planetNumber));
                    planetNumber++;
                }
            }
        });

        mButtons[5].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (planetNumber < 9)
                {
                    mButtons[5].setText(Integer.toString(planetNumber));
                    planetNumber++;
                }
            }
        });

        mButtons[6].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (planetNumber < 9)
                {
                    mButtons[6].setText(Integer.toString(planetNumber));
                    planetNumber++;
                }
            }
        });

        mButtons[7].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (planetNumber < 9)
                {
                    mButtons[7].setText(Integer.toString(planetNumber));
                    planetNumber++;
                }
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                planetNumber = 1;
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
                //Intent mainMenuIntent = new Intent(PlanetActivity.this, MainMenu.class);
                //mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(mainMenuIntent);
                Intent submitIntent = new Intent(PlanetActivity.this, GamePickerActivity.class);
                submitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                complete[0] = true;
                scores = scores + 1;
                updateStudent();
                submitIntent.putExtra("GamesComplete", complete);
                submitIntent.putExtra("gamePickerID", studentID);
                submitIntent.putExtra("gamePickerScore", score);
                startActivity(submitIntent);
            }
        });

    }

    private int getGrade()
    {
        int score = 0;

        for (int i = 0; i < 8; i++)
        {
            if (hm.get(planetList.get(i)) == mButtons[i].getText())
            {
                score++;
            }
        }

        return score;
    }

    private void updateStudent(){

        class UpdateStudent extends AsyncTask<Void,Void,String>
        {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PlanetActivity.this,"Loading...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(PlanetActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_EMP_ID,Integer.toString(studentID));
                hashMap.put(Config.KEY_EMP_DESG,location);
                hashMap.put(Config.KEY_EMP_SAL,Integer.toString(scores));

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        UpdateStudent ue = new UpdateStudent();
        ue.execute();
    }

}


