package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);

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
                Intent mainMenuIntent = new Intent(PlanetActivity.this, MainMenu.class);
                mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainMenuIntent);
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

}


