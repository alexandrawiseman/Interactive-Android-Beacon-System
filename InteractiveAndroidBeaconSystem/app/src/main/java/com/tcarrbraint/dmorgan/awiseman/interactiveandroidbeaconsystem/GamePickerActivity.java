package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;

import java.text.DecimalFormat;
import java.util.Collection;


public class GamePickerActivity extends Activity implements BeaconConsumer
{

    private BeaconManager mBeaconManager = BeaconManager.getInstanceForApplication(this);
    private LinearLayout mLayoutbackground;
    private Button mPlaybutton;
    private String mSavelast = "E";
    private String mSaveIdentity = "D";
    private boolean[] mComplete = new boolean[3];
    private int mStudentID;
    private int mScore;
    private GoogleApiClient mClient;

    long startTime = 0;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable()
    {
        @Override
        public void run()
        {

            if (mComplete[0] && mComplete[1] && mComplete[2])
            {
                logToDisplay("Wow you beat the game at every exhibit, you're done! Time to return to your teacher.");
                mPlaybutton.setClickable(true);
                mPlaybutton.getBackground().setColorFilter(null);
                mLayoutbackground.setBackgroundColor(Color.MAGENTA);
                mPlaybutton.setVisibility(View.VISIBLE);
                mPlaybutton.setText("Back to Main Menu");
            } else if (mSavelast != mSaveIdentity)
            {
                switch (mSaveIdentity)
                {
                    case "A":
                    {
                        mLayoutbackground.setBackgroundColor(Color.BLUE);
                        mPlaybutton.setVisibility(View.VISIBLE);
                        mPlaybutton.setText(R.string.planetbuttontext);
                        if (mComplete[0])
                        {
                            logToDisplay(getString(R.string.gamepicker_galaxy_complete));
                            mPlaybutton.setClickable(false);
                            mPlaybutton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                        } else
                        {
                            logToDisplay(getString(R.string.gamepicker_galaxy));
                            mPlaybutton.setClickable(true);
                            mPlaybutton.getBackground().setColorFilter(null);
                        }
                        break;
                    }
                    case "B":
                    {
                        mLayoutbackground.setBackgroundColor(Color.RED);
                        mPlaybutton.setVisibility(View.VISIBLE);
                        mPlaybutton.setText(R.string.statuebuttontext);
                        if (mComplete[1])
                        {
                            logToDisplay(getString(R.string.gamepicker_statue_complete));
                            mPlaybutton.setClickable(false);
                            mPlaybutton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                        } else
                        {
                            logToDisplay(getString(R.string.gamepicker_statue));
                            mPlaybutton.setClickable(true);
                            mPlaybutton.getBackground().setColorFilter(null);
                        }
                        break;
                    }
                    case "C":
                    {
                        mLayoutbackground.setBackgroundResource(R.color.regionc_color);
                        mPlaybutton.setVisibility(View.VISIBLE);
                        mPlaybutton.setText(R.string.paintingbuttontext);
                        if (mComplete[2])
                        {
                            logToDisplay(getString(R.string.gamepicker_monalisa_complete));
                            mPlaybutton.setClickable(false);
                            mPlaybutton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                        } else
                        {
                            logToDisplay(getString(R.string.gamepicker_monalisa));
                            mPlaybutton.setClickable(true);
                            mPlaybutton.getBackground().setColorFilter(null);
                        }
                        break;
                    }
                    default:
                    {
                        logToDisplay(getString(R.string.gamepicker_default));
                        mLayoutbackground.setBackgroundResource(R.color.main_menu_background);
                        mPlaybutton.setVisibility(View.GONE);
                        break;
                    }

                }
                mSavelast = mSaveIdentity;
            }

            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_picker);

        mStudentID = getIntent().getIntExtra("gamePickerID", 0);
        mScore = getIntent().getIntExtra("gamePickerScore", 0);
        Log.d("GamePickerActivity", "Student ID: " + mStudentID);

        if (getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            mComplete = getIntent().getBooleanArrayExtra("GamesComplete");
        }

        if (mComplete[0])
        {
            ImageButton buttona = (ImageButton) GamePickerActivity.this.findViewById(R.id.aButton);
            buttona.setImageResource(R.drawable.check_icon);
        }
        if (mComplete[1])
        {
            ImageButton buttonb = (ImageButton) GamePickerActivity.this.findViewById(R.id.bButton);
            buttonb.setImageResource(R.drawable.check_icon);
        }
        if (mComplete[2])
        {
            ImageButton buttonc = (ImageButton) GamePickerActivity.this.findViewById(R.id.cButton);
            buttonc.setImageResource(R.drawable.check_icon);
        }

        mLayoutbackground = (LinearLayout) GamePickerActivity.this.findViewById(R.id.backlayout);
        mPlaybutton = (Button) GamePickerActivity.this.findViewById(R.id.playgame);

        Region region1 = new Region("bb1", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10172"), Identifier.parse("1"), Identifier.parse("1"));
        Region region2 = new Region("bb2", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10007"), Identifier.parse("1"), Identifier.parse("2"));
        Region region3 = new Region("bb3", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10101"), Identifier.parse("1"), Identifier.parse("2"));

        try
        {
            mBeaconManager.startMonitoringBeaconsInRegion(region1);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        try
        {
            mBeaconManager.startMonitoringBeaconsInRegion(region2);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
        try
        {
            mBeaconManager.startMonitoringBeaconsInRegion(region3);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager
                .getBeaconParsers()
                .add(new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        //Set Running Average Interval to 2sec
        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter.class);
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(2000);

        //set active scan time to 0.5sec //players=20001
        mBeaconManager.setForegroundScanPeriod(500l);
        mBeaconManager.setForegroundBetweenScanPeriod(0l);
        try
        {
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }

        mBeaconManager.bind(this);

        timerHandler.postDelayed(timerRunnable, 1000);
        Button playbutton = (Button) findViewById(R.id.playgame);
        playbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mComplete[0] && mComplete[1] && mComplete[2])
                {
                    Intent doneIntent = new Intent(GamePickerActivity.this, MainMenu.class);
                    startActivity(doneIntent);
                } else
                {
                    switch (mSaveIdentity)
                    {
                        case "A":
                        {
                            Intent planetIntent = new Intent(GamePickerActivity.this, PlanetActivity.class);
                            planetIntent.putExtra("GamesComplete", mComplete);
                            planetIntent.putExtra("gamePickerID", mStudentID);
                            planetIntent.putExtra("gamePickerScore", mScore);
                            startActivity(planetIntent);
                            break;
                        }
                        case "B":
                        {
                            Intent cameraIntent = new Intent(GamePickerActivity.this, CameraInstructionActivity.class);
                            cameraIntent.putExtra("GamesComplete", mComplete);
                            cameraIntent.putExtra("gamePickerID", mStudentID);
                            cameraIntent.putExtra("gamePickerScore", mScore);
                            startActivity(cameraIntent);
                            break;
                        }
                        case "C":
                        {
                            Intent paintingIntent = new Intent(GamePickerActivity.this, CatchPaintingActivity.class);
                            paintingIntent.putExtra("GamesComplete", mComplete);
                            paintingIntent.putExtra("gamePickerID", mStudentID);
                            paintingIntent.putExtra("gamePickerScore", mScore);
                            startActivity(paintingIntent);
                            break;
                        }

                    }
                }
            }
        });

        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        // Save the user's current game state
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mBeaconManager.unbind(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mBeaconManager.isBound(this)) mBeaconManager.setBackgroundMode(true);
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mBeaconManager.isBound(this)) mBeaconManager.setBackgroundMode(false);
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onBeaconServiceConnect()
    {
        mBeaconManager.setRangeNotifier(new RangeNotifier()
        {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region)
            {

                ///CLOSEST BEACON CODE
                double min = 1000;
                String save = "";
                DecimalFormat df = new DecimalFormat("#.###");
                for (Beacon beacon : beacons)
                {

                    if (beacon.getDistance() < min)
                    {
                        min = beacon.getDistance();
                        save = beacon.getId1().toString();
                    }
                }
                mSaveIdentity = identify(save);
            }

        });

        try
        {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e)
        {
        }
    }

    private void logToDisplay(final String line)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                TextView regiontext = (TextView) GamePickerActivity.this.findViewById(R.id.regiontext);
                regiontext.setText(line);
            }
        });
    }

    private String identify(String uid)
    {

        switch (uid)
        {
            case "a7ae2eb7-1f00-4168-b99b-a749bac10172":
                return "A";

            case "a7ae2eb7-1f00-4168-b99b-a749bac10007":
                return "C";

            case "a7ae2eb7-1f00-4168-b99b-a749bac10101":
                return "C";

            default:
                return "NotABC";

        }

    }

    @Override
    public void onStart()
    {
        super.onStart();

        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "GamePicker Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "GamePicker Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }
}