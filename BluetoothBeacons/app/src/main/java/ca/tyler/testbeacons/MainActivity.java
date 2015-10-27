package ca.tyler.testbeacons;

import android.app.Activity;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {


    private TextView mTest1;
    private TextView mTest2;
    private TextView mTest3;
    private RegionBootstrap regionBootstrap;

        protected static final String TAG = "RangingActivity";
        private BeaconManager beaconManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            beaconManager = BeaconManager.getInstanceForApplication(this);
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=a7ae2eb7,i:4-19,i:20-21,i:22-23,p:24-24"));
            beaconManager.bind(this);
        }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            beaconManager.unbind(this);
        }
        @Override
        public void onBeaconServiceConnect() {
            Log.i(TAG, "Something?");
            beaconManager.setRangeNotifier(new RangeNotifier() {
                @Override
                public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                    if (beacons.size() > 0) {
                        Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    }
                    Log.i(TAG, "Nothing.");
                }
            });

            try {
                beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            } catch (RemoteException e) {    }
        }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "onCreateOptionsMenu(Bundle) called");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

}


