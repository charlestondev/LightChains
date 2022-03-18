package com.lightchains.app.Scenario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by charleston on 17/09/14.
 */
public class Sprite{
    public int x, y,width,height;
    public BitmapContainer bc;
    final static int STAND = 0;
    int countFrame;
    int totalFrames;
    int state;
    Paint paint;
    public Sprite(){
        paint = new Paint();
    }
    public Sprite(BitmapContainer bc){
        this.bc = bc;
        x = 0;
        y = 0;
        width = bc.bitmapFrames[STAND][0].getWidth();
        height = bc.bitmapFrames[STAND][0].getHeight();
        setState(STAND);
        paint = new Paint();

    }

    public Bitmap getBimap(){
        Bitmap frame = bc.bitmapFrames[state][countFrame];
        return frame;
    }
    public void draw(Canvas c){
        if(countFrame<bc.bitmapFrames[state].length-1)
            countFrame++;
        if(countFrame==bc.bitmapFrames[state].length-1 && state == STAND)
           countFrame=0;
        if(countFrame==bc.bitmapFrames[state].length-1 && state == Lamp.LAMP_BURST)
            setState(Lamp.LAMP_BURNED);
        Bitmap frame = getBimap();
        c.drawBitmap(frame, x,y,paint);
    }
    public void setState(int state){
        this.state = state;
        countFrame = 0;
        totalFrames = bc.bitmapFrames[state].length-1;
    }
}
