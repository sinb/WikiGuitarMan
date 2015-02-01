package com.sinb.wikiguitarman;



import java.util.jar.JarEntry;

import android.support.v7.app.ActionBarActivity;
import android.R.integer;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WikiGuitarMan extends ActionBarActivity {

	private WebView myWebview;
	private int url_id;
	private int last_url_id = -1;
	private String[] wikiurls;
	//for menu bar
	final int GET_ANOTHER = 0X111;
	final int LAST_LINK = 0x112;
	final int SAVE_LINK = 0x113;
	final int EXIT_APP = 0x114;
	final int NO_IMAGE = 0x115;
	//
	private SharedPreferences mPrefs;
	java.util.Vector<Integer> last_id;
	java.util.Vector<Integer> saved_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wiki_guitar_man);
	    // 显示 icon
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	    getSupportActionBar().setLogo(R.drawable.ic_launcher);
	    getSupportActionBar().setDisplayUseLogoEnabled(true);
	    //
		Resources res = getResources();
		wikiurls = res.getStringArray(R.array.wikiurls);
		myWebview = (WebView)findViewById(R.id.webView1);
		url_id = getRandomUrlId();
		myWebview.loadUrl(wikiurls[url_id]);
		this.myWebview.setWebViewClient(new WebViewClient(){

		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url){
		      view.loadUrl(url);
		      return true;
		    }
		});
		//用来记录一些preference
		last_id = new java.util.Vector<Integer>();
		saved_id = new java.util.Vector<Integer>();
		mPrefs = getSharedPreferences("save_id", MODE_PRIVATE);
		
		//
	}
	public int getRandomUrlId(){
		return (int)( Math.random() * wikiurls.length );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.wiki_guitar_man, menu);
		menu.add(0, GET_ANOTHER, 0, "再来一发!");
		menu.add(0, LAST_LINK, 0, "上一个");
		menu.add(0, NO_IMAGE, 0, "显示图片?");
//		menu.add(0, SAVE_LINK, 0, "收藏");
//		SubMenu save_list = menu.addSubMenu("收藏列表");
		menu.add(0, EXIT_APP, 0, "关闭");
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == GET_ANOTHER) {
			last_url_id = url_id;
			last_id.add(last_url_id);
			url_id = getRandomUrlId();
			myWebview.loadUrl(wikiurls[url_id]);
			return true;
		}
		if (id == LAST_LINK) {
			if(last_id.size() == 0){
				Toast.makeText(this, "没有上一个了", Toast.LENGTH_SHORT).show();
				return true;
			}
			url_id = last_id.get(last_id.size()-1);
			last_id.remove(last_id.size()-1);
			myWebview.loadUrl(wikiurls[url_id]);
			return true;
		}
		if(id == EXIT_APP){
			System.exit(0);
			return true;
		}
		if(id == NO_IMAGE){
			if(myWebview.getSettings().getBlockNetworkImage() == true){
				myWebview.getSettings().setBlockNetworkImage(false);
				Toast.makeText(this, "已经打开图片显示", Toast.LENGTH_SHORT).show();
			}
			else{
				myWebview.getSettings().setBlockNetworkImage(true);
				Toast.makeText(this, "已经关闭图片显示", Toast.LENGTH_SHORT).show();	
			}
		}
//		if(id == SAVE_LINK){
//			SharedPreferences.Editor mPrefsEditor = mPrefs.edit();
//			mPrefsEditor.putInt("url_id", saved_id.get(saved_id.size()));
//			mPrefsEditor.commit();
//		}
//		
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebview.canGoBack()) {
	    	myWebview.goBack();
	    	myWebview.getSettings().setBuiltInZoomControls(true);
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}	
}
