package com.lightchains.app.Scenario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.lightchains.app.R;
import com.lightchains.app.StageSelect;
import com.lightchains.app.StartGame;
import com.lightchains.app.Story1;
import com.lightchains.app.Story2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by charleston on 17/09/14.
 */
public class Scenario{
    Context context;
    public Sprite spritesMatrix[][];
    public List<Sprite> spritesList;
    public List<Lamp> chain;
    public Sprite background;
    public Sprite firefly;
    public Balloon balloon;
    public Text text;
    public Sprite endMenu;
    public Sprite pauseMenu;
    public boolean pause = false;
    public Sprite buttonRetry;
    public Sprite buttonStageSelect;
    public Sprite buttonNext;
    public Sprite buttonPlay;
    public Sprite dark;
    public Sprite star1;
    public Sprite star2;
    public Sprite star3;
    public Text textFailed;
    public Text textStageNumber;
    public int backgroundHeightDifference;
    public int rows,cols, width, height;
    public int celSize;
    public int spareHeight;
    public int margin;
    public String[] stageLamps;
    Stage stage;
    Text debug;
    boolean waiting = false;
    Map<String,ScenarioAction> actions;
    String currentAction;
    boolean touch_down = false;
    boolean restarting = true;
    boolean stageSelecting = true;
    boolean animating = false;
    public static final String ACTION_CLEAR_BURNED_LAMPS = "clear burned lamps";
    public static final String ACTION_DOWN_LAMPS = "down lamps";
    public static final String ACTION_FILL_MATRIX = "fill matrix";
    public static final String ACTION_STAGE_CLEARED = "stage cleared";
    public static final String ACTION_STAGE_FAILED = "stage failed";
    public static final String ACTION_GAME_RUNNING = "game running";
    public static final String ACTION_STAGE_INTRO = "stage intro";
    public static final String ACTION_STAGE_STORY = "stage story";
    public static final String ACTION_PAUSE= "stage pause";
    public static final String ACTION_NOT_TOUCH= "not touch";

    Text time;
    Text battery;
    Text score;
    int smallest;
    int small;
    int medium;
    int big;
    double batteryValue;
    double batteryCount;
    int scoreValue;
    int scoreCount;
    int count_delay_time;

