package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;


public class GamePickerActivity extends Activity implements BeaconConsumer {

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private LinearLayout layoutbackground;
    private Button playbutton;
    private String savelast = "E";
    private String saveIdentity = "D";
    private boolean[] complete = new boolean[3];
    private int studentID;
    private int score;

    long startTime = 0;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            if (complete[0] && complete[1] && complete[2]){
                logToDisplay("Wow you beat the game at every exhibit, you're done! Time to return to your teacher.");
                playbutton.setClickable(true);
                playbutton.getBackground().setColorFilter(null);
                layoutbackground.setBackgroundColor(Color.MAGENTA);
                playbutton.setVisibility(View.VISIBLE);
                playbutton.setText("Back to Main Menu");
            } else if (savelast != saveIdentity) {
                switch (saveIdentity) {
                    case "A": {
                        layoutbackground.setBackgroundColor(Color.BLUE);
                        playbutton.setVisibility(View.VISIBLE);
                        playbutton.setText(R.string.planetbuttontext);
                        if (complete[0]){
                            logToDisplay(getString(R.string.gamepicker_galaxy_complete));
                            playbutton.setClickable(false);
                            playbutton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                        } else {
                            logToDisplay(getString(R.string.gamepicker_galaxy));
                            playbutton.setClickable(true);
                            playbutton.getBackground().setColorFilter(null);
                        }
                        break;
                    }
                    case "B": {
                        layoutbackground.setBackgroundColor(Color.RED);
                        playbutton.setVisibility(View.VISIBLE);
                        playbutton.setText(R.string.statuebuttontext);
                        if (complete[1]){
                            logToDisplay(getString(R.string.gamepicker_statue_complete));
                            playbutton.setClickable(false);
                            playbutton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                        } else {
                            logToDisplay(getString(R.string.gamepicker_statue));
                            playbutton.setClickable(true);
                            playbutton.getBackground().setColorFilter(null);
                        }
                        break;
                    }
                    case "C": {
                        layoutbackground.setBackgroundResource(R.color.regionc_color);
                        playbutton.setVisibility(View.VISIBLE);
                        playbutton.setText(R.string.paintingbuttontext);
                        if (complete[2]){
                            logToDisplay(getString(R.string.gamepicker_monalisa_complete));
                            playbutton.setClickable(false);
                            playbutton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.DARKEN);
                        } else {
                            logToDisplay(getString(R.string.gamepicker_monalisa));
                            playbutton.setClickable(true);
                            playbutton.getBackground().setColorFilter(null);
                        }
                        break;
                    }
                    default: {
                        logToDisplay(getString(R.string.gamepicker_default));
                        layoutbackground.setBackgroundResource(R.color.main_menu_background);
                        playbutton.setVisibility(View.GONE);
                        break;
                    }

                }
                savelast = saveIdentity;
            }

            timerHandler.postDelayed(this, 1000);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_picker);

        studentID = getIntent().getIntExtra("gamePickerID", 0);
        score = getIntent().getIntExtra("gamePickerScore", 0);
        Log.d("GamePickerActivity", "Student ID: " + studentID);

        if(getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            complete = getIntent().getBooleanArrayExtra("GamesComplete");
        }

        if (complete[0]) {
            ImageButton buttona = (ImageButton) GamePickerActivity.this.findViewById(R.id.aButton);
            buttona.setImageResource(R.drawable.check_icon);
        }
        if (complete[1]) {
            ImageButton buttonb = (ImageButton) GamePickerActivity.this.findViewById(R.id.bButton);
            buttonb.setImageResource(R.drawable.check_icon);
        }
        if (complete[2]) {
            ImageButton buttonc = (ImageButton) GamePickerActivity.this.findViewById(R.id.cButton);
            buttonc.setImageResource(R.drawable.check_icon);
        }

        layoutbackground = (LinearLayout) GamePickerActivity.this.findViewById(R.id.backlayout);
        playbutton = (Button) GamePickerActivity.this.findViewById(R.id.playgame);

        Region region1 = new Region("bb1", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10172"), Identifier.parse("1"), Identifier.parse("1"));
        //Region region1 = new Region("bb1", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10007"), Identifier.parse("1"), Identifier.parse("1"));
        Region region2 = new Region("bb2", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10001"), Identifier.parse("1"), Identifier.parse("2"));
        //Region region2 = new Region("bb2", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10007"), Identifier.parse("1"), Identifier.parse("2"));
        Region region3 = new Region("bb3", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10101"), Identifier.parse("1"), Identifier.parse("2"));
        //Region region3 = new Region("bb3", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10007"), Identifier.parse("1"), Identifier.parse("2"));
        try {
            beaconManager.startMonitoringBeaconsInRegion(region1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            beaconManager.startMonitoringBeaconsInRegion(region2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            beaconManager.startMonitoringBeaconsInRegion(region3);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager
                .getBeaconParsers()
                .add(new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        //Set Running Average Interval to 2sec
        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter.class);
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(2000);

        //set active scan time to 0.5sec //players=20001
        beaconManager.setForegroundScanPeriod(500l);
        beaconManager.setForegroundBetweenScanPeriod(0l);
        try {
            beaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //beaconManager.setBackgroundScanPeriod(100);
        //beaconManager.setBackgroundBetweenScanPeriod(100);
        beaconManager.bind(this);

        timerHandler.postDelayed(timerRunnable, 1000);
        Button playbutton = (Button) findViewById(R.id.playgame);
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complete[0] && complete[1] && complete[2]) {
                    Intent doneIntent = new Intent(GamePickerActivity.this, MainMenu.class);
                    startActivity(doneIntent);
                } else {
                    switch (saveIdentity) {
                        case "A": {
                            Intent planetIntent = new Intent(GamePickerActivity.this, PlanetActivity.class);
                            planetIntent.putExtra("GamesComplete", complete);
                            planetIntent.putExtra("gamePickerID", studentID);
                            planetIntent.putExtra("gamePickerScore", score);
                            startActivity(planetIntent);
                            break;
                        }
                        case "B": {
                            Intent cameraIntent = new Intent(GamePickerActivity.this, CameraInstructionActivity.class);
                            cameraIntent.putExtra("GamesComplete", complete);
                            cameraIntent.putExtra("gamePickerID", studentID);
                            cameraIntent.putExtra("gamePickerScore", score);
                            startActivity(cameraIntent);
                            break;
                        }
                        case "C": {
                            Intent paintingIntent = new Intent(GamePickerActivity.this, CatchPaintingActivity.class);
                            paintingIntent.putExtra("GamesComplete", complete);
                            paintingIntent.putExtra("gamePickerID", studentID);
                            paintingIntent.putExtra("gamePickerScore", score);
                            startActivity(paintingIntent);
                            break;
                        }

                    }
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save the user's current game state
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                ///ORIGINAL SAMPLE CODE
                //for(int i = 0; i <= beacons.size(); i++){
               /*
               if (beacons.size() > 0) {
                   EditText editText = (EditText) RangingActivity.this.findViewById(R.id.rangingText);
                   Beacon firstBeacon = beacons.iterator().next();
                   logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
              }*/


               /*///MY DISTANCE CODE
               int i = 1;
               DecimalFormat df = new DecimalFormat("#.###");
               for (Beacon beacon : beacons) {
                   logToDisplay("beacon " + i + " about " + df.format(beacon.getDistance()) + " meters.");
                   i++;
               }*/

                ///CLOSEST BEACON CODE
                double min = 1000;
                String save = "";
                DecimalFormat df = new DecimalFormat("#.###");
                for (Beacon beacon : beacons) {

                    if (beacon.getDistance() < min) {
                        min = beacon.getDistance();
                        save = beacon.getId1().toString();
                    }
                }
                saveIdentity = identify(save);
                //logToDisplay("Closest = " + saveIdentity + " at " + df.format(min) + " meters.");



                /*///OUTPUT RSSI + TX Power
               int rssi,tx;
               String save = "";
               for (Beacon beacon : beacons) {
                   rssi = beacon.getRssi();
                   tx = beacon.getTxPower();
                   save = beacon.getId1().toString();
                   logToDisplay("Beacon = " + identify(save) + ", RSSI = " + rssi + ", TX = " + tx);
               }
               */
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }


    //Dynamically Create Buttons
    /*
    private void makeGameButton(){
        TableLayout table = (TableLayout) findViewById(R.id.tableforgamebutton);
        TableRow tablerow = new TableRow(this);
        table.addView(tablerow);
        Button button = new Button(this);
        tablerow.addView(button);

    }
*/

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView regiontext = (TextView) GamePickerActivity.this.findViewById(R.id.regiontext);
                regiontext.setText(line);
            }
        });
    }

    private String identify(String uid) {

        switch (uid) {
             case "a7ae2eb7-1f00-4168-b99b-a749bac10172":
            //case "a7ae2eb7-1f00-4168-b99b-a749bac10007":
                return "A";

            case "a7ae2eb7-1f00-4168-b99b-a749bac10001":
            //case "a7ae2eb7-1f00-4168-b99b-a749bac10007":
                return "B";

            case "a7ae2eb7-1f00-4168-b99b-a749bac10101":
            //case "a7ae2eb7-1f00-4168-b99b-a749bac10007":
                return "C";

            default:
                return "NotABC";

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GamePicker Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GamePicker Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
