package com.lightchains.app.Scenario;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by charleston on 20/10/14.
 */
public class Balloon extends Sprite {
    int width, height;
    int x, y;
    public Balloon(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        paint.setColor(Color.WHITE);
        paint.setAlpha(150);
    }
    public void draw(Canvas c){
        c.drawRect(x, y, width, height,paint);
    }
}
