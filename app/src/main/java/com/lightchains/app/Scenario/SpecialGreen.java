package com.lightchains.app.Scenario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;

import com.lightchains.app.R;

/**
 * Created by charleston on 17/09/14.
 */
public class SpecialGreen extends Lamp {
    static BitmapContainer bc = new BitmapContainer(4);//constructor creates the array;
    static int bonus = 150;
    static int point = 50;
    static double consume = 5;
    static int referenceBonus = Led3.bonus;
    static int referencePoint = Led3.point;
    static double referenceConsume = Led3.consume;
    public SpecialGreen(Context context, int x, int y, int width, int power){

        this.width = width;
        this.x = x;
        this.y = y;
        this.power = power;
        Bitmap lampOff1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.special_green_off);
        Bitmap lampOff2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.special_green_on1);
        Bitmap lampOff3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.special_green_on2);
        Bitmap lampOn = BitmapFactory.decodeResource(context.getResources(), R.drawable.special_green_on3);
        Bitmap lampBurst1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.special_green_on2);
        Bitmap lampBurst2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.special_green_on3);
        Bitmap lampBurst3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.special_green_burst);
        Bitmap lampBurned = BitmapFactory.decodeResource(context.getResources(), R.drawable.lid);
        lampOff1 =  Bitmap.createScaledBitmap(lampOff1, width ,width,false);
        lampOff2 =  Bitmap.createScaledBitmap(lampOff2, width ,width,false);
        lampOff3 =  Bitmap.createScaledBitmap(lampOff3, width ,width,false);
        lampOn = Bitmap.createScaledBitmap(lampOn, width ,width,false);
        lampBurst1 = Bitmap.createScaledBitmap(lampBurst1, width ,width,false);
        lampBurst2 = Bitmap.createScaledBitmap(lampBurst2, width ,width,false);
        lampBurst3 = Bitmap.createScaledBitmap(lampBurst3, width ,width,false);
        lampBurned = Bitmap.createScaledBitmap(lampBurned, width ,width,false);
        bc.bitmapFrames[Sprite.STAND] = new Bitmap[]{lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff1,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff3,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2,lampOff2};
        bc.bitmapFrames[LAMP_ON] = new Bitmap[]{lampOn};
        bc.bitmapFrames[LAMP_BURST] = new Bitmap[]{lampBurst1,lampBurst2,lampBurst3,lampBurst1,lampBurst2,lampBurst3,lampBurst1,lampBurst2,lampBurst3,lampBurst1,lampBurst2,lampBurst3,lampBurst1,lampBurst2,lampBurst3,lampBurst1,lampBurst2,lampBurst3,lampBurst1,lampBurst2,lampBurst3};
        bc.bitmapFrames[LAMP_BURNED] = new Bitmap[]{lampBurned};
        super.bc = bc;
        super.paint = new Paint();
        paint.setAlpha(0);
    }
    @Override
    public int getPoint() {
        return this.point;
    }

    @Override
    public int getBonus() {
        return this.bonus;
    }

    @Override
    public double getConsume() {
        return this.consume;
    }
}
