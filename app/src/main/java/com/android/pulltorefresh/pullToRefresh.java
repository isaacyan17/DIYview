package com.android.pulltorefresh;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.diyview.R;

/**
 * Created by jingqiang on 15/8/7.
 */


public class pullToRefresh extends ListView{
    private static final String TAG = "pullToRefresh";
    private View view;
    private int height;
    private int currentHeight;
    TextView text;
    ProgressBar pro;
    ImageView arr;
    private static final int IDLE = 0;
    private static final int pulling = 1;
    private static final int ready_refresh = 2;
    private int mListState=IDLE;
    pulltoRefreshAsyncTask asyncTask;
    private Handler handler = new Handler();
    public void setOnPulltoRefresh( pulltoRefreshAsyncTask asyncTask)
    {
        this.asyncTask=asyncTask;
    }

    public interface pulltoRefreshAsyncTask{
        void preExcute();
        void doInBackground();
        void PostExcute();
    }


    public pullToRefresh(Context context) {
        super(context);
        initView(context);
    }

    public pullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public pullToRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context)
    {
        view=View.inflate(context, R.layout.refresh_header,null);
        arr = (ImageView)view.findViewById(R.id.arrow);
        pro = (ProgressBar)view.findViewById(R.id.progress);
        text = (TextView)view.findViewById(R.id.text);
        view.measure(0,0);
        //getMeasuredHeight();
        height=view.getMeasuredHeight();
        currentHeight=-height;
        addHeaderView(view);
        view.setPadding(0, currentHeight, 0, 0);
    }
    int startY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int position = getFirstVisiblePosition();
        if(position>0){
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN :
                startY= (int) ev.getRawY();
            break;

            case MotionEvent.ACTION_MOVE :
                int endY= (int) ev.getRawY();
                int dy=endY-startY;
                currentHeight+=dy;
                startY=(int) ev.getRawY();
                //Log.d(TAG, currentHeight+"--");

                view.setPadding(0, currentHeight, 0, 0);
                if(currentHeight>0)
                {
                    text.setText("松开即可更新");

                    mListState=ready_refresh;
                }
                else if(currentHeight<=0&&currentHeight>=-height)
                {
                    mListState=pulling;
                    text.setText("下拉更新啊啊啊啊 ");
                }
                else if(currentHeight<height)
                {
                    mListState=IDLE;
                    currentHeight=-height;
                }
            break;

            case MotionEvent.ACTION_UP :
                switch (mListState)
                {
                    case ready_refresh:
                        //可以更新了
                        pro.setVisibility(View.VISIBLE);
                        arr.setVisibility(View.INVISIBLE);
                        view.setPadding(0, 0, 0, 0);
                        text.setText("正在更新中... ");
                        asyncTask.preExcute();
                        if(asyncTask!=null) {
                            new Thread() {
                                @Override
                                public void run() {
                                    asyncTask.doInBackground();
                                    //放在里面是因为线程。
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            currentHeight=-height;
                                            view.setPadding(0, currentHeight, 0, 0);
                                            mListState=IDLE;
                                            asyncTask.PostExcute();
                                            pro.setVisibility(View.INVISIBLE);
                                            arr.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }.start();

//                            currentHeight=-height;
//                            view.setPadding(0, currentHeight, 0, 0);
//                            mListState=IDLE;
//                            asyncTask.PostExcute();
//                            pro.setVisibility(View.INVISIBLE);
//                            arr.setVisibility(View.VISIBLE);

                        }
                        break;
                    case pulling:
                        view.setPadding(0,-height,0,0);
                        mListState=IDLE;
                        break;
                    case IDLE:
                        view.setPadding(0, -height, 0, 0);
                        pro.setVisibility(View.INVISIBLE);
                        arr.setVisibility(View.VISIBLE);
                        break;
                }

            break;
        }
        return super.onTouchEvent(ev);
    }
}
