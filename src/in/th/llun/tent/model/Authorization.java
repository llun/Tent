package in.th.llun.tent.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

public class Authorization extends JSONRemoteObject {

	public Authorization(JSONObject object) {
		super(object);
	}

	public Date getExpiresAt() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
		    Locale.getDefault());
		try {
			return format.parse(mRaw.optString("expires_at"));
		} catch (ParseException e) {
			return null;
		}
	}
}
