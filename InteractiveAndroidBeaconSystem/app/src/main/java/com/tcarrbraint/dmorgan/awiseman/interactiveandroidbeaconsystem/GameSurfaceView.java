package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

import android.app.Activity;
import android.content.Context;
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
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;


public class GameSurfaceView extends SurfaceView implements Runnable, SensorEventListener
{
    private static final String TAG = "ME";
    private static final int MAX_FPS = 40; //desired fps
    private static final int FRAME_PERIOD = 1000 / MAX_FPS; // the frame period
    private static final int WIDTH = 150;
    private static final int HEIGHT = 150;

    private TextView mScoretext;
    private boolean mIsRunning = false;
    private Thread mGameThread;
    private SurfaceHolder mHolder;
    private int mScreenWidth;
    private int mScreenHeight;
    private Sprite[] mSprites;
    private Painting[] mPaintings;
    private Sensor mAccelerometer;
    private SoundPool mSounds;
    private int mSCatch;
    private Context mContext;

    public SensorManager sensorManager = null;
    public static int x1;
    public static int y1;
    public static int z1;
    public int score = 0;

    ShapeDrawable mDrawable = new ShapeDrawable();

    public GameSurfaceView(Context context)
    {
        super(context);
        mContext = context;
        mScoretext = (TextView) findViewById(R.id.score);

        Log.d(TAG, "GameSurfaceView Open");

        sensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        mHolder = getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
                mScreenWidth = width;
                mScreenHeight = height;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {

            }
        });

        mSounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSCatch = mSounds.load(context, R.raw.catchpainting, 1);

        mSprites = new Sprite[]{
                new Sprite(100, 100, BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher)),
        };

        mPaintings = new Painting[]{
                new Painting(300, 300, paintingpicker()),
                new Painting(400, 600, paintingpicker()),
                new Painting(100, 600, paintingpicker()),
        };
    }

    Bitmap paintingpicker()
    {
        Random rand = new Random();
        int rNum = (rand.nextInt(8));
        switch (rNum)
        {
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
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            x1 = (int) sensorEvent.values[1];
            y1 = (int) sensorEvent.values[2]; // tilt forward = negative
            z1 = (int) sensorEvent.values[0]; // tilt right = negative

        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {

        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public void resume()
    {
        mIsRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void pause()
    {
        mIsRunning = false;
        boolean retry = true;
        while (retry)
        {
            try
            {
                mGameThread.join();
                retry = false;
            } catch (InterruptedException e)
            {
            }
        }
    }


    class Painting extends Sprite
    {

        public Painting(int x, int y, Bitmap image)
        {
            super(x, y, image);
        }
    }

    class Sprite
    {
        int x;
        int y;
        int directionX = 1;
        int directionY = 1;
        int speed = 10;
        int color = 0;
        Bitmap image;

        public Sprite(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public Sprite(int x, int y, Bitmap image)
        {
            this(x, y);
            this.image = image;
        }

        public Sprite(int x, int y, Bitmap image, int color)
        {
            this(x, y, image);
            this.color = color;
        }

    }

    static Painting[] addElement(Painting[] a, Painting e)
    {
        a = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }


    int counter = 1000;

    protected void step()
    {

        //manage time and add mPaintings
        counter--;
        if ((counter == 700) || (counter == 400))
        {
            mPaintings = addElement(mPaintings, new Painting(300, 300, paintingpicker()));
        }
        if (counter <= 0)
        {
            //END GAME
            synchronized (mHolder)
            {
                //quit to mainmenu
                ((Activity) mContext).finish();
            }
        }

        //Move the Paintings
        for (int index = 0, length = mPaintings.length; index < length; index++)
        {
            Painting painting = mPaintings[index];
            if ((painting.x < 0) || ((painting.x + painting.image.getWidth()) > mScreenWidth))
            {
                painting.directionX *= -1;
            }
            if ((painting.y < 0) || ((painting.y + painting.image.getHeight()) > mScreenHeight))
            {
                painting.directionY *= -1;
            }

            Rect current = new Rect(painting.x, painting.y,
                    painting.x + painting.image.getWidth(),
                    painting.y + painting.image.getHeight());
            for (int subindex = 0; subindex < length; subindex++)
            {
                if (subindex != index)
                {
                    Painting subpainting = mPaintings[subindex];
                    Rect other = new Rect(subpainting.x, subpainting.y,
                            subpainting.x + subpainting.image.getWidth(),
                            subpainting.y + subpainting.image.getHeight());
                    if (Rect.intersects(current, other))
                    {
                        // Poor physics implementation.
                        painting.directionX *= -1;
                        painting.directionY *= -1;
                    }
                }
            }

            int movex = z1 * -3;
            int movey = y1 * -3;
            painting.x += painting.speed * painting.directionX;
            painting.y += painting.speed * painting.directionY;
        }

        //Move the Character
        for (int index = 0; index <= mSprites.length - 1; index++)
        {
            Sprite sprite = mSprites[index];
            if ((sprite.x < 0))
            {
                sprite.x += 30;
            }
            if ((sprite.x + sprite.image.getWidth()) > mScreenWidth)
            {
                sprite.x -= 30;
            }
            if ((sprite.y < 0))
            {
                sprite.y += 30;
            }
            if ((sprite.y + sprite.image.getHeight()) > mScreenHeight)
            {
                sprite.y -= 30;
            }
            Rect current = new Rect(sprite.x, sprite.y,
                    sprite.x + sprite.image.getWidth(),
                    sprite.y + sprite.image.getHeight());
            for (int subindex = 0, length2 = mPaintings.length; subindex < length2; subindex++)
            {
                Sprite subsprite = mPaintings[subindex];
                Rect other = new Rect(subsprite.x, subsprite.y,
                        subsprite.x + subsprite.image.getWidth(),
                        subsprite.y + subsprite.image.getHeight());

                //Collision player to painting
                if (Rect.intersects(current, other))
                {

                    //score
                    Log.d(TAG, "Collision!");
                    score += 100;
                    Log.d(TAG, String.valueOf(score));

                    //play sound
                    mSounds.play(mSCatch, 1.0f, 1.0f, 0, 0, 1.5f);

                    //move painting to new location
                    Random randme = new Random();
                    mPaintings[subindex].image = paintingpicker();
                    int newx = (int) randme.nextInt(mScreenWidth - subsprite.image.getWidth() - 100);
                    int newy = (int) randme.nextInt(mScreenHeight - subsprite.image.getHeight() - 10);
                    mPaintings[subindex].x = newx;
                    mPaintings[subindex].y = newy;
                    subsprite = mPaintings[subindex];
                    Rect newrect = new Rect(subsprite.x, subsprite.y,
                            subsprite.x + subsprite.image.getWidth(),
                            subsprite.y + subsprite.image.getHeight());

                    //Test painting location to see if it overlaps.
                    int testflag = 0; //tell loop to end
                    int testindex = 0; //increments for each painting tested. end loop if testloc > # of mPaintings
                    do
                    {
                        if (testindex == subindex)
                        {
                            testindex++;
                        }
                        if (testindex >= mPaintings.length)
                        {
                            testindex = 0;
                            testflag = 1;
                        }

                        //current testing painting
                        subsprite = mPaintings[testindex];
                        Rect test = new Rect(subsprite.x, subsprite.y,
                                subsprite.x + subsprite.image.getWidth(),
                                subsprite.y + subsprite.image.getHeight());

                        if (Rect.intersects(newrect, test))
                        {
                            testindex = 0;
                            //move painting to new location
                            randme = new Random();
                            newx = (int) randme.nextInt(mScreenWidth - subsprite.image.getWidth() - 100);
                            newy = (int) randme.nextInt(mScreenHeight - subsprite.image.getHeight() - 10);
                            mPaintings[subindex].x = newx;
                            mPaintings[subindex].y = newy;
                            subsprite = mPaintings[subindex];
                            newrect = new Rect(subsprite.x, subsprite.y,
                                    subsprite.x + subsprite.image.getWidth(),
                                    subsprite.y + subsprite.image.getHeight());
                        } else
                        {
                            testindex++;
                        }
                    } while (testflag != 1);

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
        }

    }

    protected void render(Canvas canvas)
    {
        canvas.drawColor(Color.WHITE);
        for (int index = 0, length = mSprites.length; index < length; index++)
        {
            Paint p = null;
            if (mSprites[index].color != 0)
            {
                p = new Paint();
                ColorFilter filter = new LightingColorFilter(mSprites[index].color, 0);
                p.setColorFilter(filter);
            }

            canvas.drawBitmap(mSprites[index].image, mSprites[index].x, mSprites[index].y, p);
        }
        for (int index = 0, length = mPaintings.length; index < length; index++)
        {
            Paint p = null;
            if (mPaintings[index].color != 0)
            {
                p = new Paint();
                ColorFilter filter = new LightingColorFilter(mPaintings[index].color, 0);
                p.setColorFilter(filter);
            }

            canvas.drawBitmap(mPaintings[index].image, mPaintings[index].x, mPaintings[index].y, p);
        }

        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(255, 0, 0, 0);
        textPaint.setFakeBoldText(true);
        textPaint.setTextSize(50);

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double counterdisplay = Double.parseDouble(twoDForm.format(counter / 100.0));
        canvas.drawText("Time: " + counterdisplay, 20, 100, textPaint);
        canvas.drawText("Score: " + score, 400, 100, textPaint);
    }

    @Override
    public void run()
    {
        while (mIsRunning)
        {
            // We need to make sure that the surface is ready
            if (!mHolder.getSurface().isValid())
            {
                continue;
            }
            long started = System.currentTimeMillis();

            // update
            step();
            // draw
            Canvas canvas = mHolder.lockCanvas();
            if (canvas != null)
            {
                render(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }

            float deltaTime = (System.currentTimeMillis() - started);
            int sleepTime = (int) (FRAME_PERIOD - deltaTime);
            if (sleepTime > 0)
            {
                try
                {
                    mGameThread.sleep(sleepTime);
                } catch (InterruptedException e)
                {
                }
            }
            while (sleepTime < 0)
            {
                step();
                sleepTime += FRAME_PERIOD;
            }
        }
    }
}
