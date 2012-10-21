package com.iit.luau;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Layout;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CalendarWebView {

	public static void getAlert(final Context c, String inputURL, final String title) {
		AlertDialog.Builder alert = new AlertDialog.Builder(c);
		
		alert.setTitle("Events for:" + title);
		final WebView wv = new WebView(c);
		wv.getSettings().setJavaScriptEnabled(true);

		wv.loadUrl(inputURL);

		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);

				return true;
			}
		});

		alert.setView(wv);
		alert.setNegativeButton("Close", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
			}
		});
		
		alert.setNeutralButton("Chat", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id)
			{
				getAlert(c, "http://client02.chat.mibbit.com", "Chat");
			}
		});

		alert.show();
	}
}
