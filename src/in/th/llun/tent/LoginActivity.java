package in.th.llun.tent;

import in.th.llun.tent.model.Account;
import in.th.llun.tent.model.Authorization;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.remote.Tent;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class LoginActivity extends Activity {

	private Tent mTent;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mTent = Tent.getInstance(getApplicationContext());

		final Activity activity = this;

		final View authenticatingView = (View) findViewById(R.id.authenticating);
		final ProgressBar loadingBar = (ProgressBar) findViewById(R.id.webLoadingBar);
		final WebView webView = (android.webkit.WebView) findViewById(R.id.webContent);
		loadingBar.setVisibility(View.VISIBLE);
		loadingBar.setProgress(1);
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(android.webkit.WebView view, int progress) {
				if (progress == 100) {
					loadingBar.setVisibility(View.GONE);
				} else {
					loadingBar.setVisibility(View.VISIBLE);
				}
				loadingBar.setProgress(progress);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(android.webkit.WebView view,
			    String url) {
				if (url.startsWith("tentapp")) {
					authenticatingView.setVisibility(View.VISIBLE);
					mTent.token(url, new BasecampResponse<Authorization>() {

						@Override
						public void onResponse(Authorization response) {
							Log.d(Tent.LOG_TAG, response.rawString());
							List<Account> accounts = response.getAccounts().collection();
							if (accounts.size() > 1) {
								// Show popup for choosing account
							}
							else {
								mTent.saveAccount(accounts.get(0));
							}
							activity.finish();
						}
					});
				} else {
					view.loadUrl(url);
				}
				return false;
			}
		});
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		webView.loadUrl(mTent.authorizationUrl());

		Log.d(Tent.LOG_TAG, "Authorize URL: " + mTent.authorizationUrl());
	}

}
