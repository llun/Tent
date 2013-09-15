package in.th.llun.tent;

import in.th.llun.tent.remote.Tent;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

public class Settings {

	public static final String KEY_BASECAMP_ID = "th.llun.tent.basecamp.id";
	public static final String KEY_BASECAMP_SECRET = "th.llun.tent.basecamp.secret";

	private static Settings sInstance;

	private Map<String, Object> mProperties;

	public static Settings getInstance(Context context) {
		if (sInstance == null) {
			synchronized (Settings.class) {
				if (sInstance == null) {
					sInstance = new Settings(context);
				}
			}
		}

		return sInstance;
	}

	public Settings(Context context) {
		mProperties = new HashMap<String, Object>();
		try {
			Bundle bundle = context.getPackageManager().getApplicationInfo(
			    context.getPackageName(), PackageManager.GET_META_DATA).metaData;
			if (bundle.containsKey(KEY_BASECAMP_ID)) {
				mProperties.put(KEY_BASECAMP_ID, bundle.getString(KEY_BASECAMP_ID));
			}

			if (bundle.containsKey(KEY_BASECAMP_SECRET)) {
				mProperties.put(KEY_BASECAMP_SECRET,
				    bundle.getString(KEY_BASECAMP_SECRET));
			}

		} catch (NameNotFoundException e) {
			Log.d(Tent.LOG_TAG, "Cannot get application meta data", e);
		}
	}

	public Settings(Map<String, Object> properties) {
		mProperties = properties;
	}

	public String getID() {
		if (mProperties.containsKey(KEY_BASECAMP_ID))
			return (String) mProperties.get(KEY_BASECAMP_ID);
		return null;
	}

	public String getSecret() {
		if (mProperties.containsKey(KEY_BASECAMP_SECRET))
			return (String) mProperties.get(KEY_BASECAMP_SECRET);
		return null;
	}

}
