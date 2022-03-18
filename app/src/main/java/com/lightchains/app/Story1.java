package com.lightchains.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by charleston on 20/08/14.
 */
public class Story1 extends Activity {
    List pictures;
    ImageView picture;
    Iterator picturesIterator;
    TextView textView1;
    TextView textView2;
    Thread storyThread;
    MediaPlayer song;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        setContentView(R.layout.history_1);
        Typeface font = Typeface.createFromAsset(getAssets(), "Pixel Cyr Normal.ttf");
        textView1 = (TextView)findViewById(R.id.text_picture1);
        textView2 = (TextView)findViewById(R.id.text_picture2);
        textView1.setTypeface(font);
        textView2.setTypeface(font);

        pictures = new ArrayList<Picture>();
        pictures.add(new Picture(R.drawable.third_fourth,getResources().getString(R.string.third),getResources().getString(R.string.fourth),4000));
        pictures.add(new Picture(R.drawable.fifth_sixth,getResources().getString(R.string.fifth),getResources().getString(R.string.sixth),4000));
        pictures.add(new Picture(R.drawable.seventh,getResources().getString(R.string.seventh),"",4000));
        pictures.add(new Picture(R.drawable.eighth_ninth,getResources().getString(R.string.eighth),getResources().getString(R.string.ninth),4000));
        pictures.add(new Picture(R.drawable.tenth_eleventh,"",getResources().getString(R.string.tenth),4000));
        picture = (ImageView) findViewById(R.id.story_background);
        picturesIterator = pictures.iterator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        song = MediaPlayer.create(this, R.raw.story1);
        song.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                song.start();
            }
        });
        storyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread changePicture = null;
                boolean skip = false;
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    skip = true;
                    if(changePicture!=null)
                        changePicture.interrupt();
                }
                while(picturesIterator.hasNext() && !skip){
                    final Picture current = (Picture)picturesIterator.next();
                    changePicture = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            current.show();
                        }
                    });
                    picture.post(changePicture);
                    try {
                        Thread.sleep(current.duration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        skip=true;
                    }
                }
                startGame();
            }
        });
        storyThread.start();
    }
    public void startGame(){
        storyThread = null;
        picture = null;
        pictures = null;
        song.stop();
        song.release();
        song = null;
        startActivity(new Intent(getApplicationContext(), StageSelect.class));
    }
    public void skipStory(View v){
        storyThread.interrupt();
    }
    private class Picture{
        int resourceId;
        String text1;
        String text2;
        int duration;
        public Picture(int resourceId, String text1, String text2, int duration){
            this.resourceId = resourceId;
            this.text1 = text1;
            this.text2 = text2;
            this.duration = duration;
        }
        public void show(){
            if(!text1.equals("")){
                textView1.setText(text1);
                textView1.setVisibility(View.VISIBLE);
            }
            else
                textView1.setVisibility(View.GONE);
            if(!text2.equals("")){
                textView2.setText(text2);
                textView2.setVisibility(View.VISIBLE);
            }
            else
                textView2.setVisibility(View.GONE);
            picture.setImageDrawable(getResources().getDrawable(resourceId));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(song!=null)
            song.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
