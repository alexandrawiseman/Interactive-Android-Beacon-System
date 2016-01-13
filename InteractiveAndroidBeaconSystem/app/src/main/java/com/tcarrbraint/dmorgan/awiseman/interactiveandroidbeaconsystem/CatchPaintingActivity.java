package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CatchPaintingActivity extends Activity {

    protected GameSurfaceView gameView;
    private boolean[] complete = new boolean[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            complete = getIntent().getBooleanArrayExtra("GamesComplete");
        }

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

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Intent submitIntent = new Intent(CatchPaintingActivity.this, GamePickerActivity.class);
        submitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        complete[2] = true;
        submitIntent.putExtra("GamesComplete", complete);
        startActivity(submitIntent);
    }


}
