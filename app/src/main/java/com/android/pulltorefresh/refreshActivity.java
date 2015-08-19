package com.android.pulltorefresh;

import android.app.Activity;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.diyview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class refreshActivity extends Activity {

    private static final String TAG = "refreshActivity";
    private pullToRefresh ptr;
    List<String> newsTitle;
    mAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
       // Log.d(TAG,"112314124");
        ptr=(pullToRefresh)findViewById(R.id.ptr);
        newsTitle = new ArrayList<String>();
        newsTitle.add("这是原始数据+1");
        newsTitle.add("这是原始数据+2");
        newsTitle.add("这是原始数据+3");
        newsTitle.add("这是原始数据+4");
        newsTitle.add("这是原始数据+5");
        newsTitle.add("这是原始数据+6");
        newsTitle.add("这是原始数据+7");

        newsTitle.add("这是原始数据+8");
        newsTitle.add("这是原始数据+9");
        newsTitle.add("这是原始数据+10");
        newsTitle.add("这是原始数据+11");
        newsTitle.add("这是原始数据+12");
        newsTitle.add("这是原始数据+13");
        newsTitle.add("这是原始数据+14");
        adapter= new mAdapter();
        ptr.setAdapter(adapter);
        ptr.setOnPulltoRefresh(new pullToRefresh.pulltoRefreshAsyncTask() {
            @Override
            public void preExcute() {
                Toast.makeText(refreshActivity.this,"正在拼命加载中...",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void doInBackground() {
                SystemClock.sleep(3000);
                Random random = new Random();
                newsTitle.add(0, "这是新数据" + random.nextInt(2000));
            }

            @Override
            public void PostExcute() {
                adapter.notifyDataSetChanged();
            }
        });

    }
    private class mAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return newsTitle.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(getApplicationContext());
            tv.setText(newsTitle.get(position));
            tv.setPadding(20, 20, 20, 20);
            return tv;
        }
    }
}
