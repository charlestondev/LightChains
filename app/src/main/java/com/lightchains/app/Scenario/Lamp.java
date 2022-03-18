package com.lightchains.app.Scenario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;


/**
 * Created by charleston on 28/09/14.
 */
public abstract class Lamp extends Sprite{
    static final int LAMP_ON = 1;
    static final int LAMP_BURST = 2;
    static final int LAMP_BURNED = 3;
    public int power;
    public Lamp(){}
    public Lamp(Context context, int x, int y, int width, int drawables[], BitmapContainer bc){
        this.width = width;
        this.x = x;
        this.y = y;
        if(bc.bitmapFrames[Sprite.STAND]==null){
            Bitmap lampOff = BitmapFactory.decodeResource(context.getResources(), drawables[0]);
            Bitmap lampOn = BitmapFactory.decodeResource(context.getResources(), drawables[1]);
            Bitmap lampBurst = BitmapFactory.decodeResource(context.getResources(), drawables[2]);
            Bitmap lampBurned = BitmapFactory.decodeResource(context.getResources(), drawables[3]);
            lampOff =  Bitmap.createScaledBitmap(lampOff, width ,width,false);
            lampOn = Bitmap.createScaledBitmap(lampOn, width ,width,false);
            lampBurst = Bitmap.createScaledBitmap(lampBurst, width ,width,false);
            lampBurned = Bitmap.createScaledBitmap(lampBurned, width ,width,false);
            bc.bitmapFrames[Sprite.STAND] = new Bitmap[]{lampOff};
            bc.bitmapFrames[LAMP_ON] = new Bitmap[]{lampOn};
            bc.bitmapFrames[LAMP_BURST] = new Bitmap[]{lampBurst,lampBurst,lampBurst,lampBurst,lampBurst};
            bc.bitmapFrames[LAMP_BURNED] = new Bitmap[]{lampBurned};
        }
        super.bc = bc;//used on method draw
        super.paint = new Paint();
        paint.setAlpha(0);
    }
    public abstract int getPoint();
    public abstract int getBonus();
    public abstract double getConsume();

}
