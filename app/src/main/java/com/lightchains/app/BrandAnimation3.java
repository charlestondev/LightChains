package com.lightchains.app;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by charleston on 20/08/14.
 */
public class BrandAnimation3 extends Activity {
    ImageView brand_logo;
    MediaPlayer brand_sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.brand_view);
        brand_logo = (ImageView)findViewById(R.id.brand_logo);
        brand_sound = MediaPlayer.create(this, R.raw.explosion);
        Animation brand_animation = AnimationUtils.loadAnimation(this,R.anim.brand_animation);
        brand_logo.startAnimation(brand_animation);
        brand_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        brand_sound.start();
                    }
                }).start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent menu = new Intent(getApplicationContext(), Menu.class);
                        BrandAnimation3.this.startActivity(menu);
                    }
                }).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onStop() {
        super.onStop();
        brand_sound.release();
        brand_sound = null;
    }
}
