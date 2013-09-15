package in.th.llun.tent;

import in.th.llun.tent.remote.Tent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

public class Settings {

	public static final String KEY_BASECAMP_ID = "th.llun.tent.basecamp.id";
	public static final String KEY_BASECAMP_SECRET = "th.llun.tent.basecamp.secret";

	private static Settings sInstance;

	private Context mContext;

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

	private Settings(Context context) {
		mContext = context;
	}

	public String getID() {
		return getBundle().getString(KEY_BASECAMP_ID);
	}

	public String getSecret() {
		return getBundle().getString(KEY_BASECAMP_SECRET);
	}

	private Bundle getBundle() {
		try {
			Bundle bundle = mContext.getPackageManager().getApplicationInfo(
			    mContext.getPackageName(), PackageManager.GET_META_DATA).metaData;
			return bundle;
		} catch (NameNotFoundException e) {
			Log.d(Tent.LOG_TAG, "Cannot get application meta data", e);
			return null;
		}

	}
}
