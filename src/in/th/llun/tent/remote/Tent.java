package in.th.llun.tent.remote;

import in.th.llun.tent.Settings;
import in.th.llun.tent.model.Account;
import in.th.llun.tent.model.Authorization;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Document;
import in.th.llun.tent.model.Event;
import in.th.llun.tent.model.Formatter;
import in.th.llun.tent.model.People;
import in.th.llun.tent.model.Project;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.model.Topic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

public class Tent {

	public static final String LOG_TAG = "Tent";

	private static final String BASECAMP_AUTHORIZE_REDIRECT_URL = "tentapp://basecamp/login/auth";

	private static final String TENT_AUTHENTICATION_STORE = "tent.authentication";
	private static final String TENT_STORE_KEY_RAW = "raw";
	private static final String TENT_STORE_KEY_ACCOUNT = "account";
	private static final String TENT_STORE_KEY_EXPIRES = "expires";

	private static Tent sTent;

	private Settings mSettings;
	private Context mContext;
	private OAuthService mService;
	private ExecutorService mExecutor;

	private Token mToken = null;
	private Date mTokenExpires = null;
	private Account mAccount = null;

	public static Tent getInstance(Context context) {
		if (sTent == null) {
			synchronized (Tent.class) {
				if (sTent == null) {
					sTent = new Tent(context, Settings.getInstance(context));
				}
			}
		}

		if (sTent.mContext != context) {
			sTent.mContext = context;
		}

		return sTent;
	}

	public static Tent getInstance() {
		return sTent;
	}

	public Tent(Context context, Settings settings) {
		mContext = context;
		mSettings = settings;
		mExecutor = Executors.newFixedThreadPool(10);
		mService = new ServiceBuilder().provider(BasecampApi.class)
		    .apiKey(mSettings.getID()).apiSecret(mSettings.getSecret())
		    .callback(BASECAMP_AUTHORIZE_REDIRECT_URL).build();

		SharedPreferences preference = context.getSharedPreferences(
		    TENT_AUTHENTICATION_STORE, Context.MODE_PRIVATE);
		String rawToken = preference.getString(TENT_STORE_KEY_RAW, null);
		if (rawToken != null) {
			long expiresTimestamp = preference.getLong(TENT_STORE_KEY_EXPIRES, 0);
			if (new Date().getTime() > expiresTimestamp) {
				Editor editor = preference.edit();
				editor.clear();
				editor.commit();
			} else {
				String rawAccount = preference.getString(TENT_STORE_KEY_ACCOUNT, null);
				try {
					mAccount = new Account(new JSONObject(rawAccount));
					mToken = new TokenExtractor20Impl().extract(rawToken);
					mTokenExpires = new Date(expiresTimestamp);
				} catch (Exception e) {
					Log.e(LOG_TAG, "Can't restore account", e);
				}
			}
		}
	}

	public boolean isLoggedIn() {
		if (mTokenExpires != null && new Date().getTime() > mTokenExpires.getTime()) {
			SharedPreferences preference = mContext.getSharedPreferences(
			    TENT_AUTHENTICATION_STORE, Context.MODE_PRIVATE);
			Editor editor = preference.edit();
			editor.clear();
			editor.commit();
		} else if (mToken != null) {
			return true;
		}
		return false;
	}

