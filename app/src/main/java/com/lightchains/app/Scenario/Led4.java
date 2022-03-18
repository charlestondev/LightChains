package com.lightchains.app.Scenario;

import android.content.Context;

import com.lightchains.app.R;

/**
 * Created by charleston on 17/09/14.
 */
public class Led4 extends Lamp {
    static BitmapContainer bc = new BitmapContainer(4);//constructor creates the array;
    static int bonus = 10;
    static int point = 5;
    static double consume = 0.5;
    public Led4(Context context, int x, int y, int width){
        super(
                context,
                x,
                y,
                width,
                new int[]{
                        R.drawable.led4_off,
                        R.drawable.led4_on,
                        R.drawable.led4_burst,
                        R.drawable.led1_burned
                },
                bc
        );
        point = 5;
        bonus = 10;
        consume = 0.5;
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
