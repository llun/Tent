package in.th.llun.tent.model;

import in.th.llun.tent.remote.Tent;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONObject;

import android.util.Log;

public class Topic extends JSONRemoteObject {

	public Topic(JSONObject raw) {
	  super(raw);
  }
	
	public String getId() {
		return mRaw.optString("id");
	}
	
	public String getTitle() {
		return mRaw.optString("title");
	}
	
	public String getExcerpt() {
		return mRaw.optString("excerpt");
	}
	
	public People getLastUpdater() {
		return new People(mRaw.optJSONObject("last_updater"));
	}
	
	public String getTopicId() {
		return getTopicable().optString("id");
	}
	
	public String getType() {
		return getTopicable().optString("type");
	}
	
	public String getUrl() {
		return getTopicable().optString("url");
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
	
	public int getNumberOfAttachment() {
		return mRaw.optInt("attachments");
	}
	
	private JSONObject getTopicable() {
		return mRaw.optJSONObject("topicable");
	}

}