    float scaleFactor;
    boolean story;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String actionBeforePause;
    MediaPlayer song;
    MediaPlayer intro;
    MediaPlayer stageFailed;
    MediaPlayer stageCleared;
    int lampsSoundResources[];
    public Scenario(Context context, int width, int height,int stageNumber, boolean story){
        //some configurations
        rows = 10;
        cols = 8;
        this.width = width;
        this.height = height;
        this.margin = 5;
        this.celSize = (width-(2*margin))/cols;
        this.spareHeight = height-(rows*celSize)-margin-3;
        this.smallest = (int)(height*0.02);
        this.small = (int)(height*0.03);
        this.medium = (int)(height*0.04);
        this.big = (int)(height*0.05);
        this.story = story;
        Log.i("scenario", story+"");
        this.context=context;
        prefs = context.getSharedPreferences("scores", context.MODE_PRIVATE);

        stage = new Stage(context,stageNumber,width);
        song.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        intro.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        //lampsSoundResources = new int[]{R.raw.lamp11,R.raw.lamp12,R.raw.lamp13,R.raw.lamp14,R.raw.lamp15,R.raw.lamp16,R.raw.lamp17,R.raw.lamp18,R.raw.lamp19,R.raw.lamp20};
        lampsSoundResources = new int[]{R.raw.lamp17,R.raw.lamp18,R.raw.lamp19,R.raw.lamp20};
        //background image and difference to animate
        backgroundHeightDifference = stage.background.getHeight()-height;

        //add background as first element to be sowed backwards
        BitmapContainer bc = new BitmapContainer(1);
        bc.bitmapFrames[Sprite.STAND] = new Bitmap[]{stage.background};
        background = new Sprite(bc);
        background.y = 0;//-backgroundHeightDifference;
        spritesList = new ArrayList<Sprite>();
        spritesList.add(background);

        BitmapContainer bc2 = new BitmapContainer(1);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.firefly);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor),(int)(bitmap.getHeight()*scaleFactor),false);

        bc2.bitmapFrames[Sprite.STAND] = new Bitmap[]{bitmap};
        firefly = new Sprite(bc2);
        firefly.x = margin;
        firefly.y = height - firefly.bc.bitmapFrames[Sprite.STAND][0].getHeight() - margin;


        balloon = new Balloon(5,firefly.y-big,firefly.x+width-(2*smallest),firefly.y+(3*smallest)-big);
        text = new Text(context, context.getString(R.string.inside_robot),smallest,balloon.x+smallest,balloon.y+smallest+smallest);

        BitmapContainer bc3 = new BitmapContainer(1);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.background_end_menu);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor),(int)(bitmap.getHeight()*scaleFactor),false);

        bc3.bitmapFrames[Sprite.STAND] = new Bitmap[]{bitmap};
        endMenu = new Sprite(bc3);
        endMenu.x = (background.width-endMenu.width)/2;
        endMenu.y = height/2-endMenu.height/2;

        BitmapContainer bc4 = new BitmapContainer(2);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.button_retry);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor),(int)(bitmap.getHeight()*scaleFactor),false);
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.raw.button_retry_touched);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int)(bitmap2.getWidth()*scaleFactor),(int)(bitmap2.getHeight()*scaleFactor),false);
        bc4.bitmapFrames[Sprite.STAND] = new Bitmap[]{bitmap};
        bc4.bitmapFrames[1] = new Bitmap[]{bitmap2};
        buttonRetry = new Sprite(bc4);
        buttonRetry.x = (int)(width*0.33)-buttonRetry.width;
        buttonRetry.y = height/2;
        //buttonRetry.setState(1);

        BitmapContainer bc5 = new BitmapContainer(2);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.button_stage_select);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor),(int)(bitmap.getHeight()*scaleFactor),false);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.raw.button_stage_select_touched);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int)(bitmap2.getWidth()*scaleFactor),(int)(bitmap2.getHeight()*scaleFactor),false);
        bc5.bitmapFrames[Sprite.STAND] = new Bitmap[]{bitmap};
        bc5.bitmapFrames[1] = new Bitmap[]{bitmap2};
        buttonStageSelect = new Sprite(bc5);
        buttonStageSelect.x = (int)(width*0.5)-buttonStageSelect.width/2;
        buttonStageSelect.y = height/2;

        BitmapContainer bc6 = new BitmapContainer(2);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.button_next);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor),(int)(bitmap.getHeight()*scaleFactor),false);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.raw.button_next_touched);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int)(bitmap2.getWidth()*scaleFactor),(int)(bitmap2.getHeight()*scaleFactor),false);
        bc6.bitmapFrames[Sprite.STAND] = new Bitmap[]{bitmap};
        bc6.bitmapFrames[1] = new Bitmap[]{bitmap2};
        buttonNext = new Sprite(bc6);
        buttonNext.x = (int)(width*0.66);
        buttonNext.y = height/2+buttonStageSelect.height/2-buttonNext.height/2;

        BitmapContainer bc7 = new BitmapContainer(2);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.button_play);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor),(int)(bitmap.getHeight()*scaleFactor),false);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.raw.button_play_touched);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int)(bitmap2.getWidth()*scaleFactor),(int)(bitmap2.getHeight()*scaleFactor),false);

        bc7.bitmapFrames[Sprite.STAND] = new Bitmap[]{bitmap};
        bc7.bitmapFrames[1] = new Bitmap[]{bitmap2};
        buttonPlay = new Sprite(bc7);
        buttonPlay.x = (int)(width*0.66);
        buttonPlay.y = height/2;

        BitmapContainer bc8 = new BitmapContainer(2);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.star_loose);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor),(int)(bitmap.getHeight()*scaleFactor),false);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.raw.star_win);
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int)(bitmap2.getWidth()*scaleFactor),(int)(bitmap2.getHeight()*scaleFactor),false);
        bc8.bitmapFrames[Sprite.STAND] = new Bitmap[]{bitmap};
        bc8.bitmapFrames[1] = new Bitmap[]{bitmap2};
        star1 = new Sprite(bc8);
        star1.x = buttonRetry.x;
        star1.y = buttonStageSelect.y - star1.height - smallest;

        star2 = new Sprite(bc8);
        star2.x = buttonStageSelect.x;
        star2.y = buttonStageSelect.y - star1.height - smallest;

        star3 = new Sprite(bc8);
        star3.x = buttonNext.x;
        star3.y = buttonStageSelect.y - star1.height - smallest;


        textFailed = new Text(context, context.getString(R.string.you_lose), medium, this.width/2, star1.y+medium);
        textFailed.paint.setTextAlign(Paint.Align.CENTER);

        textStageNumber = new Text(context, context.getString(R.string.stage)+stageNumber, big,Color.rgb(250,180,0), this.width/2, this.height/2);
        textStageNumber.paint.setTextAlign(Paint.Align.CENTER);
        spritesList.add(textStageNumber);

        endMenu.paint.setAlpha(0);
        buttonRetry.paint.setAlpha(0);
        buttonStageSelect.paint.setAlpha(0);
        buttonNext.paint.setAlpha(0);

        BitmapContainer bc9 = new BitmapContainer(1);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.background_pause);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*scaleFactor),(int)(bitmap.getHeight()*scaleFactor),false);

        bc9.bitmapFrames[Sprite.STAND] = new Bitmap[]{bitmap};
        pauseMenu = new Sprite(bc9);
        pauseMenu.x = (background.width-pauseMenu.width)/2;
        pauseMenu.y = height/2-pauseMenu.height/2;

        firefly.paint.setAlpha(0);
        balloon.paint.setAlpha(0);
        text.paint.setAlpha(0);

        //Robot Battery
        Text batteryLabel = new Text(context, context.getString(R.string.robot_battery),smallest,Color.rgb(240,255,50),10,smallest);
        spritesList.add(batteryLabel);
        batteryValue = 100;
        batteryCount = 100;
        battery = new Text(context, batteryValue+"",medium,Color.rgb(100,255,50),10,smallest+medium);
        spritesList.add(battery);

        //Time
        Text timeLabel = new Text(context, context.getString(R.string.time),smallest,Color.LTGRAY,this.width/2,smallest);
        timeLabel.paint.setTextAlign(Paint.Align.CENTER);
        spritesList.add(timeLabel);

        time = new Text(context, String.valueOf(stage.time),big,Color.WHITE,this.width/2,smallest+big);
        time.paint.setTextAlign(Paint.Align.CENTER);
        spritesList.add(time);

        //Score
        Text scoreLabel = new Text(context, context.getString(R.string.score),smallest,Color.rgb(100,100,255),this.width-10,smallest);
        scoreLabel.x = scoreLabel.x-scoreLabel.bounds.width();
        spritesList.add(scoreLabel);
        scoreValue = 0;
        scoreCount = 0;
        score = new Text(context, String.format("%05d", scoreValue),medium,Color.rgb(150,150,255),this.width-10,smallest+medium);
        score.x = score.x-score.bounds.width();
        spritesList.add(score);

        debug = new Text(context, "teste",small,Color.rgb(150,150,255),this.width/2,100);
        debug.paint.setTextAlign(Paint.Align.CENTER);
        //spritesList.add(debug);
        chain = new ArrayList<Lamp>();

        fillMatrix(context);
        createScenarioActions();
        currentAction = ACTION_STAGE_INTRO;
        setWait(true);

    }
    public void draw(Canvas canvas){
        if(currentAction != null)
        {
            try {
                actions.get(currentAction).execute();
            }
            catch (NullPointerException e){
                Log.e("exception captured on draw scenario", "null pointer exception");
            }catch (Exception e) {
                if(e.getMessage().equals("End action exception"))
                {
                    if(batteryValue==0){
                        currentAction = ACTION_STAGE_CLEARED;
                    }
                    else
                        currentAction=ACTION_GAME_RUNNING;
                }
                else
                {
                    Log.e("exception captured on draw scenario", e.getMessage());
                }

            }
            if(batteryValue>0)
                time.text = countDownTime()+"";
        }
        synchronized (getSpritesList()) {
            int size = getSpritesList().size();
            for (int i = 0; i < size; i++) {
                Sprite sprite = getSpritesList().get(i);
                sprite.draw(canvas);
            }
        }
    }
    public int countDownTime(){
        count_delay_time++;
        if(count_delay_time>=60){
            count_delay_time = 0;
            stage.time--;
            if(stage.time<0)
                stage.time=0;
        }
        if(stage.time==0){
            currentAction=ACTION_STAGE_FAILED;
        }
        return stage.time;
    }
    public void setBatteryAndScore(int score, double consume){
        scoreValue += score;
        batteryValue -= consume;
        if(batteryValue<=0){
            batteryValue = 0;
        }
    }
    public void decreaseBatteryScorePoints(){
        if(batteryCount>batteryValue){
            batteryCount-=0.5;
            if(batteryCount<0)
                batteryCount=0;
        }
        if(scoreCount<scoreValue)
            scoreCount++;
        battery.text = String.format("%.1f",batteryCount);
        score.text = String.format("%05d",scoreCount);
    }
    interface ScenarioAction{
        int frame = 0;
        public void execute() throws Exception;
    }
    public void createScenarioActions(){
        actions = new HashMap<String, ScenarioAction>();
        actions.put(ACTION_CLEAR_BURNED_LAMPS, new ScenarioAction() {
            int frame = 0;
            @Override
            public void execute() throws Exception {
                if(frame<100){
                    //for(int i = 0; i < chain.size();i++)
                    //    chain.get(i).paint.setAlpha(104-frame);
                    for(int i = 0; i < rows;i++)
                        for(int j = 0; j < cols; j++){
                            if(spritesMatrix[i][j].state == Lamp.LAMP_BURNED || spritesMatrix[i][j].state == Lamp.LAMP_BURST )
                                spritesMatrix[i][j].paint.setAlpha(104-frame);
                        }
                    frame+=4;
                }else{
                    frame = 0;
                    //for(int i = 0; i < chain.size();i++) {
                    //    Sprite lamp = chain.get(i);
                    //    int row = getRow(lamp.y);
                    //    int col = getCol(lamp.x);
                    for(int i = 0; i < rows;i++)
                        for(int j = 0; j < cols; j++){
                            if(spritesMatrix[i][j].state == Lamp.LAMP_BURNED || spritesMatrix[i][j].state == Lamp.LAMP_BURST ){
                                getSpritesList().remove(spritesMatrix[i][j]);
                                if(spritesMatrix[i][j] instanceof SpecialRed || spritesMatrix[i][j] instanceof SpecialGreen || spritesMatrix[i][j] instanceof SpecialYellow || spritesMatrix[i][j] instanceof SpecialBlue)
                                    spritesMatrix[i][j] = new Lid(context, j*celSize+8,i*celSize+spareHeight,celSize);
                                else
                                    spritesMatrix[i][j] = null;
                            }
                        }
                        //spritesMatrix[row][col] = null;
                        //getSpritesList().remove(lamp);
                    //}
                    //chain.clear();
                    //throw new Exception("End action exception");
                    currentAction = ACTION_DOWN_LAMPS;
                }

            }
        });
        actions.put(ACTION_DOWN_LAMPS, new ScenarioAction() {
            int count = 0;
            int max = 6;
            int difference = celSize%max;
            int i = spritesMatrix.length-1;
            boolean moved = false;
            @Override
            public void execute() throws Exception {
                moved = false;
                for (int j = 0; j < spritesMatrix[0].length; j++){
                    if(spritesMatrix[i][j]==null){
                        for(int k = i-1;k>=0;k--){
                            // Lamps that doesn't go down
                            if(!(spritesMatrix[k][j] instanceof Lid) && spritesMatrix[k][j]!=null && !(spritesMatrix[k][j] instanceof SpecialRed) && !(spritesMatrix[k][j] instanceof SpecialGreen) && !(spritesMatrix[k][j] instanceof SpecialYellow) && !(spritesMatrix[k][j] instanceof SpecialBlue)){
                                spritesMatrix[k][j].y += celSize/max;
                                if(count==1){
                                    int jump  = 0;
                                    for(int l = k+1;l<i;l++)
                                        if((spritesMatrix[l][j] instanceof Lid) || (spritesMatrix[l][j] instanceof SpecialRed) || (spritesMatrix[l][j] instanceof SpecialBlue) || (spritesMatrix[l][j] instanceof SpecialGreen) || (spritesMatrix[l][j] instanceof SpecialYellow))
                                            jump++;
                                        else
                                            break;
                                    spritesMatrix[k][j].y += celSize*jump;
                                    spritesMatrix[k][j].y += difference;
                                }
                                moved = true;
                            }
                        }
                    }

                }
                if(moved)
                    count++;
                else{
                    i--;
                    count=0;
                }
                if(count==max){
                    boolean nullCleard = true;
                    for(int aux=0;aux<spritesMatrix[i].length;aux++ )
                        if(spritesMatrix[i][aux]==null)
                            nullCleard = false;
                    if(nullCleard)
                        i--;
                    for(int ii = spritesMatrix.length-1 ;ii>=0;ii--)
                        for(int j = 0;j<spritesMatrix[0].length;j++){
                            if(spritesMatrix[ii][j]!=null) {
                                int row = getRow(spritesMatrix[ii][j].y);
                                int col = getCol(spritesMatrix[ii][j].x);
                                spritesMatrix[row][col] = spritesMatrix[ii][j];
                                if(row!=ii || col != j)
                                    spritesMatrix[ii][j] = null;
                            }
                        }
                    count=0;
                }
                if(i==0){
                    i = spritesMatrix.length-1;
                    //todo remove this snippet
                    /*for(int ii = spritesMatrix.length-1 ;ii>=0;ii--)
                        for(int j = 0;j<spritesMatrix[0].length;j++){
                            if(spritesMatrix[ii][j]!=null) {
                                int row = getRow(spritesMatrix[ii][j].y);
                                int col = getCol(spritesMatrix[ii][j].x);
                                spritesMatrix[row][col] = spritesMatrix[ii][j];
                                if(row!=ii || col != j)
                                    spritesMatrix[ii][j] = null;
                            }
                        }
                    */
                    currentAction = ACTION_FILL_MATRIX;
                    //throw new Exception("End action exception");
                }
            }
        });
        actions.put(ACTION_FILL_MATRIX,new ScenarioAction() {
            int frame = 0;
            float step = 255/30;
            @Override
            public void execute() throws Exception {
                if(frame==1)
                    fillMatrix(context);
                if(frame<=30){
                    for(int i = 0;i<spritesMatrix.length;i++)
                        for(int j = 0;j<spritesMatrix[0].length;j++)
                            if(spritesMatrix[i][j] != null)
                                if(spritesMatrix[i][j].paint.getAlpha()<230)
                                    spritesMatrix[i][j].paint.setAlpha((int)(frame*step));
                    frame++;
                }
                else{
                    frame = 0;
                    throw new Exception("End action exception");
                }
            }
        });
        actions.put(ACTION_STAGE_CLEARED,new ScenarioAction() {
            int frame = 0;
            int stop1 = 30;
            int end = 90;
            float alphaStep = 255/stop1;
            @Override
            public void execute() throws Exception {
                decreaseBatteryScorePoints();
                if(frame<=end){
                    if(frame==0){
                        setWait(true);
                        stageCleared.start();
                        stageCleared.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mediaPlayer.release();
                            }
                        });
                        song.stop();
                        //song.pause();
                        //song.release();

                        //spritesList.add(dark);
                        animating = true;
                        restarting = false;
                        stageSelecting = false;
                        spritesList.add(endMenu);
                        spritesList.add(buttonRetry);
                        spritesList.add(buttonStageSelect);
                        spritesList.add(buttonNext);
                        spritesList.add(star1);
                        spritesList.add(star2);
                        spritesList.add(star3);
                        star1.setState(1);
                        MediaPlayer star1Sound = MediaPlayer.create(context, R.raw.star1);
                        star1Sound.start();
                        if(prefs.getInt("stage"+stage.number,0)<scoreValue){
                            editor = prefs.edit();
                            editor.putInt("stage"+stage.number,scoreValue);
                            editor.commit();
                        }
                    }
                    if(frame<stop1){
                        endMenu.paint.setAlpha((int)(frame*alphaStep));
                        buttonRetry.paint.setAlpha((int)(frame*alphaStep));
                        buttonStageSelect.paint.setAlpha((int)(frame*alphaStep));
                        buttonNext.paint.setAlpha((int)(frame*alphaStep));
                    }
                    if(frame>stop1 && frame <= end){
                        if(frame==60 && scoreValue >= stage.scoreTwoStars){
                            star2.setState(1);
                            MediaPlayer star2Sound = MediaPlayer.create(context, R.raw.star2);
                            star2Sound.start();
                        }
                        if(frame==end && scoreValue >= stage.scoreThreeStars){
                            star3.setState(1);
                            MediaPlayer star3Sound = MediaPlayer.create(context, R.raw.star3);
                            star3Sound.start();
                        }
                    }

                    frame++;
                    if(frame==end)
                        animating = false;
                }
            }
        });
        actions.put(ACTION_GAME_RUNNING, new ScenarioAction() {
            @Override
            public void execute() throws Exception {
                decreaseBatteryScorePoints();
                if(isWaiting())
                    setWait(false);
            }
        });
        actions.put(ACTION_NOT_TOUCH, new ScenarioAction() {
            @Override
            public void execute() throws Exception {
                decreaseBatteryScorePoints();
            }
        });
        actions.put(ACTION_STAGE_FAILED, new ScenarioAction() {
            int frame = 0;
            int end = 30;
            float alphaStep = 255/end;
            @Override
            public void execute() throws Exception {
                if(frame<=end){
                    if(frame==0){
                        setWait(true);
                        //song.stop();
                        //song.pause();
                        //song.release();

                        stageFailed.start();
                        stageFailed.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mediaPlayer.release();

                            }
                        });
                        song.stop();

                        //song.release();
                        //spritesList.add(dark);
                        animating = true;
                        restarting = false;
                        stageSelecting = false;
                        spritesList.add(endMenu);
                        spritesList.add(buttonRetry);
                        spritesList.add(buttonStageSelect);
                        spritesList.add(textFailed);
                        //spritesList.add(buttonNext);
                    }
                    endMenu.paint.setAlpha((int)(frame*alphaStep));
                    buttonRetry.paint.setAlpha((int)(frame*alphaStep));
                    buttonStageSelect.paint.setAlpha((int)(frame*alphaStep));
                    buttonNext.paint.setAlpha((int)(frame*alphaStep));
                    frame++;
                    if(frame==end)
                        animating = false;
                }
            }
        });
        actions.put(ACTION_STAGE_INTRO, new ScenarioAction() {
            int frame = 0;
            int stop1 = 60;
            int stop2 = stop1+60;
            int stop3 = stop2+60;

            //int finalFrame = stop1+stop2+stop3+120;
            float pixelStep = backgroundHeightDifference/60;
            int pixelLost = backgroundHeightDifference%60;
            float alphaStep = 255/60;
            @Override
            public void execute() throws Exception {

                if(frame<=stop3){
                    if(frame==stop1)
                        background.y -= pixelLost;
                    if(frame>stop1 && frame<stop2)
                        background.y -= pixelStep;
                    if(frame==stop2){
                        fillMatrix(context);
                    }

                    if(frame>stop2 && frame < stop3) {
                        for (int i = 0; i < spritesMatrix.length; i++)
                            for (int j = 0; j < spritesMatrix[0].length; j++){
                                if (spritesMatrix[i][j] != null)
                                    if (spritesMatrix[i][j].paint.getAlpha() < 230)
                                        spritesMatrix[i][j].paint.setAlpha((int) ((frame-stop2) * alphaStep));

                            }
                        textStageNumber.paint.setAlpha(10);
                    }

                    frame++;
                }else{
                    frame = 0;
                    if(!story)
                        throw new Exception("End action exception");
                    else
                        currentAction = ACTION_STAGE_STORY;
                }
                count_delay_time--;
            }
        });
        actions.put(ACTION_STAGE_STORY, new ScenarioAction() {
            int frame = 0;
            int stop1 = 60;
            int stop2 = stop1+60;
            int stop3 = stop2+60;
            @Override
            public void execute() throws Exception {
                if(frame <= stop3){
                    if(frame==1){
                        spritesList.add(firefly);
                        spritesList.add(balloon);
                        spritesList.add(text);
                        firefly.paint.setAlpha(255);
                        balloon.paint.setAlpha(255);
                        text.paint.setAlpha(255);
                    }
                    if(frame==stop1)
                        text.text = context.getString(R.string.found_circuit);
                    if(frame==stop2)
                        text.text = context.getString(R.string.need_todo_something);
                    if(frame==stop3){
                        firefly.paint.setAlpha(0);
                        balloon.paint.setAlpha(0);
                        text.paint.setAlpha(0);
                    }
                    frame++;
                }else{
                    frame = 0;
                    throw new Exception("End action exception");
                }
                count_delay_time--;
            }
        });
        actions.put(ACTION_PAUSE, new ScenarioAction() {
            boolean showMenu = false;
            @Override
            public void execute() throws Exception {
                if(pause){
                    if(!showMenu){
                        setWait(true);
                        showMenu = true;
                        spritesList.add(pauseMenu);
                        spritesList.add(buttonRetry);
                        spritesList.add(buttonStageSelect);

                        buttonRetry.paint.setAlpha(255);
                        buttonStageSelect.paint.setAlpha(255);
                        spritesList.add(buttonPlay);

                    }
                    count_delay_time--;
                }
                else{
                    showMenu = false;
                    currentAction = actionBeforePause;
                    spritesList.remove(pauseMenu);
                    buttonRetry.paint.setAlpha(0);
                    buttonStageSelect.paint.setAlpha(0);

                    spritesList.remove(buttonRetry);
                    spritesList.remove(buttonStageSelect);
                    spritesList.remove(buttonPlay);

                }
            }
        });
    }
    public void pause(boolean stopSounds){
        if(!pause && !touch_down && currentAction != ACTION_STAGE_CLEARED && currentAction != ACTION_STAGE_FAILED){
            actionBeforePause = currentAction;
            currentAction = ACTION_PAUSE;
            if(stopSounds)
                song.pause();
            else
                song.setVolume(0.5f,0.5f);
        }
        else{
            if(!song.isPlaying())
                song.start();
            song.setVolume(1,1);
        }
        pause = !pause;
    }
    public void fillMatrix(Context context){
        if(spritesMatrix==null){
            //if not exist
            spritesMatrix = new Sprite[rows][];
            for(int i = 0; i < rows; i++)
                spritesMatrix[i] = new Sprite[cols];
            stage.fillVoidSpaces();
        }
        stageLamps = stage.getLamps();

        //distribute lamps throughout the matrix and in the list to be drawn
        Random random = new Random();
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++){
                if(spritesMatrix[i][j]==null) {
                    spritesMatrix[i][j] = createLamp(context, stageLamps[random.nextInt(stageLamps.length)], j * celSize + 8, i * celSize + spareHeight);
                    spritesList.add(spritesMatrix[i][j]);
                }
            }
        //verify if there is Specials
        boolean thereIsSpecial = false;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++){
                if(spritesMatrix[i][j] instanceof SpecialRed || spritesMatrix[i][j] instanceof SpecialBlue || spritesMatrix[i][j] instanceof SpecialGreen || spritesMatrix[i][j] instanceof SpecialYellow){
                    thereIsSpecial = true;
                    i = rows;
                    j = cols;
                }
            }

        //verify if there is chains
        boolean thereIsChain = false;

        while(!thereIsChain && !thereIsSpecial){
            for(int i = 0; i < rows;i++){// three horizontal
                for(int j = 0;j < cols-2;j++){
                    if(!(spritesMatrix[i][j] instanceof Lid)) {
                        if (spritesMatrix[i][j].getClass().getName().equals(spritesMatrix[i][j + 1].getClass().getName())) {
                            if (spritesMatrix[i][j].getClass().getName().equals(spritesMatrix[i][j + 2].getClass().getName())) {
                                thereIsChain = true;
                                j = cols;
                                i = rows;
                            }
                        }
                    }
                }
            }
            if(!thereIsChain){
                for(int i = 0; i < rows -2;i++){// three vertical
                    for(int j = 0;j < cols;j++){
                        if(!(spritesMatrix[i][j] instanceof Lid)) {
                            if (spritesMatrix[i][j].getClass().getName().equals(spritesMatrix[i+1][j].getClass().getName())) {
                                if (spritesMatrix[i][j].getClass().getName().equals(spritesMatrix[i+2][j].getClass().getName())) {
                                    thereIsChain = true;
                                    j = cols;
                                    i = rows;
                                }
                            }
                        }
                    }
                }
            }
            if(!thereIsChain){
                for(int i = 0; i < rows-1;i++){
                    for(int j = 0;j < cols-1;j++){
                        // The lid is not verified to know if there is any chain, but the specials would be checked later
                        if(!(spritesMatrix[i][j] instanceof Lid) && !(spritesMatrix[i][j] instanceof SpecialRed) && !(spritesMatrix[i][j] instanceof SpecialGreen) && !(spritesMatrix[i][j] instanceof SpecialYellow) && !(spritesMatrix[i][j] instanceof SpecialBlue)){
                            String unu = spritesMatrix[i][j].getClass().getName();
                            String du = spritesMatrix[i][j+1].getClass().getName();
                            String tri = spritesMatrix[i+1][j+1].getClass().getName();
                            String kvar = spritesMatrix[i+1][j].getClass().getName();
                            if(unu.equals(du) && unu.equals(tri)){
                                thereIsChain = true;
                                j = cols;
                                i = rows;
                            }
                            else if(du.equals(tri) && du.equals(kvar) && !(spritesMatrix[i][j+1] instanceof Lid)){
                                thereIsChain = true;
                                j = cols;
                                i = rows;
                            }
                            else if(tri.equals(kvar) && tri.equals(unu)){
                                thereIsChain = true;
                                j = cols;
                                i = rows;
                            }
                            else if(kvar.equals(unu) && kvar.equals(du)){
                                thereIsChain = true;
                                j = cols;
                                i = rows;
                            }
                        }
                    }
                }
            }

            if(!thereIsChain){
                int row, col;
                for(int i = 0; i < 5; i++){
                    do{
                        row = random.nextInt(rows);
                        col = random.nextInt(cols);
                        //generating random location of the special
                    }while(spritesMatrix[row][col] instanceof Lid || spritesMatrix[row][col] instanceof SpecialRed || spritesMatrix[row][col] instanceof SpecialGreen || spritesMatrix[row][col] instanceof SpecialYellow || spritesMatrix[row][col] instanceof SpecialBlue);
                    getSpritesList().remove(spritesMatrix[row][col]);
                    spritesMatrix[row][col] = createLamp(context, stageLamps[random.nextInt(stageLamps.length)], col * celSize + 8, row * celSize + spareHeight);
                    spritesList.add(spritesMatrix[row][col]);
                }
            }
        }
    }
    public void setWait(boolean value){
        waiting = value;
    }
    public boolean isWaiting(){
        return waiting;
    }

    public boolean onTouch(MotionEvent event){
        if(!isWaiting()) {
            int col = getCol((int) event.getX());
            int row = getRow((int) event.getY());
            if(row >= 0 && row < this.rows && col >= 0 && col < this.cols) {
                if (spritesMatrix[row][col] != null) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //debug.text = String.format("col:%d - row:%d  -- x:%d - y:%d", col, row, (int) event.getX(), (int) event.getY());
                        if (!(spritesMatrix[row][col] instanceof Lid)) {
                            chain.add((Lamp) spritesMatrix[row][col]);
                            spritesMatrix[row][col].setState(Lamp.LAMP_ON);
                            touch_down = true;
                            MediaPlayer lamp1 = MediaPlayer.create(context, lampsSoundResources[0]);
                            lamp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mediaPlayer.start();
                                }
                            });
                            lamp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mediaPlayer.release();
                                }
                            });
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE && touch_down) {
                        Lamp lamp = chain.get(chain.size() - 1);
                        int lampRow = getRow(lamp.y);
                        int lampCol = getCol(lamp.x);
                        // when moving don't verify this itens as chains, but the similar ones
                        if (!(spritesMatrix[row][col] instanceof Lid) && !(spritesMatrix[row][col] instanceof SpecialRed) && !(spritesMatrix[row][col] instanceof SpecialGreen) && !(spritesMatrix[row][col] instanceof SpecialYellow) && !(spritesMatrix[row][col] instanceof SpecialBlue)) {
                            if (lampRow != row || lampCol != col) {
                                if (((lampRow - row == 1 || lampRow - row == -1) && lampCol == col) || ((lampCol - col == 1 || lampCol - col == -1) && lampRow == row)) {
                                    if (lamp.getClass().equals(spritesMatrix[row][col].getClass())) {
                                        if (chain.indexOf(spritesMatrix[row][col]) == -1) {
                                            chain.add((Lamp) spritesMatrix[row][col]);
                                            int index = chain.size()-1;
                                            if(index>lampsSoundResources.length-1)
                                                index = lampsSoundResources.length-1;

                                            MediaPlayer lamp1 = MediaPlayer.create(context, lampsSoundResources[index]);
                                            lamp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mediaPlayer) {
                                                    mediaPlayer.start();
                                                }
                                            });
                                            lamp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mediaPlayer) {
                                                    mediaPlayer.release();
                                                }
                                            });
                                            spritesMatrix[row][col].setState(Lamp.LAMP_ON);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                touch_down = false;
                if (chain.size() < 3) {
                    if(chain.size()==1) {
                        if (chain.get(0) instanceof SpecialRed || chain.get(0) instanceof SpecialBlue || chain.get(0) instanceof SpecialGreen || chain.get(0) instanceof SpecialYellow) {
                            setWait(true);
                            currentAction = ACTION_NOT_TOUCH;
                            MediaPlayer lamp1 = MediaPlayer.create(context, R.raw.special);
                            lamp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mediaPlayer.start();
                                }
                            });
                            lamp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mediaPlayer.release();
                                }
                            });
                            Lamp lamp = chain.get(0);
                            lamp.setState(Lamp.LAMP_BURST);
                            int multiply = 0;
                            int bonus = 0;
                            double consume = 0;
                            int point = 0;
                            Class reference;
                            if(lamp instanceof SpecialRed)
                                reference = Led1.class;
                            else if(lamp instanceof SpecialBlue)
                                reference = Led2.class;
                            else if(lamp instanceof SpecialGreen)
                                reference = Led3.class;
                            else
                                reference = Led4.class;

                            //Burst everything of the same kind
                            /* for (int i = 0; i < rows; i++)
                                for (int j = 0; j < cols; j++)
                                    if (spritesMatrix[i][j].getClass().equals(reference)) {
                                        spritesMatrix[i][j].setState(Lamp.LAMP_BURST);
                                        multiply++;
                                    }
                            */

                            int power = (lamp.power/2);
                            int diffRow = getRow(lamp.y)-power;
                            int diffCol = getCol(lamp.x)-power;
                            for(int i = 0; i < (power*2+1); i++)
                                for(int j = 0; j < (power*2+1); j++)
                                    try{
                                        if(i != power || j != power)
                                            if(spritesMatrix[i+diffRow][j+diffCol].getClass().equals(reference)){
                                                spritesMatrix[i+diffRow][j+diffCol].setState(Lamp.LAMP_BURST);
                                                multiply++;
                                            }
                                    }catch(ArrayIndexOutOfBoundsException e){
                                        //only jump
                                    }

                            if(lamp instanceof SpecialRed){
                                bonus = ((SpecialRed)lamp).referenceBonus * multiply;
                                consume = ((SpecialRed)lamp).referenceConsume * multiply;
                                point = ((SpecialRed)lamp).referencePoint * multiply;
    
                                bonus += ((SpecialRed)lamp).bonus;
                                consume += ((SpecialRed)lamp).consume;
                                point += ((SpecialRed)lamp).point;
                            }else if(lamp instanceof SpecialBlue){
                                bonus = ((SpecialBlue)lamp).referenceBonus * multiply;
                                consume = ((SpecialBlue)lamp).referenceConsume * multiply;
                                point = ((SpecialBlue)lamp).referencePoint * multiply;

                                bonus += ((SpecialBlue)lamp).bonus;
                                consume += ((SpecialBlue)lamp).consume;
                                point += ((SpecialBlue)lamp).point;
                            }else if(lamp instanceof SpecialGreen){
                                bonus = ((SpecialGreen)lamp).referenceBonus * multiply;
                                consume = ((SpecialGreen)lamp).referenceConsume * multiply;
                                point = ((SpecialGreen)lamp).referencePoint * multiply;

                                bonus += ((SpecialGreen)lamp).bonus;
                                consume += ((SpecialGreen)lamp).consume;
                                point += ((SpecialGreen)lamp).point;
                            }else if(lamp instanceof SpecialYellow){
                                bonus = ((SpecialYellow)lamp).referenceBonus * multiply;
                                consume = ((SpecialYellow)lamp).referenceConsume * multiply;
                                point = ((SpecialYellow)lamp).referencePoint * multiply;

                                bonus += ((SpecialYellow)lamp).bonus;
                                consume += ((SpecialYellow)lamp).consume;
                                point += ((SpecialYellow)lamp).point;
                            }
                            setBatteryAndScore(point + bonus, consume);
                            chain.clear();
                            currentAction = ACTION_CLEAR_BURNED_LAMPS;
                        } else {
                            for (Sprite sprite : chain)
                                sprite.setState(Sprite.STAND);
                            chain.clear();

                        }
                    }else
                    {
                        for (Sprite sprite : chain)
                            sprite.setState(Sprite.STAND);
                        chain.clear();
                    }
                } else {
                    setWait(true);
                    currentAction = ACTION_NOT_TOUCH;
                    for (Sprite sprite : chain)
                        sprite.setState(Lamp.LAMP_BURST);
                    //setWait(true);
                    MediaPlayer lamp1 = MediaPlayer.create(context,R.raw.burst);
                    lamp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                    lamp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });

                    Lamp lamp= chain.get(0);
                    Lamp start = lamp;
                    Lamp end = chain.get(chain.size()-1);
                    boolean asideOfStart = false;
                    int startCol = getCol(start.x);
                    int startRow = getRow(start.y);
                    if(startRow>0)
                        if(spritesMatrix[startRow-1][startCol].equals(end))
                            asideOfStart = true;
                    if(startCol<spritesMatrix[0].length-1)
                        if(spritesMatrix[startRow][startCol+1].equals(end))
                            asideOfStart = true;
                    if(startRow<spritesMatrix.length-1)
                        if(spritesMatrix[startRow+1][startCol].equals(end))
                            asideOfStart = true;
                    if(startCol>0)
                        if(spritesMatrix[startRow][startCol-1].equals(end))
                            asideOfStart = true;
                    int countLids = 0;
                    for(int i = 0; i < rows;i++)
                        for(int j = 0; j < cols;j++)
                            if(spritesMatrix[i][j] instanceof Lid)
                                countLids++;
                    if(asideOfStart && countLids>0){
                        if(true){//chain.size()==8) {
                            Random random = new Random();
                            boolean allowed;
                            int specialRow;
                            int specialCol;
                            do {
                                allowed = false;
                                specialRow = random.nextInt(rows);
                                specialCol = random.nextInt(cols);
                                if ((spritesMatrix[specialRow][specialCol] instanceof Lid))
                                    allowed = true;
                            } while (!allowed);
                            if(lamp instanceof Led1){
                                //spritesList.remove(spritesMatrix[specialRow][specialCol]);
                                spritesMatrix[specialRow][specialCol] = new SpecialRed(context, specialCol * celSize + 8, (specialRow) * celSize + spareHeight, celSize, chain.size());
                                spritesList.add(spritesMatrix[specialRow][specialCol]);
                            }
                            else if(lamp instanceof Led3){
                                //spritesList.remove(spritesMatrix[specialRow][specialCol]);
                                spritesMatrix[specialRow][specialCol] = new SpecialGreen(context, specialCol * celSize + 8, (specialRow) * celSize + spareHeight, celSize,chain.size());
                                spritesList.add(spritesMatrix[specialRow][specialCol]);
                            }
                            else if(lamp instanceof Led2){
                                //spritesList.remove(spritesMatrix[specialRow][specialCol]);
                                spritesMatrix[specialRow][specialCol] = new SpecialBlue(context, specialCol * celSize + 8, (specialRow) * celSize + spareHeight, celSize,chain.size());
                                spritesList.add(spritesMatrix[specialRow][specialCol]);
                            }
                            else if(lamp instanceof Led4){
                                //spritesList.remove(spritesMatrix[specialRow][specialCol]);
                                spritesMatrix[specialRow][specialCol] = new SpecialYellow(context, specialCol * celSize + 8, (specialRow) * celSize + spareHeight, celSize,chain.size());
                                spritesList.add(spritesMatrix[specialRow][specialCol]);
                            }
                        }
                    }
                    int multiply = chain.size();
                    int bonus = lamp.getBonus()*multiply;
                    double consume = lamp.getConsume()*multiply;
                    int score = lamp.getPoint()*multiply+bonus;
                    setBatteryAndScore(score,consume);
                    chain.clear();
                    currentAction = ACTION_CLEAR_BURNED_LAMPS;
                }
            }
        }else if(!restarting && !stageSelecting && !animating)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(event.getY() > buttonStageSelect.y)
                    if(event.getY() < buttonStageSelect.y+buttonStageSelect.height)
                        if(event.getX() > buttonStageSelect.x)
                            if(event.getX() < buttonStageSelect.x+buttonStageSelect.width)
                                buttonStageSelect.setState(1);
                if(event.getY() > buttonRetry.y)
                    if(event.getY() < buttonRetry.y+buttonRetry.height)
                        if(event.getX() > buttonRetry.x)
                            if(event.getX() < buttonRetry.x+buttonRetry.width)
                                buttonRetry.setState(1);
                if(event.getY() > buttonNext.y)
                    if(event.getY() < buttonNext.y+buttonNext.height)
                        if(event.getX() > buttonNext.x)
                            if(event.getX() < buttonNext.x+buttonNext.width)
                                buttonNext.setState(1);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getY() > buttonStageSelect.y)
                    if(event.getY() < buttonStageSelect.y+buttonStageSelect.height)
                        if(event.getX() > buttonStageSelect.x)
                            if(event.getX() < buttonStageSelect.x+buttonStageSelect.width) {
                                if(!stageSelecting){
                                    stageSelecting = true;
                                    context.startActivity(new Intent(context.getApplicationContext(),StageSelect.class));
                                    ((Activity) context).finish();
                                }
                            }
                if(event.getY() > buttonRetry.y)
                    if(event.getY() < buttonRetry.y+buttonRetry.height)
                        if(event.getX() > buttonRetry.x)
                            if(event.getX() < buttonRetry.x+buttonRetry.width){
                                restarting = true;
                                Intent intent = new Intent(context, StartGame.class);
                                Bundle extras = new Bundle();
                                extras.putInt("stage_number", stage.number);
                                intent.putExtras(extras);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                if(batteryValue==0){
                    if(event.getY() > buttonNext.y)
                        if(event.getY() < buttonNext.y + buttonNext.height)
                            if(event.getX() > buttonNext.x)
                                if(event.getX() < buttonNext.x+buttonNext.width){
                                    if(stage.number==30) {
                                        ((Activity)context).finish();
                                        context.startActivity(new Intent(context, Story2.class));
                                    }
                                    else if(stage.number<30){
                                        restarting = true;
                                        Intent intent = new Intent(context, StartGame.class);
                                        Bundle extras = new Bundle();
                                        extras.putInt("stage_number", stage.number+1);
                                        //extras.putBoolean("story", true);
                                        intent.putExtras(extras);
                                        ((Activity) context).finish();
                                        context.startActivity(intent);
                                    }
                                }
                }
                buttonStageSelect.setState(Sprite.STAND);
                buttonRetry.setState(Sprite.STAND);
                buttonNext.setState(Sprite.STAND);
            }

        }else if(pause){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(event.getY() > buttonStageSelect.y)
                    if(event.getY() < buttonStageSelect.y+buttonStageSelect.height)
                        if(event.getX() > buttonStageSelect.x)
                            if(event.getX() < buttonStageSelect.x+buttonStageSelect.width)
                                buttonStageSelect.setState(1);
                if(event.getY() > buttonRetry.y)
                    if(event.getY() < buttonRetry.y+buttonRetry.height)
                        if(event.getX() > buttonRetry.x)
                            if(event.getX() < buttonRetry.x+buttonRetry.width)
                                buttonRetry.setState(1);
                if(event.getY() > buttonPlay.y)
                    if(event.getY() < buttonPlay.y+buttonPlay.height)
                        if(event.getX() > buttonPlay.x)
                            if(event.getX() < buttonPlay.x+buttonPlay.width)
                                buttonPlay.setState(1);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getY() > buttonStageSelect.y)
                    if(event.getY() < buttonStageSelect.y+buttonStageSelect.height)
                        if(event.getX() > buttonStageSelect.x)
                            if(event.getX() < buttonStageSelect.x+buttonStageSelect.width) {
                                //if(!stageSelecting){
                                    stageSelecting = true;
                                    context.startActivity(new Intent(context.getApplicationContext(),StageSelect.class));
                                    ((Activity) context).finish();
                                //}
                            }
                if(event.getY() > buttonRetry.y)
                    if(event.getY() < buttonRetry.y+buttonRetry.height)
                        if(event.getX() > buttonRetry.x)
                            if(event.getX() < buttonRetry.x+buttonRetry.width){
                                restarting = true;
                                Intent intent = new Intent(context, StartGame.class);
                                Bundle extras = new Bundle();
                                extras.putInt("stage_number", stage.number);
                                intent.putExtras(extras);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
            if(event.getY() > buttonPlay.y)
                if(event.getY() < buttonPlay.y+buttonPlay.height)
                    if(event.getX() > buttonPlay.x)
                        if(event.getX() < buttonPlay.x+buttonPlay.width)
                            pause(false);
                buttonStageSelect.setState(Sprite.STAND);
                buttonRetry.setState(Sprite.STAND);
                buttonPlay.setState(Sprite.STAND);
            }
        }
        return true;
    }

    public int getCol(float x)
    {
        //return (int)Math.floor((x+margin)/celSize);
        return (int)Math.floor(x/celSize);
    }
    public int getRow(float y){
        //return (int)Math.floor((y-spareHeight+margin)/celSize);
        return (int)Math.floor((y-spareHeight)/celSize);
    }
    public List<Sprite> getSpritesList(){
        return spritesList;
    }
    public Sprite createLamp(Context context, String lamp, int x, int y){
        if(lamp.equals("led1"))
            return new Led1(context,x,y, celSize);
        else if(lamp.equals("lamp2"))
            return new Lamp2(context,x,y, celSize);
        else if(lamp.equals("led2"))
            return new Led2(context,x,y, celSize);
        else if(lamp.equals("led3"))
            return new Led3(context,x,y, celSize);
        else if(lamp.equals("led4"))
            return new Led4(context,x,y, celSize);
        return new Lamp1(context,x,y, celSize);
    }
    public class Stage{
        public Bitmap background;
        public int time;
        public int number;
        public int startTime = 60;
        public int scoreTwoStars;
        public int scoreThreeStars;
        public Stage(Context context, int number, int width){

            this.number = number;
            scoreTwoStars = StageScores.scores[number-1][0];
            scoreThreeStars = StageScores.scores[number-1][1];
            if(number>=1 && number <= 24){
                this.background = BitmapFactory.decodeResource(context.getResources(), R.raw.background_stage1);
                song = MediaPlayer.create(context, R.raw.stage1v2);
                intro = MediaPlayer.create(context, R.raw.intro_stage);
                stageFailed = MediaPlayer.create(context, R.raw.stagefailed);
                stageCleared= MediaPlayer.create(context, R.raw.stageclear);
                scaleFactor = ((float)width/(float)this.background.getWidth());
                this.background = Bitmap.createScaledBitmap(this.background, width,(int)(this.background.getHeight()*scaleFactor),false);
                this.startTime = 80;
            }else{
                this.background = BitmapFactory.decodeResource(context.getResources(), R.raw.background_stage2);
                song = MediaPlayer.create(context, R.raw.final_stages);
                intro = MediaPlayer.create(context, R.raw.intro_stage);
                stageFailed = MediaPlayer.create(context, R.raw.stagefailed);
                stageCleared= MediaPlayer.create(context, R.raw.stageclear);
                scaleFactor = ((float)width/(float)this.background.getWidth());
                this.background = Bitmap.createScaledBitmap(this.background, width,(int)(this.background.getHeight()*scaleFactor),false);
                this.startTime = 80;
            }
            song.setLooping(true);
            this.time = startTime;
        }
        public String[] getLamps(){
            if(number>=1 && number<=5){
                return new String[]{"lamp1","lamp2","led1",};
            }else if(number>=6 && number<=24) {
                return new String[]{"lamp1", "lamp2", "led1", "led2"};
            }
            else if(number>=25 && number<=27){
                return new String[]{"lamp1", "lamp2", "led1", "led2", "led3"};
            }
            else{
                return new String[]{"lamp1", "lamp2", "led1", "led2", "led3", "led4"};
            }
            //return new String[]{"led1"};
        }
        public void fillVoidSpaces(){
            int[][] rowsCols = new int[][]{};
            if(number==1){
                rowsCols = new int[][]{

                        new int[]{4,3},new int[]{4,4},new int[]{5,3},new int[]{5,4}
                };
            }
            else if(number==2){
                rowsCols = new int[][]{

                    new int[]{4,3},new int[]{4,4},new int[]{5,3},new int[]{5,4},new int[]{4,2},new int[]{5,5}
                };
            }else if(number==3){
                rowsCols = new int[][]{

                    new int[]{4,3},new int[]{4,4},new int[]{5,3},new int[]{5,4},new int[]{4,2},new int[]{5,5},new int[]{5,2},new int[]{4,5}
                };
            }else if(number==4){
                rowsCols = new int[][]{

                    new int[]{4,3},new int[]{4,4},new int[]{5,3},new int[]{5,4},new int[]{4,2},new int[]{5,5},new int[]{5,2},new int[]{4,5},new int[]{4,1},new int[]{5,6}
                };
            }else if(number==5){
                rowsCols = new int[][]{

                    new int[]{4,3},new int[]{4,4},new int[]{5,3},new int[]{5,4},new int[]{4,2},new int[]{5,5},new int[]{5,2},new int[]{4,5},new int[]{4,1},new int[]{5,6},new int[]{5,1},new int[]{4,6}
                };
            }else if(number==6){
                rowsCols = new int[][]{

                     new int[]{4,3},new int[]{4,4},new int[]{5,3},new int[]{5,4},new int[]{4,2},new int[]{5,5},new int[]{5,2},new int[]{4,5},new int[]{4,1},new int[]{5,6},new int[]{5,1},new int[]{4,6},new int[]{5,0},new int[]{4,7}
                };
            }
            else if(number==7){
                rowsCols = new int[][]{

                        new int[]{4,3},new int[]{4,4},new int[]{5,3},new int[]{5,4},new int[]{4,2},new int[]{5,5},new int[]{5,2},new int[]{4,5},new int[]{4,1},new int[]{5,6},new int[]{5,1},new int[]{4,6},new int[]{5,0},new int[]{4,7},new int[]{4,0},new int[]{5,7}
                };
            }
            else if(number==8){
                rowsCols = new int[][]{
                        new int[]{5,5},new int[]{4,2},new int[]{5,2},new int[]{4,5},new int[]{4,1},new int[]{5,6},new int[]{5,1},new int[]{4,6},new int[]{5,0},new int[]{4,7},new int[]{4,0},new int[]{5,7}
                };
            }
            else if(number==9){
                rowsCols = new int[][]{
                        new int[]{5,2},new int[]{4,5},new int[]{4,1},new int[]{5,6},new int[]{5,1},new int[]{4,6},new int[]{5,0},new int[]{4,7},new int[]{4,0},new int[]{5,7}
                };
            }
            else if(number==10){
                rowsCols = new int[][]{
                        new int[]{4,1},new int[]{5,6},new int[]{5,1},new int[]{4,6},new int[]{5,0},new int[]{4,7},new int[]{4,0},new int[]{5,7}
                };
            }
            else if(number==11){
                rowsCols = new int[][]{
                        new int[]{3,2},new int[]{4,2},new int[]{5,2},new int[]{6,2},new int[]{3,5},new int[]{4,5},new int[]{5,5},new int[]{6,5}
                };
            }
            else if(number==12){
                rowsCols = new int[][]{
                        new int[]{0,2},new int[]{1,2},new int[]{2,2},new int[]{7,2},new int[]{8,2},new int[]{9,2},
                        new int[]{0,5},new int[]{1,5},new int[]{2,5},new int[]{7,5},new int[]{8,5},new int[]{9,5},
                };
            }
            else if(number==13){
                rowsCols = new int[][]{
                        new int[]{0,2},new int[]{1,2},new int[]{2,2},
                        new int[]{0,5},new int[]{1,5},new int[]{2,5},
                        new int[]{3,0},new int[]{3,1},new int[]{3,2},new int[]{3,3},new int[]{3,4},new int[]{3,5},new int[]{3,6},new int[]{3,7}

                };
            }
            else if(number==14){
                rowsCols = new int[][]{
                        new int[]{3,0},new int[]{3,1},new int[]{3,2},new int[]{3,3},new int[]{3,4},new int[]{3,5},new int[]{3,6},new int[]{3,7},
                        new int[]{7,0},new int[]{7,1},new int[]{7,2},new int[]{7,3},new int[]{7,4},new int[]{7,5},new int[]{7,6},new int[]{7,7}

                };
            }
            else if(number==15){
                rowsCols = new int[][]{
                        new int[]{3,0},new int[]{3,1},new int[]{3,6},new int[]{3,7},
                        new int[]{7,0},new int[]{7,1},new int[]{7,6},new int[]{7,7}

                };
            }
            else if(number==16){
                rowsCols = new int[][]{
                        new int[]{3,0},new int[]{3,1},new int[]{3,6},new int[]{3,7},
                        new int[]{3,2},new int[]{4,2},new int[]{5,2},new int[]{6,2},new int[]{7,2},
                        new int[]{3,5},new int[]{4,5},new int[]{5,5},new int[]{6,5},new int[]{7,5},
                        new int[]{7,0},new int[]{7,1},new int[]{7,6},new int[]{7,7}

                };
            }
            else if(number==17){
                rowsCols = new int[][]{
                        new int[]{0,1},new int[]{1,1},new int[]{2,1},new int[]{3,1},new int[]{6,1},new int[]{7,1},new int[]{8,1},new int[]{9,1},
                        new int[]{0,6},new int[]{1,6},new int[]{2,6},new int[]{3,6},new int[]{6,6},new int[]{7,6},new int[]{8,6},new int[]{9,6},
                };
            }
            else if(number==18){
                rowsCols = new int[][]{
                        new int[]{0,1},new int[]{1,1},new int[]{2,1},new int[]{3,1},new int[]{6,1},new int[]{7,1},new int[]{8,1},new int[]{9,1},
                        new int[]{0,6},new int[]{1,6},new int[]{2,6},new int[]{3,6},new int[]{6,6},new int[]{7,6},new int[]{8,6},new int[]{9,6},
                        new int[]{2,2},new int[]{2,3},new int[]{2,4},new int[]{2,5},

                };
            }
            else if(number==19){
                rowsCols = new int[][]{
                        new int[]{0,0},new int[]{0,1},new int[]{0,2},
                        new int[]{1,0},new int[]{1,1},new int[]{1,2},
                        new int[]{2,0},new int[]{2,1},new int[]{2,2},

                        new int[]{7,5},new int[]{7,6},new int[]{7,7},
                        new int[]{8,5},new int[]{8,6},new int[]{8,7},
                        new int[]{9,5},new int[]{9,6},new int[]{9,7},
                };
            }
            else if(number==20){
                rowsCols = new int[][]{
                        new int[]{0,0},new int[]{0,1},new int[]{0,2},new int[]{0,3},new int[]{0,4},new int[]{0,5},new int[]{0,6},new int[]{0,7},
                        new int[]{9,0},new int[]{9,1},new int[]{9,2},new int[]{9,3},new int[]{9,4},new int[]{9,5},new int[]{9,6},new int[]{9,7},
                        new int[]{1,0},new int[]{2,0},new int[]{3,0},new int[]{4,0},new int[]{5,0},new int[]{6,0},new int[]{7,0},new int[]{8,0},
                        new int[]{1,7},new int[]{2,7},new int[]{3,7},new int[]{4,7},new int[]{5,7},new int[]{6,7},new int[]{7,7},new int[]{8,7}
                };
            }
            else if(number==21){
                rowsCols = new int[][]{
                        new int[]{0,0},new int[]{0,1},new int[]{0,2},new int[]{0,3},new int[]{0,4},new int[]{0,5},new int[]{0,6},new int[]{0,7},
                        new int[]{1,1},new int[]{1,2},new int[]{1,3},new int[]{1,4},new int[]{1,5},new int[]{1,6},
                        new int[]{9,0},new int[]{9,1},new int[]{9,2},new int[]{9,3},new int[]{9,4},new int[]{9,5},new int[]{9,6},new int[]{9,7},
                        new int[]{1,0},new int[]{2,0},new int[]{3,0},new int[]{4,0},new int[]{5,0},new int[]{6,0},new int[]{7,0},new int[]{8,0},
                        new int[]{1,7},new int[]{2,7},new int[]{3,7},new int[]{4,7},new int[]{5,7},new int[]{6,7},new int[]{7,7},new int[]{8,7}
                };
            }
            else if(number==22){
                rowsCols = new int[][]{
                        new int[]{0,0},new int[]{0,1},new int[]{0,2},new int[]{0,3},new int[]{0,4},new int[]{0,5},new int[]{0,6},new int[]{0,7},
                        new int[]{1,1},new int[]{1,2},new int[]{1,3},new int[]{1,4},new int[]{1,5},new int[]{1,6},
                        new int[]{9,0},new int[]{9,1},new int[]{9,2},new int[]{9,3},new int[]{9,4},new int[]{9,5},new int[]{9,6},new int[]{9,7},
                        new int[]{8,1},new int[]{8,2},new int[]{8,3},new int[]{8,4},new int[]{8,5},new int[]{8,6},
                        new int[]{1,0},new int[]{2,0},new int[]{3,0},new int[]{4,0},new int[]{5,0},new int[]{6,0},new int[]{7,0},new int[]{8,0},
                        new int[]{1,7},new int[]{2,7},new int[]{3,7},new int[]{4,7},new int[]{5,7},new int[]{6,7},new int[]{7,7},new int[]{8,7}
                };
            }
            else if(number==23){
                rowsCols = new int[][]{
                        new int[]{4,2},new int[]{4,3},new int[]{4,4},new int[]{4,5},
                        new int[]{5,2},new int[]{5,3},new int[]{5,4},new int[]{5,5},
                };
            }
            else if(number==24){
                rowsCols = new int[][]{
                        new int[]{3,2},new int[]{3,3},new int[]{3,4},new int[]{3,5},
                        new int[]{6,3},new int[]{6,4}
                };
            }
            else if(number==25){
                rowsCols = new int[][]{
                        new int[]{2,3},new int[]{2,4},
                        new int[]{7,3},new int[]{7,4}
                };
            }
            else if(number==26){
                rowsCols = new int[][]{
                        new int[]{2,2},new int[]{2,5},
                        new int[]{7,2},new int[]{7,5}
                };
            }
            else if(number==27){
                rowsCols = new int[][]{
                        new int[]{2,1},new int[]{2,6},
                        new int[]{7,1},new int[]{7,6}
                };
            }
            else if(number==28){
                rowsCols = new int[][]{
                        new int[]{2,0},new int[]{2,7}
                };
            }
            else if(number==29){
                rowsCols = new int[][]{
                        new int[]{0,0},new int[]{0,7}
                };
            }
            else if(number==30){
                rowsCols = new int[][]{
                        new int[]{9,0},new int[]{9,7}
                };
            }
            insertLids(rowsCols);
        }
    }
    public void insertLids(int[][] rowsCols){
        for(int i = 0; i < rowsCols.length;i++){
            int row = rowsCols[i][0];
            int col = rowsCols[i][1];
            spritesMatrix[row][col] = new Lid(context, col*celSize+8,(row)*celSize+spareHeight,celSize);
            spritesList.add(spritesMatrix[row][col]);
        }

    }
}
