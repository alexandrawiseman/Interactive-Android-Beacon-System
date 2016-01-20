package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AdultModeActivity extends Activity
{
    private Button mDoneButton;
    private ImageButton mImageButton;
    private ImageView mAdultImage;
    private TextView mFact;
    private int mFactCount = 0;
    private String beacon;
    private int[] mStatueStrings = {R.string.adult_statue_1, R.string.adult_statue_2, R.string.adult_statue_3};
    private int[] mPlanetStrings = {R.string.adult_planets_1, R.string.adult_planets_2, R.string.adult_planets_3, R.string.adult_planets_4, R.string.adult_planets_5};
    private int[] mPaintingStrings = {R.string.adult_painting_1, R.string.adult_painting_2, R.string.adult_painting_3};
    private boolean[] complete = new boolean[3];


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_mode);

        if(getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            complete = getIntent().getBooleanArrayExtra("GamesComplete");
        }
        if(getIntent().getStringExtra("beacon") != null)
        {
            beacon = getIntent().getStringExtra("beacon");
        }

        //beacon = "A";

        mAdultImage = (ImageView) findViewById(R.id.adult_image);
        if(beacon.equals("C"))
        {
            mAdultImage.setImageResource(R.drawable.monalisa);
        }
        else if(beacon.equals("B"))
        {
            mAdultImage.setImageResource(R.drawable.statueofliberty_blackandwhite);
        }
        else if(beacon.equals("A"))
        {
            mAdultImage.setImageResource(R.drawable.planets);
        }

        mDoneButton = (Button) findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent doneIntent = new Intent(AdultModeActivity.this, FactPickerActivity.class);
                doneIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                switch (beacon) {
                    case "A": {
                        complete[0] = true;
                        break;
                    }
                    case "B": {
                        complete[1] = true;
                        break;
                    }
                    case "C": {
                        complete[2] = true;
                        break;
                    }
                }
                doneIntent.putExtra("GamesComplete", complete);
                startActivity(doneIntent);
            }
        });

        mFact = (TextView) findViewById(R.id.fact_text);
        if(beacon.equals("C"))
        {
            mFact.setText(getResources().getString(R.string.adult_painting_1));
        }
        else if(beacon.equals("B"))
        {
            mFact.setText(getResources().getString(R.string.adult_statue_1));
        }
        else if(beacon.equals("A"))
        {
            mFact.setText(getResources().getString(R.string.adult_planets_1));
        }

        mImageButton = (ImageButton) findViewById(R.id.next_fact_button);
        mImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mFactCount++;

                if(beacon.equals("C"))
                {
                    if (mFactCount >= mPaintingStrings.length)
                    {
                        mFactCount = 0;
                    }
                    mFact.setText(mPaintingStrings[mFactCount]);
                }
                else if(beacon.equals("B"))
                {
                    if (mFactCount >= mStatueStrings.length)
                    {
                        mFactCount = 0;
                    }
                    mFact.setText(mStatueStrings[mFactCount]);
                }
                else if(beacon.equals("A"))
                {
                    if (mFactCount >= mPlanetStrings.length)
                    {
                        mFactCount = 0;
                    }
                    mFact.setText(mPlanetStrings[mFactCount]);
                }
            }
        });
    }

}
