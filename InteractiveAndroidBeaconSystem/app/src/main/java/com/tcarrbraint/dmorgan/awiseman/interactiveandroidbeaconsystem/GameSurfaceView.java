package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;


public class GameSurfaceView extends SurfaceView implements Runnable,SensorEventListener {

    private static final String TAG = "ME";
    private TextView scoretext;

    private boolean isRunning = false;
    private Thread gameThread;
    private SurfaceHolder holder;
    public SensorManager sensorManager = null;
    private int screenWidth;
    private int screenHeight;


    private Sprite[] sprites;
    private Painting[] paintings;

    private final static int MAX_FPS = 40; //desired fps
    private final static int FRAME_PERIOD = 1000 / MAX_FPS; // the frame period

    ShapeDrawable mDrawable = new ShapeDrawable();
    private Sensor Accelerometer;
    public static int x1;
    public static int y1;
    public static int z1;
    public int score = 0;
    private static final int width = 150;
    private static final int height = 150;

    public GameSurfaceView(Context context) {
        super(context);
        scoretext = (TextView) findViewById(R.id.score);

        Log.d(TAG, "GameSurfaceView Open");

        sensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                screenWidth = width;
                screenHeight = height;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });



        sprites = new Sprite[] {
                new Sprite(100, 100, BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher)),
        };
        paintings = new Painting[] {
                new Painting(300, 300, paintingpicker()),
                new Painting(400, 600, paintingpicker()),
                new Painting(100, 600, paintingpicker()),
        };
    }

    Bitmap paintingpicker(){
        Random rand = new Random();
        int rNum = (rand.nextInt(8));
        switch  (rNum){
            case 0:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint1);
            case 1:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint2);
            case 2:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint3);
            case 3:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint4);
            case 4:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint5);
            case 5:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint6);
            case 6:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint7);
            case 7:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint8);
        }
        return BitmapFactory.decodeResource(this.getResources(), R.drawable.paint1);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // the values you were calculating originally here were over 10000!
            x1 = (int) sensorEvent.values[1];
            y1 = (int) sensorEvent.values[2]; // tilt forward = negative
            z1 = (int) sensorEvent.values[0]; // tilt right = negative

        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {

        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void resume() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void pause() {
        isRunning = false;
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
    }


    class Painting extends Sprite{

        public Painting(int x, int y, Bitmap image) {
            super(x, y, image);
        }
    }
    class Sprite {
        int x;
        int y;
        int directionX = 1;
        int directionY = 1;
        int speed = 10;
        int color = 0;
        Bitmap image;

        public Sprite(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Sprite(int x, int y, Bitmap image) {
            this(x, y);
            this.image = image;
        }

        public Sprite(int x, int y, Bitmap image, int color) {
            this(x, y, image);
            this.color = color;
        }

    }

    static Painting[] addElement(Painting[] a, Painting e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }


    int counter = 2000;
    protected void step() {

        //manage time and add paintings
        counter--;
        if ((counter == 1000) || (counter == 500)){
            paintings = addElement(paintings, new Painting(300, 300, paintingpicker()));
        }
        if (counter <= 0 ) {
            //END GAME?
            Intent intent = new Intent("kill");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            //holder.getSurface().release();
            //System.out.println("Paintings Caught Score = " + score + "!");
            //Intent mainMenuIntent = new Intent(GameSurfaceView, MainMenu.class);
            //mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //startActivity(mainMenuIntent);
        }

        //Move the Paintings
        for (int index = 0, length = paintings.length; index < length; index++) {
            Painting painting = paintings[index];
            if ((painting.x < 0) || ((painting.x + painting.image.getWidth()) > screenWidth)) {
                painting.directionX *= -1;
            }
            if ((painting.y < 0) || ((painting.y + painting.image.getHeight()) > screenHeight)) {
                painting.directionY *= -1;
            }

            Rect current = new Rect(painting.x, painting.y,
                    painting.x + painting.image.getWidth(),
                    painting.y + painting.image.getHeight());
            for (int subindex = 0; subindex < length; subindex++) {
                if (subindex != index) {
                    Painting subpainting = paintings[subindex];
                    Rect other = new Rect(subpainting.x, subpainting.y,
                            subpainting.x + subpainting.image.getWidth(),
                            subpainting.y + subpainting.image.getHeight());
                    if (Rect.intersects(current, other)) {
                        // Poor physics implementation.
                        painting.directionX *= -1;
                        painting.directionY *= -1;
                    }
                }
            }

            int movex = z1 * -3;
            int movey = y1 * -3;
            painting.x +=  painting.speed * painting.directionX;
            painting.y += painting.speed * painting.directionY;
        }

        //Move the Character
        for (int index = 0, length = sprites.length; index < length; index++) {
            Sprite sprite = sprites[index];
            if ((sprite.x < 0)) {
                //sprite.directionX *= -1;
                sprite.x += 30;
            }
            if ((sprite.x + sprite.image.getWidth()) > screenWidth) {
                sprite.x -= 30;
            }
            if ((sprite.y < 0)) {
                //sprite.directionY *= -1;
                sprite.y += 30;
            }
            if ((sprite.y + sprite.image.getHeight()) > screenHeight) {
                sprite.y -= 30;
            }
            Rect current = new Rect(sprite.x, sprite.y,
                    sprite.x + sprite.image.getWidth(),
                    sprite.y + sprite.image.getHeight());
            for (int subindex = 0, length2 = paintings.length; subindex < length2; subindex++) {
                Sprite subsprite = paintings[subindex];
                Rect other = new Rect(subsprite.x, subsprite.y,
                        subsprite.x + subsprite.image.getWidth(),
                        subsprite.y + subsprite.image.getHeight());
                if (Rect.intersects(current, other)) {
                    // Poor physics implementation.
                    Random randme = new Random();
                    paintings[subindex].image = paintingpicker();
                    int newx = (int) randme.nextInt(screenWidth - subsprite.image.getWidth() - 100);
                    int newy = (int) randme.nextInt(screenHeight - subsprite.image.getHeight() - 10);
                    paintings[subindex].x = newx;
                    paintings[subindex].y = newy;
                    Log.d(TAG, "Collision!");
                    score += 100;
                    Log.d(TAG, String.valueOf(score));
                }
            }

            int movex = z1 * -4;
            if (movex > 12)
                movex = 12;
            if (movex < -12)
                movex = -12;

            int movey = (y1 - 5) * -4;
            if (movey > 12)
                movey = 12;
            if (movey < -12)
                movey = -12;

            sprite.x += movex;
            sprite.y += movey;
            //sprite.x += (sprite.directionX * sprite.speed);
            //sprite.y += (sprite.directionY * sprite.speed);
            //x1 = (int) sensorEvent.values[1];
            //y1 = (int) sensorEvent.values[2]; // tilt forward = negative
            //z1 = (int) sensorEvent.values[0]; // tilt right = negative
        }

    }

    protected void render(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        for (int index = 0, length = sprites.length; index < length; index++) {
            Paint p = null;
            if (sprites[index].color != 0) {
                p = new Paint();
                ColorFilter filter = new LightingColorFilter(sprites[index].color, 0);
                p.setColorFilter(filter);
            }

            canvas.drawBitmap(sprites[index].image, sprites[index].x, sprites[index].y, p);
        }
        for (int index = 0, length = paintings.length; index < length; index++) {
            Paint p = null;
            if (paintings[index].color != 0) {
                p = new Paint();
                ColorFilter filter = new LightingColorFilter(paintings[index].color, 0);
                p.setColorFilter(filter);
            }

            canvas.drawBitmap(paintings[index].image, paintings[index].x, paintings[index].y, p);
        }

        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(255, 0, 0, 0);
        textPaint.setFakeBoldText(true);
        textPaint.setTextSize(30);

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double counterdisplay = Double.parseDouble(twoDForm.format(counter / 100.0));
        canvas.drawText("Time: " + counterdisplay,20,100,textPaint);
        canvas.drawText("Score: " + score,500,100,textPaint);

    }

    @Override
    public void run() {
        while(isRunning) {
            // We need to make sure that the surface is ready
            if (! holder.getSurface().isValid()) {
                continue;
            }
            long started = System.currentTimeMillis();

            // update
            step();
            // draw
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                render(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            float deltaTime = (System.currentTimeMillis() - started);
            int sleepTime = (int) (FRAME_PERIOD - deltaTime);
            if (sleepTime > 0) {
                try {
                    gameThread.sleep(sleepTime);
                }
                catch (InterruptedException e) {
                }
            }
            while (sleepTime < 0) {
                step();
                sleepTime += FRAME_PERIOD;
            }
        }
    }
}
