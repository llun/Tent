package in.th.llun.tent.model;

import in.th.llun.tent.remote.Tent;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

public class Event extends JSONRemoteObject {

	public Event(JSONObject raw) {
		super(raw);
	}

	public String getId() {
		return mRaw.optString("id");
	}

	public String getSummary() {
		return mRaw.optString("summary");
	}

	public String getTarget() {
		return mRaw.optString("target");
	}

	public String getAction() {
		return mRaw.optString("action");
	}

	public String getExcept() {
		return mRaw.optString("excerpt");
	}

	public String getUrl() {
		return mRaw.optString("url");
	}

	public String getHtmlUrl() {
		return mRaw.optString("html_url");
	}

	public Date getUpdatedAt() {
		try {
			return Formatter.dateFromStringWithTimezone(mRaw.optString("updated_at"));
		} catch (ParseException e) {
			Log.e(Tent.LOG_TAG, "Can't parse date format", e);
			return null;
		}
	}

	public Date getCreatedAt() {
		try {
			return Formatter.dateFromStringWithTimezone(mRaw.optString("created_at"));
		} catch (ParseException e) {
			Log.e(Tent.LOG_TAG, "Can't parse date format", e);
			return null;
		}
	}

	public People getCreator() {
		return new People(mRaw.optJSONObject("creator"));
	}

}
