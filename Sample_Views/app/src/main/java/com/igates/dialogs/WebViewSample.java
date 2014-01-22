package com.igates.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewSample extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view_sample);
		
		
		WebView myWeb = (WebView) findViewById(R.id.myWebView);
		myWeb.loadUrl("http://ynet.co.il");
	}
}
