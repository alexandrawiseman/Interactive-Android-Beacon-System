package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class CameraActivity extends Activity implements SurfaceHolder.Callback
{

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;
    private int mStudentID;
    private int mScore;
    private boolean[] mComplete = new boolean[3];

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (getIntent().getBooleanArrayExtra("GamesComplete") != null)
        {
            mComplete = getIntent().getBooleanArrayExtra("GamesComplete");
        }
        mStudentID = getIntent().getIntExtra("gamePickerID", 0);
        mScore = getIntent().getIntExtra("gamePickerScore", 0);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.control_liberty, null);
        LayoutParams layoutParamsControl
                = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);

        Button buttonTakePicture = (Button) findViewById(R.id.takepicture);
        buttonTakePicture.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                camera.takePicture(myShutterCallback,
                        myPictureCallback_RAW, myPictureCallback_JPG);
            }
        });
    }

    ShutterCallback myShutterCallback = new ShutterCallback()
    {

        @Override
        public void onShutter()
        {
        }
    };

    PictureCallback myPictureCallback_RAW = new PictureCallback()
    {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1)
        {
        }
    };

    PictureCallback myPictureCallback_JPG = new PictureCallback()
    {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1)
        {
            Bitmap myImage
                    = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
            Bitmap mutableBitmap = myImage.copy(Bitmap.Config.ARGB_8888, true);

            Matrix m = new Matrix();
            m.preScale(-1, 1);
            Bitmap flip = Bitmap.createBitmap(mutableBitmap, 0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight(), m, false);
            flip.setDensity(DisplayMetrics.DENSITY_DEFAULT);

            Paint paint = new Paint();
            Canvas canvas = new Canvas(flip);

            ImageView view = (ImageView) findViewById(R.id.statue);

            view.setDrawingCacheEnabled(true);
            Bitmap viewCapture = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            Rect src = new Rect(0, 0, viewCapture.getWidth(), viewCapture.getHeight());
            RectF dst = new RectF(0, 0, viewCapture.getWidth(), myImage.getHeight());
            canvas.drawBitmap(viewCapture, src, dst, paint);

            CommonResources.photo = flip;
            Intent cameraIntent = new Intent(CameraActivity.this, CameraSubmitActivity.class);
            cameraIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            cameraIntent.putExtra("GamesComplete", mComplete);
            cameraIntent.putExtra("gamePickerID", mStudentID);
            cameraIntent.putExtra("gamePickerScore", mScore);
            startActivity(cameraIntent);
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        if (previewing)
        {
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null)
        {
            try
            {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //Android Players issues changed from CAMERA_FACING_BACK
        camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

}