package com.lightchains.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightchains.app.Scenario.StageScores;

/**
 * Created by charleston on 14/10/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    SharedPreferences prefs;
    public ImageAdapter(Context c) {
        mContext = c;
        prefs = mContext.getSharedPreferences("scores", mContext.MODE_PRIVATE);
    }

    public int getCount() {
        //return mThumbIds.length;
        return 30;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final IconViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.stage_select_icon, null);
            viewHolder = new IconViewHolder(v);
            v.setTag(viewHolder);
            //v.setLayoutParams(new GridView.LayoutParams(90, 150));
            //v.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //v.setPadding(8, 8, 8, 8);
        } else {
            viewHolder = (IconViewHolder) v.getTag();
        }
        //SharedPreferences prefs = mContext.getSharedPreferences("scores", mContext.MODE_PRIVATE);
        int score = prefs.getInt("stage"+(position+1),0);
        int previousScore = prefs.getInt("stage"+position,0);
        viewHolder.background.setImageResource(previousScore==0&&position!=0?mThumbIds[1]:mThumbIds[0]);
        if(score>0){
            viewHolder.star1.setImageResource(android.R.drawable.star_big_on);
        }
        else{
            viewHolder.star1.setImageResource(android.R.drawable.star_big_off);
        }
        if(score> StageScores.scores[position][0]){
            viewHolder.star2.setImageResource(android.R.drawable.star_big_on);
        }else{
            viewHolder.star2.setImageResource(android.R.drawable.star_big_off);
        }
        if(score>StageScores.scores[position][1]){
            viewHolder.star3.setImageResource(android.R.drawable.star_big_on);
        }else{
            viewHolder.star3.setImageResource(android.R.drawable.star_big_off);
        }
        viewHolder.number.setText(position+1+"");
        viewHolder.score.setText(String.format("%05d",score));
        return v;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.icon1_stage_select,
            R.drawable.icon1_blocked_stage_select
    };
    class IconViewHolder {
        public ImageView background;
        public TextView number;
        public TextView score;
        public ImageView star1;
        public ImageView star2;
        public ImageView star3;
        public IconViewHolder(View base) {
            background = (ImageView) base.findViewById(R.id.background);
            number = (TextView) base.findViewById(R.id.number);
            score = (TextView) base.findViewById(R.id.number2);
            star1 = (ImageView) base.findViewById(R.id.star1);
            star2 = (ImageView) base.findViewById(R.id.star2);
            star3 = (ImageView) base.findViewById(R.id.star3);
        }
    }
}
