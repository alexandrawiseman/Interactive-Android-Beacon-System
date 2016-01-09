package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CatchPaintingActivity extends Activity {

    protected GameSurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gameView = new GameSurfaceView(this);
        setContentView(gameView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();

    }
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    private void killActivity() {
        finish();
    }

    public class MessageHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            killActivity();
        }
    }





}
