package com.lightchains.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lightchains.app.Scenario.Scenario;
import com.lightchains.app.Scenario.Sprite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by charleston on 12/08/14.
 */
public class StartGame extends Activity{
    int stageNumber;
    boolean story;
    Panel panel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        panel = new Panel(this);
        setContentView(panel);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            stageNumber = b.getInt("stage_number");
            story = b.getBoolean("story", false);
            Log.i("story", story + "");
        }
        else{
            stageNumber = 1;
            story = false;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(!panel.scenario.pause)
            panel.scenario.pause(true);

    }

    @Override
    protected void onStop() {
        if(!panel.scenario.pause)
            panel.scenario.pause(true);
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        panel.scenario.pause(true);

    }

    class Panel extends SurfaceView implements SurfaceHolder.Callback {
        private TimeThread _thread;
        private int canvas_width;
        private int canvas_height;
        int bY = 0;
        //Bitmap background = BitmapFactory.decodeResource(getResources(),R.drawable.background_stage1);
        Scenario scenario;
        public Panel(Context context) {
            super(context);
            getHolder().addCallback(this);
            _thread = new TimeThread(getHolder(), this);
            setFocusable(true);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return scenario.onTouch(event);
        }

        @Override
        public void draw(Canvas canvas) {
            scenario.draw(canvas);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Canvas c  = holder.lockCanvas(null);
            canvas_width = c.getWidth();
            canvas_height = c.getHeight();
            //float scaleFactor = ((float)canvas_width/(float)background.getWidth());
            //background = Bitmap.createScaledBitmap(background, canvas_width ,(int)(background.getHeight()*scaleFactor),false);
            //backgroundDifference = background.getHeight()-canvas_height;
            scenario = new Scenario(getContext(),canvas_width, canvas_height,stageNumber, story);
            //Log.i("canvas size | background height", canvas_width+" - "+canvas_height+" | "+background.getHeight());
            getHolder().unlockCanvasAndPost(c);
            _thread.setRunning(true);
            _thread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }
    }
    class TimeThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;

        public TimeThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        public SurfaceHolder getSurfaceHolder() {
            return _surfaceHolder;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        if(c!=null)
                            _panel.draw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}
