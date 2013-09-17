package in.th.llun.tent.model;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONObject;

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
			return Formatter.dateFromStringWithTimezone("updated_at");
		} catch (ParseException e) {
			return null;
		}
	}

	public Date getCreatedAt() {
		try {
			return Formatter.dateFromStringWithTimezone("created_at");
		} catch (ParseException e) {
			return null;
		}
	}

	public Creator getCreator() {
		return new Creator(mRaw.optJSONObject("creator"));
	}

}
