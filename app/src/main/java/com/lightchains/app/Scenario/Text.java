package com.lightchains.app.Scenario;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Created by charleston on 22/09/14.
 */
public class Text extends Sprite{
    String text;
    int textSize;
    int color;
    Typeface font;
    Rect bounds;
    Paint paint;
    public Text(Context ctx,String text, int textSize, int color, int x, int y){
        this.text = text;
        this.textSize = textSize;
        this.color = color;
        this.font = Typeface.createFromAsset(ctx.getAssets(), "Pixel Cyr Normal.ttf");
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(this.font);
        paint.setColor(this.color);
        paint.setTextSize(this.textSize);
        this.bounds = new Rect();
        paint.getTextBounds(this.text,0,this.text.length(),bounds);
        this.x = x;
        this.y = y;
    }
    public Text(Context ctx, String text, int textSize, int x, int y){
        this(ctx, text, textSize, Color.BLACK, x, y);
    }
    public void draw(Canvas c){
        c.drawText(text, x, y, paint);
    }
}
