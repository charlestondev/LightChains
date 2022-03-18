package com.lightchains.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by charleston on 12/08/14.
 * play games not implemented by the issue of memory leak, regarding that this game is using "activities" for each screen
 */
public class Menu extends FragmentActivity{

    Button startGame;
    MediaPlayer songMenu;
    AdView mAdView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.menu);
        //lamp1 = (ImageView)findViewById(R.id.lamp1);
        //led1 = (ImageView)findViewById(R.id.led1);
        startGame = (Button)findViewById(R.id.button_start_game);

        Animation characterMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.character_menu);
        ImageView characterMenu = (ImageView)findViewById(R.id.eyelids_menu);
        characterMenu.startAnimation(characterMenuAnimation);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }

        super.onPause();
        songMenu.stop();
        songMenu.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        System.gc();
        songMenu = MediaPlayer.create(this, R.raw.menu);
        songMenu.setLooping(true);
        songMenu.start();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();
    }

    public void startGame(View v){
        finish();
        startActivity(new Intent(getApplicationContext(), Story1.class));
    }
    public void credits(View v){
        DialogFragment dialog =  new CreditsDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    public void help(View v){
        DialogFragment dialog =  new HelpDialog();
        dialog.show(getSupportFragmentManager(), "dialog2");
    }
}
