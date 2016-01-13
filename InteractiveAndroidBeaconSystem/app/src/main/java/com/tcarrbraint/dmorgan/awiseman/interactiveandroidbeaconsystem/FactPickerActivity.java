package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
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


public class FactPickerActivity extends Activity implements BeaconConsumer {

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private LinearLayout layoutbackground;
    private Button playbutton;
    private String savelast = "E";
    private String saveIdentity = "D";
    private boolean[] complete = new boolean[3];


    long startTime = 0;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (savelast != saveIdentity) {
                switch (saveIdentity) {
                    case "A": {
                        logToDisplay("You've reached the Galaxy exhibit. Click the button to learn more about your solar system.");
                        layoutbackground.setBackgroundColor(Color.BLUE);
                        playbutton.setVisibility(View.VISIBLE);
                        playbutton.setText("Enter Planet Exhibit");
                        break;
                    }
                    case "B": {
                        logToDisplay("The World Wonders Exhibit. Click to learn about France's largest gift.");
                        layoutbackground.setBackgroundColor(Color.RED);
                        playbutton.setVisibility(View.VISIBLE);
                        playbutton.setText("Enter Liberty Exhibit");
                        break;
                    }
                    case "C": {
                        logToDisplay("The paintings exhibit. Click for info on the most famous artists including Leonardo, Van Gogh, and Picasso.");
                        layoutbackground.setBackgroundResource(R.color.regionc_color);
                        playbutton.setVisibility(View.VISIBLE);
                        playbutton.setText("Enter Famous Paintings Exhibit");
                        break;
                    }
                    default: {
                        logToDisplay("We'll let you know when you're at an exhibit.");
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
        if(getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            complete = getIntent().getBooleanArrayExtra("GamesComplete");
        }

        if (complete[0]) {
            ImageButton buttona = (ImageButton) FactPickerActivity.this.findViewById(R.id.aButton);
            buttona.setImageResource(R.drawable.a_icon);
        }
        if (complete[1]) {
            ImageButton buttonb = (ImageButton) FactPickerActivity.this.findViewById(R.id.bButton);
            buttonb.setImageResource(R.drawable.b_icon);
        }
        if (complete[2]) {
            ImageButton buttonc = (ImageButton) FactPickerActivity.this.findViewById(R.id.cButton);
            buttonc.setImageResource(R.drawable.c_icon);
        }

        layoutbackground = (LinearLayout) FactPickerActivity.this.findViewById(R.id.backlayout);
        playbutton = (Button) FactPickerActivity.this.findViewById(R.id.playgame);

        Region region1 = new Region("bb1", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC1CA64"), Identifier.parse("1"), Identifier.parse("1"));
        Region region2 = new Region("bb2", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10001"), Identifier.parse("1"), Identifier.parse("2"));
        Region region3 = new Region("bb3", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10002"), Identifier.parse("1"), Identifier.parse("2"));
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

        //set active scan time to 0.5sec
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
                switch (saveIdentity) {
                    case "A": {
                        Intent planetIntent = new Intent(FactPickerActivity.this, AdultModeActivity.class);
                        planetIntent.putExtra("GamesComplete", complete);
                        planetIntent.putExtra("beacon", saveIdentity);
                        startActivity(planetIntent);
                        break;
                    }
                    case "B": {
                        Intent cameraIntent = new Intent(FactPickerActivity.this, AdultModeActivity.class);
                        cameraIntent.putExtra("GamesComplete", complete);
                        cameraIntent.putExtra("beacon", saveIdentity);
                        startActivity(cameraIntent);
                        break;
                    }
                    case "C": {
                        Intent paintingIntent = new Intent(FactPickerActivity.this, AdultModeActivity.class);
                        paintingIntent.putExtra("GamesComplete", complete);
                        paintingIntent.putExtra("beacon", saveIdentity);
                        startActivity(paintingIntent);

                        break;
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
                TextView regiontext = (TextView) FactPickerActivity.this.findViewById(R.id.regiontext);
                regiontext.setText(line);
            }
        });
    }

    private String identify(String uid) {

        switch (uid) {
            case "a7ae2eb7-1f00-4168-b99b-a749bac1ca64":
                return "C";

            case "a7ae2eb7-1f00-4168-b99b-a749bac10001":
                return "C";

            case "a7ae2eb7-1f00-4168-b99b-a749bac10002":
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