	public void saveAccount(Account account) {
		mAccount = account;
		SharedPreferences preference = mContext.getSharedPreferences(
		    TENT_AUTHENTICATION_STORE, Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(TENT_STORE_KEY_ACCOUNT, account.rawString());
		editor.commit();
	}

	public String authorizationUrl() {
		return mService.getAuthorizationUrl(null);
	}

	public void token(String url, final BasecampResponse<Authorization> response) {
		Log.v(LOG_TAG, "Access Token: " + url);
		mToken = new TokenExtractor20Impl().extract(url);
		loadAccounts(new BasecampResponse<Authorization>() {

			@Override
			public void onResponse(Authorization authorization) {
				mTokenExpires = authorization.getExpiresAt();

				SharedPreferences preference = mContext.getSharedPreferences(
				    TENT_AUTHENTICATION_STORE, Context.MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.putString(TENT_STORE_KEY_RAW, mToken.getRawResponse());
				editor.putLong(TENT_STORE_KEY_EXPIRES, authorization.getExpiresAt()
				    .getTime());
				editor.commit();

				if (response != null) {
					response.onResponse(authorization);
				}
			}

		});
	}

	public void loadAccounts(final BasecampResponse<Authorization> response) {
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

	public void me(final BasecampResponse<People> response) {

		invoke(getEndpoint("people/me"), Verb.GET, null, new BaseRemoteResult() {

			public void onResponse(JSONObject object) {
				if (response != null) {
					response.onResponse(new People(object));
				}
			}

		});
	}

	public void loadEvents(Date since, int page,
	    final BasecampResponse<RemoteCollection<Event>> response) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("page", Integer.toString(page));
		if (since != null) {
			parameters.put("since", Formatter.stringFromDateWithTimezone(since));
		}

		invoke(getEndpoint("events"), Verb.GET, parameters, new BaseRemoteResult() {

			public void onResponse(JSONArray array) {
				ArrayList<Event> events = new ArrayList<Event>(array.length());
				for (int i = 0; i < array.length(); i++) {
					JSONObject raw = array.optJSONObject(i);
					events.add(new Event(raw));
				}
				response.onResponse(new RemoteCollection<Event>(events));
			}

		});
	}

	public void loadEvents(
	    final BasecampResponse<RemoteCollection<Event>> response) {
		loadEvents(null, 1, response);
	}

	public void loadProjectEvents(Project project,
	    final BasecampResponse<RemoteCollection<Event>> response) {

		invoke(getEndpoint("projects/" + project.getId() + "/events"), Verb.GET,
		    null, new BaseRemoteResult() {

			    public void onResponse(JSONArray array) {
				    ArrayList<Event> events = new ArrayList<Event>(array.length());
				    for (int i = 0; i < array.length(); i++) {
					    JSONObject raw = array.optJSONObject(i);
					    events.add(new Event(raw));
				    }
				    response.onResponse(new RemoteCollection<Event>(events));
			    }

		    });

	}

	public void loadProjects(
	    final BasecampResponse<RemoteCollection<Project>> response) {

		invoke(getEndpoint("projects"), Verb.GET, null, new BaseRemoteResult() {

			public void onResponse(JSONArray array) {
				if (response != null) {
					ArrayList<Project> projects = new ArrayList<Project>(array.length());
					for (int i = 0; i < array.length(); i++) {
						JSONObject raw = array.optJSONObject(i);
						projects.add(new Project(raw));
					}
					response.onResponse(new RemoteCollection<Project>(projects));
				}
			}

		});

	}

	public void loadProjectDocuments(
	    final BasecampResponse<RemoteCollection<Document>> response) {
		loadProjectDocuments(null, response);
	}

	public void loadProjectDocuments(Project project,
	    final BasecampResponse<RemoteCollection<Document>> response) {

		String endpoint = "documents";
		if (project != null) {
			endpoint = "projects/" + project.getId() + "/documents";
		}

		invoke(getEndpoint(endpoint), Verb.GET, null, new BaseRemoteResult() {

			public void onResponse(JSONArray array) {
				if (response != null) {
					ArrayList<Document> documents = new ArrayList<Document>(array
					    .length());
					for (int i = 0; i < array.length(); i++) {
						JSONObject raw = array.optJSONObject(i);
						documents.add(new Document(raw));
					}
					response.onResponse(new RemoteCollection<Document>(documents));
				}
			}

		});

	}

	public void getDocument(String documentUrl,
	    final BasecampResponse<Document> response) {

		invoke(documentUrl, Verb.GET, null, new BaseRemoteResult() {

			public void onResponse(JSONObject object) {

				if (response != null) {
					response.onResponse(new Document(object));
				}

			}

		});

	}

	public void loadTopics(Project project,
	    final BasecampResponse<RemoteCollection<Topic>> response) {
		loadTopics(project, 0, response);
	}

	public void loadTopics(Project project, int page,
	    final BasecampResponse<RemoteCollection<Topic>> response) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (page > 1) {
			parameters.put("page", Integer.toString(page));
		}

		String endpoint = "topics";
		if (project != null) {
			endpoint = "projects/" + project.getId() + "/topics";
		}

		invoke(getEndpoint(endpoint), Verb.GET, parameters, new BaseRemoteResult() {

			public void onResponse(JSONArray array) {
				if (response != null) {
					ArrayList<Topic> topics = new ArrayList<Topic>(array.length());
					for (int i = 0; i < array.length(); i++) {
						JSONObject raw = array.optJSONObject(i);
						topics.add(new Topic(raw));
					}
					response.onResponse(new RemoteCollection<Topic>(topics));
				}
			}
		});
	}

	private String getEndpoint(String model) {
		return String.format("%s/%s.json", mAccount.getAPIEndpoint(), model);
	}

	private void invoke(final String url, Verb verb,
	    Map<String, String> parameters, final RemoteResult result) {

		Log.v(LOG_TAG, "invoke url: " + url);
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
