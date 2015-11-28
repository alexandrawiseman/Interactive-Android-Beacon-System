package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class MainMenu extends Activity
{

    private ImageButton mAdultButton;
    private ImageButton mChildButton;
    private ImageButton mTeacherButton;
    private ImageButton mInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAdultButton = (ImageButton) findViewById(R.id.adultButton);
        mAdultButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mAdultButton.setBackgroundResource(R.drawable.clicked_main_menu_button);
                }

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mAdultButton.setBackgroundResource(R.drawable.main_menu_adult_button);
                }

                return false;
            }
        });

        mChildButton = (ImageButton) findViewById(R.id.childButton);
        mChildButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mChildButton.setBackgroundResource(R.drawable.clicked_main_menu_button);
                }

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mChildButton.setBackgroundResource(R.drawable.main_menu_child_button);
                }

                return false;
            }
        });

        mChildButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Intent planetIntent = new Intent(MainMenu.this, PlanetActivity.class);
                //startActivity(planetIntent);

                Intent cameraIntent = new Intent(MainMenu.this, CameraInstructionActivity.class);
                startActivity(cameraIntent);
            }
        });

        mTeacherButton = (ImageButton) findViewById(R.id.teacherButton);
        mTeacherButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mTeacherButton.setBackgroundResource(R.drawable.clicked_main_menu_button);
                }

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mTeacherButton.setBackgroundResource(R.drawable.main_menu_teacher_button);
                }

                return false;
            }
        });

        mInfoButton = (ImageButton) findViewById(R.id.infoButton);
        mInfoButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mInfoButton.setBackgroundResource(R.drawable.clicked_main_menu_button);
                }

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mInfoButton.setBackgroundResource(R.drawable.main_menu_info_button);
                }

                return false;
            }
        });

        mInfoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent infoIntent = new Intent(MainMenu.this, InfoActivity.class);
                startActivity(infoIntent);
            }
        });

    }
}
