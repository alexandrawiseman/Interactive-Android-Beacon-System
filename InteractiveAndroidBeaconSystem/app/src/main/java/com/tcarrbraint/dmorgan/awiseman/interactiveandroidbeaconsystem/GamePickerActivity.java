package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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


public class GamePickerActivity extends Activity implements BeaconConsumer {

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private LinearLayout layoutbackground;
    private String savelast = "E";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_picker);
        layoutbackground = (LinearLayout) findViewById(R.id.gamepickerlayout);

        Region region1 = new Region("bb1", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10163"), Identifier.parse("1"), Identifier.parse("1"));
        Region region2 = new Region("bb2", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10001"), Identifier.parse("1"), Identifier.parse("2"));
        Region region3 = new Region("bb3", Identifier.parse("A7AE2EB7-1F00-4168-B99B-A749BAC10003"), Identifier.parse("1"), Identifier.parse("2"));
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

        //Set Running Average Interval

        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter.class);
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(2000);


        beaconManager.setForegroundScanPeriod(200l);
        beaconManager.setForegroundBetweenScanPeriod(0l);
        try {
            beaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //beaconManager.setBackgroundScanPeriod(100);
        //beaconManager.setBackgroundBetweenScanPeriod(100);
        beaconManager.bind(this);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
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
                String saveIdentity = identify(save);
                //logToDisplay("Closest = " + saveIdentity + " at " + df.format(min) + " meters.");

                if (savelast != saveIdentity) {
                    switch (saveIdentity) {
                        case "A":
                            logToDisplay("You made it to the Galaxy Exhibit! In this game you need to order the planets in our solar system. Ready?");
                            break;
                        case "B":
                            logToDisplay("The World Wonders Exhibit. It's time to take a picture of yourself with the statue of liberty. Ready?");
                            break;
                        case "C":
                            logToDisplay("Wow look at the famous paintings! In this game catch as many paintings as you can in the time limit. Ready?");
                            //((LinearLayout)findViewById(R.id.gamepickerlayout)).setBackgroundResource(R.color.main_menu_background);
                            //layoutbackground.setBackgroundResource(R.color.main_menu_background);
                            //layoutbackground.setBackgroundColor(Color.GREEN);
                            //makeGameButton();
                            break;
                        default:
                            logToDisplay("We'll let you know when you're at an exhibit.");
                            //layoutbackground.setBackgroundColor(Color.rgb(162,85,225));
                            break;

                    }
                    savelast = saveIdentity;
                }
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
        } catch (RemoteException e) {   }
    }

    private void makeGameButton(){
        TableLayout table = (TableLayout) findViewById(R.id.tableforgamebutton);
        TableRow tablerow = new TableRow(this);
        table.addView(tablerow);
        Button button = new Button(this);
        tablerow.addView(button);
    }


    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView regiontext = (TextView) GamePickerActivity.this.findViewById(R.id.regiontext);
                regiontext.setText(line);
            }
        });
    }

    private String identify(String uid){

        switch (uid){
            case "a7ae2eb7-1f00-4168-b99b-a749bac10163":
                return "A";

            case "a7ae2eb7-1f00-4168-b99b-a749bac10001":
                return "B";

            case "a7ae2eb7-1f00-4168-b99b-a749bac10002":
                return "C";

            default:
                return "NotABC";


        }

    }

}
