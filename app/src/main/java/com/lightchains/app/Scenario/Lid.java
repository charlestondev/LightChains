package com.lightchains.app.Scenario;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import com.lightchains.app.R;

/**
 * Created by charleston on 17/09/14.
 */
public class Lid extends Sprite {
    static BitmapContainer bc = new BitmapContainer(1);//constructor creates the array;
    public Lid(Context context, int x, int y, int width){
        this.width = width;
        this.x = x;
        this.y = y;
        if(bc.bitmapFrames[Sprite.STAND]==null){
            Bitmap lid = BitmapFactory.decodeResource(context.getResources(), R.drawable.lid);
            bc.bitmapFrames[Sprite.STAND] = new Bitmap[]{
                    Bitmap.createScaledBitmap(lid, width ,width,false)};
        }
        super.bc = bc;
        super.paint = new Paint();
        paint.setAlpha(0);
    }
}
