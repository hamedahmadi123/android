package com.irccloud.android;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MainActivity extends BaseActivity implements BuffersListFragment.OnBufferSelectedListener {
	NetworkConnection conn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.logo));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onResume() {
    	super.onResume();
    	
    	conn = NetworkConnection.getInstance();
    	conn.addHandler(mHandler);
    }
    
    @Override
    public void onPause() {
    	super.onPause();

    	if(conn != null) {
        	conn.removeHandler(mHandler);
    	}
    }
    
	static private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NetworkConnection.EVENT_USERINFO:
				Log.i("IRCCloud", "User info updated!  Hello, " + ((NetworkConnection.UserInfo)msg.obj).name);
				break;
			default:
				break;
			}
		}
	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_add:
        	AddChannelFragment newFragment = new AddChannelFragment();
            newFragment.show(getSupportFragmentManager(), "dialog");
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
	@Override
	public void onBufferSelected(int cid, int bid, String name, long last_seen_eid, long min_eid, String type, int joined, int archived) {
		Intent i = new Intent(this, MessageActivity.class);
		i.putExtra("cid", cid);
		i.putExtra("bid", bid);
		i.putExtra("name", name);
		i.putExtra("last_seen_eid", last_seen_eid);
		i.putExtra("min_eid", min_eid);
		i.putExtra("type", type);
		i.putExtra("joined", joined);
		i.putExtra("archived", archived);
		startActivity(i);
	}
}
