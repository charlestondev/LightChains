package com.lightchains.app.Scenario;

import android.content.Context;

import com.lightchains.app.R;

/**
 * Created by charleston on 17/09/14.
 */
public class Led3 extends Lamp {
    static BitmapContainer bc = new BitmapContainer(4);//constructor creates the array;
    static int bonus = 10;
    static int point = 5;
    static double consume = 0.5;
    public Led3(Context context, int x, int y, int width){
        super(
                context,
                x,
                y,
                width,
                new int[]{
                        R.drawable.led3_off,
                        R.drawable.led3_on,
                        R.drawable.led3_burst,
                        R.drawable.led1_burned
                },
                bc
        );
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
