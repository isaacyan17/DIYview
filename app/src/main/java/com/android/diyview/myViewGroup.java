package com.android.diyview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by jingqiang on 15/8/7.
 */
public class myViewGroup extends LinearLayout{
    public myViewGroup(Context context) {
        super(context);
    }

    public myViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public void initView(Context context)
    {
        this.setOrientation(LinearLayout.HORIZONTAL);
        for(int i =0;i<4;i++)
        {
            ImageView ig = new ImageView(context);
            ig.setImageResource(R.mipmap.presence_invisible);
            this.addView(ig);
        }
    }
    public void stateChange(int position)
    {
        for(int i =0;i<this.getChildCount();i++)
        {
            ImageView ig = (ImageView) this.getChildAt(i);
            if(position==i)
                ig.setImageResource(R.mipmap.presence_online);
            else
                ig.setImageResource(R.mipmap.presence_invisible);
        }
    }
}
