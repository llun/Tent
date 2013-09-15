package in.th.llun.tent.remote;

import in.th.llun.tent.Settings;
import in.th.llun.tent.model.Authorization;
import in.th.llun.tent.model.BasecampResponse;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.extractors.TokenExtractor20Impl;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class Tent {

	public static final String LOG_TAG = "Tent";

	private static final String BASECAMP_AUTHORIZE_REDIRECT_URL = "tentapp://basecamp/login/auth";

	private static Tent sTent;
	private Settings mSettings;

	private Context mContext;
	private OAuthService mService;
	private ExecutorService mExecutor;

	private Token mToken;

	public static Tent getInstance(Context context) {
		if (sTent == null) {
			synchronized (Tent.class) {
				if (sTent == null) {
					sTent = new Tent(context, Settings.getInstance(context));
				}
			}
		}

		return sTent;
	}

	public Tent(Context context, Settings settings) {
		mContext = context;
		mSettings = settings;
		mExecutor = Executors.newSingleThreadExecutor();
		mService = new ServiceBuilder().provider(BasecampApi.class)
		    .apiKey(mSettings.getID()).apiSecret(mSettings.getSecret())
		    .callback(BASECAMP_AUTHORIZE_REDIRECT_URL).build();
	}

	public boolean isLoggedIn() {
		return false;
	}

	public String authorizationUrl() {
		return mService.getAuthorizationUrl(null);
	}

	public void token(String url, final BasecampResponse<Authorization> response) {
		Log.d(LOG_TAG, "Access Token: " + url);
		mToken = new TokenExtractor20Impl().extract(url);
		invoke("https://launchpad.37signals.com/authorization.json", Verb.GET,
		    null, new BaseRemoteResult() {

			    @Override
			    public void onResponse(JSONObject object) {
				    if (response != null) {
					    response.onResponse(new Authorization(object));
				    }
			    }

		    });

	}

	public void loadEvents(Date since, int page) {

	}

	private void invoke(final String url, Verb verb,
	    Map<String, String> parameters, final RemoteResult result) {

		final OAuthRequest request = new OAuthRequest(verb, url);

		if (parameters != null) {
			switch (verb) {
			case POST:
			case PUT:
				for (String key : parameters.keySet()) {
					request.addBodyParameter(key, parameters.get(key));
				}
				break;
			// Default is get
			default:
				for (String key : parameters.keySet()) {
					request.addQuerystringParameter(key, parameters.get(key));
				}
			}
		}

		mExecutor.submit(new Runnable() {

			@Override
			public void run() {
				mService.signRequest(mToken, request);
				Response response = request.send();
				final String body = response.getBody();
				Log.v(LOG_TAG, body);

				if (mContext != null) {
					Handler handler = new Handler(mContext.getMainLooper());
					handler.post(new Runnable() {

						@Override
						public void run() {
							try {
								JSONObject json = new JSONObject(body);
								result.onResponse(json);
							} catch (JSONException je) {
								try {
									JSONArray json = new JSONArray(body);
									result.onResponse(json);
								} catch (JSONException je2) {
									try {
										result.onResponse(body);
									} catch (Exception e) {
										Log.e(LOG_TAG, e.getMessage(), e);
									}
								} catch (Exception e) {
									Log.e(LOG_TAG, e.getMessage(), e);
								}
							} catch (Exception e) {
								Log.e(LOG_TAG, e.getMessage(), e);
							}

						}
					});
				}

			}
		});

	}

}
