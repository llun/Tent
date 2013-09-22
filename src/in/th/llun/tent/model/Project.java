package in.th.llun.tent.model;

import in.th.llun.tent.remote.Tent;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

public class Project extends JSONRemoteObject {

	public Project(JSONObject raw) {
		super(raw);
	}

	public String getId() {
		return mRaw.optString("id");
	}

	public String getName() {
		return mRaw.optString("name");
	}

	public String getDescription() {
		return mRaw.optString("description");
	}

	public String getUrl() {
		return mRaw.optString("url");
	}

	public boolean isStarred() {
		return mRaw.optBoolean("starred");
	}

	public Date getUpdatedAt() {
		try {
			return Formatter.dateFromStringWithTimezone(mRaw.optString("updated_at"));
		} catch (ParseException e) {
			Log.e(Tent.LOG_TAG, "Can't parse date format", e);
			return null;
		}
	}

}
