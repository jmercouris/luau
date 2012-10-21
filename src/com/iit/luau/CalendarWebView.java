package com.iit.luau;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CalendarWebView {

	public static void getAlert(Context c, String inputURL, String title) {
		AlertDialog.Builder alert = new AlertDialog.Builder(c);

		alert.setTitle("Events for:" + title);
		WebView wv = new WebView(c);
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
		alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});

		alert.show();

	}
}
