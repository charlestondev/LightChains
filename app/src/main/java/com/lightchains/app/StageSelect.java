package com.lightchains.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by charleston on 14/10/14.
 */
public class StageSelect extends Activity {
    SharedPreferences prefs;
    MediaPlayer song;
    AdView mAdView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        setContentView(R.layout.stage_select);

        Typeface font = Typeface.createFromAsset(getAssets(), "Pixel Cyr Normal.ttf");
        TextView title = (TextView)findViewById(R.id.title);
        title.setTypeface(font);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //prefs = getSharedPreferences("scores", MODE_PRIVATE);
        //GridView gridview = (GridView) findViewById(R.id.gridview);
        //gridview.setAdapter(new ImageAdapter(this));

        /*gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int previousScore = prefs.getInt("stage"+position,0);//score of previous stage
                Log.i("previous score", previousScore+"");
                if(position == 0 || previousScore !=0){
                    Intent intent = new Intent(StageSelect.this, StartGame.class);
                    Bundle extras = new Bundle();
                    extras.putInt("stage_number", position+1);
                    if(position==0)
                        extras.putBoolean("story", true);
                    intent.putExtras(extras);
                    Log.i("extras", intent.getExtras().getInt("stage_number")+"");
                    startActivity(intent);
                }
                //Toast.makeText(StageSelect.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        System.gc();
        prefs = getSharedPreferences("scores", MODE_PRIVATE);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int previousScore = prefs.getInt("stage" + position, 0);//score of previous stage
                Log.i("previous score", previousScore + "");
                if (position == 0 || previousScore != 0) {
                    Intent intent = new Intent(StageSelect.this, StartGame.class);
                    Bundle extras = new Bundle();
                    extras.putInt("stage_number", position + 1);
                    if (position == 0)
                        extras.putBoolean("story", true);
                    intent.putExtras(extras);
                    Log.i("extras", intent.getExtras().getInt("stage_number") + "");
                    startActivity(intent);
                }
                //Toast.makeText(StageSelect.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        song = MediaPlayer.create(this,R.raw.stageselect2);
        song.setLooping(true);
        song.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                song.start();
            }
        });
        Log.i("stage select", "on resume");
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
        song.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        song.stop();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),Menu.class));
    }
}
