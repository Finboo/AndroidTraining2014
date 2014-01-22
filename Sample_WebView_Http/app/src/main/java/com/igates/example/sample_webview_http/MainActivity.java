package com.igates.example.sample_webview_http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private static final String cTextViewTimerMessage = "timer";
	protected RelativeLayout mMainContentView;
	protected WebView mAdvWebview = null;
	protected TextView countDown;
	protected boolean mAdvClicked;
	protected boolean mIsShowingAdv;
	protected int timeOfCommercial = 10;

	public Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			Bundle b = msg.getData();
			String a = b.getString(cTextViewTimerMessage);
			countDown.setText("forward to application in " + a + " Seconds");
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		initAdvWebView();
		new Thread()
		{
			public void run()
			{
				for(int i = timeOfCommercial; i > 0; i--)
				{
					try
					{
						sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					Message m = new Message();
					Bundle b = new Bundle();
					b.putString(cTextViewTimerMessage, "" + i);
					m.setData(b);
					mHandler.sendMessage(m);
				}
			};
		}.start();
		super.onCreate(savedInstanceState);
	}

	private void initAdvWebView()
	{

		setContentView(R.layout.activity_main);
		copyAssets();
		
		mMainContentView = (RelativeLayout) findViewById(R.id.main_layout);
		countDown = new TextView(this);
		countDown.setTextSize(21);

		mAdvWebview = new WebView(this);

		WebSettings settings = mAdvWebview.getSettings();
		settings.setDefaultTextEncodingName("utf-8");

		// in order to make sure javascript performs after refresh or even
		// change of orientation we must cleanup cache
		mAdvWebview.clearCache(true);

		// support for Hebrew
		mAdvWebview.getSettings().setFixedFontFamily("DroidSansHebrew.ttf");

		mAdvWebview.getSettings().setBuiltInZoomControls(true);
		mAdvWebview.getSettings().setLoadWithOverviewMode(true);
		mAdvWebview.getSettings().setUseWideViewPort(true);
		mAdvWebview.getSettings().setJavaScriptEnabled(true);
		mAdvWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mAdvWebview.getSettings().setSupportMultipleWindows(true);

		mAdvWebview.getSettings().setDomStorageEnabled(true);

		mAdvWebview.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				mAdvClicked = true;
				return false;
			}
		});

		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.CENTER_VERTICAL);

		mMainContentView.addView(mAdvWebview, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		mMainContentView.addView(countDown, rp);
		countDown.setFocusable(true);
		
		// for showing ad
		mAdvWebview.setWebViewClient(new MyAddWebViewClient());
		mAdvWebview.loadUrl("file:///sdcard/adv.html");
		mIsShowingAdv = true;
		setLayoutAnim_slidedownfromtop(mMainContentView, this);
	}

	public void setLayoutAnim_slidedownfromtop(ViewGroup panel, Context ctx)
	{
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(100);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(1000);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
		panel.setLayoutAnimation(controller);
	}

	public void removeAdv()
	{
		mIsShowingAdv = false;
		mMainContentView.removeAllViews();

		Intent in = new Intent();
		in.setClassName(this, YourRealActivity.class.getName());
		this.startActivity(in);
		finish();
	}

	private void copyAssets()
	{
		AssetManager assetManager = getAssets();
		String filename = "iGates.htm";
		InputStream in = null;
		OutputStream out = null;
		try
		{
			in = assetManager.open(filename);
			File outFile = new File("/sdcard/", filename);
			out = new FileOutputStream(outFile);
			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		}
		catch (IOException e)
		{
			Log.e("tag", "Failed to copy asset file: " + filename, e);
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, read);
		}
	}

	private class MyAddWebViewClient extends WebViewClient
	{
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
		{
			super.onReceivedSslError(view, handler, error);
			removeAdv();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			super.onReceivedError(view, errorCode, description, failingUrl);
			removeAdv();
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			mHandler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					if (mIsShowingAdv)
					{
						removeAdv();
					}
				}
			}, timeOfCommercial * 1000);
		}
	}

}