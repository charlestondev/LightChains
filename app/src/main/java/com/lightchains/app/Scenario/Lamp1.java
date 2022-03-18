package com.lightchains.app.Scenario;

import android.content.Context;


import com.lightchains.app.R;

/**
 * Created by charleston on 17/09/14.
 */
public class Lamp1 extends Lamp {
    static BitmapContainer bc = new BitmapContainer(4);//constructor creates the array;
    static int bonus = 2;
    static int point = 5;
    static double consume = 1;
    public Lamp1(Context context, int x, int y,int width){
        super(
                context,
                x,
                y,
                width,
                new int[]{
                        R.drawable.lamp1_off,
                        R.drawable.lamp1_on,
                        R.drawable.lamp1_burst,
                        R.drawable.lamp1_burned
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
