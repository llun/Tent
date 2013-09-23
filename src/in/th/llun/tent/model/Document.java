package in.th.llun.tent.model;

import in.th.llun.tent.remote.Tent;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
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

	public Date getCreatedAt() {
		try {
			return Formatter.dateFromStringWithTimezone(mRaw.optString("created_at"));
		} catch (ParseException e) {
			Log.e(Tent.LOG_TAG, "Can't parse date format", e);
			return null;
		}
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

	public String getContent() {
		return mRaw.optString("content");
	}

	public People getLastUpdater() {
		return new People(mRaw.optJSONObject("last_updater"));
	}

	public List<People> getSubscribers() {
		List<People> peoples = new ArrayList<People>();

		JSONArray rawSubscribers = mRaw.optJSONArray("subscribers");
		for (int i = 0; i < rawSubscribers.length(); i++) {
			peoples.add(new People(rawSubscribers.optJSONObject(i)));
		}

		return peoples;
	}

}
