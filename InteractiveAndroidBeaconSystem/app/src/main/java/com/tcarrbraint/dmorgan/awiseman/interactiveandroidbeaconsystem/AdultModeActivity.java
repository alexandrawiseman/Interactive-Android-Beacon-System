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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_mode);

        beacon = "statue";

        mAdultImage = (ImageView) findViewById(R.id.adult_image);
        if(beacon == "statue")
        {
            mAdultImage.setImageResource(R.drawable.statueofliberty_blackandwhite);
        }
        else if(beacon == "planets")
        {
            mAdultImage.setImageResource(R.drawable.planets);
        }

        mDoneButton = (Button) findViewById(R.id.done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent doneIntent = new Intent(AdultModeActivity.this, MainMenu.class);
                startActivity(doneIntent);
            }
        });

        mFact = (TextView) findViewById(R.id.fact_text);
        if(beacon == "statue")
        {
            mFact.setText(getResources().getString(R.string.adult_statue_1));
        }
        else if(beacon == "planets")
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

                if(beacon == "statue")
                {
                    if (mFactCount >= mStatueStrings.length)
                    {
                        mFactCount = 0;
                    }
                    mFact.setText(mStatueStrings[mFactCount]);
                }
                else if(beacon == "planets")
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
