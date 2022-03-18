package com.lightchains.app.Scenario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lightchains.app.R;

/**
 * Created by charleston on 17/09/14.
 */
public class Lamp2 extends Lamp{
    static BitmapContainer bc = new BitmapContainer(4);//constructor creates the array;
    static int bonus = 3;
    static int point = 5;
    static double consume = 1.5;
    public Lamp2(Context context, int x, int y, int width){
        super(
                context,
                x,
                y,
                width,
                new int[]{
                        R.drawable.lamp2_off,
                        R.drawable.lamp2_on,
                        R.drawable.lamp2_burst,
                        R.drawable.lamp2_burned
                },
                bc
        );
        point = 5;
        bonus = 3;
        consume = 1.5;
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
