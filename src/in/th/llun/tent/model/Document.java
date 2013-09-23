package in.th.llun.tent.model;

import in.th.llun.tent.remote.Tent;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

public class Document extends JSONRemoteObject {

	public Document(JSONObject raw) {
		super(raw);
	}

	public String getId() {
		return mRaw.optString("id");
	}

	public String getTitle() {
		return mRaw.optString("title");
	}

	public Date getUpdatedAt() {
		try {
			return Formatter.dateFromStringWithTimezone(mRaw.optString("updated_at"));
		} catch (ParseException e) {
			Log.e(Tent.LOG_TAG, "Can't parse date format", e);
			return null;
		}
	}

	public String getUrl() {
		return mRaw.optString("url");
	}

}
