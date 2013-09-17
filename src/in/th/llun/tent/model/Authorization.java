package in.th.llun.tent.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class Authorization extends JSONRemoteObject {

	public Authorization(JSONObject object) {
		super(object);
	}

	public Date getExpiresAt() {
		try {
			return Formatter.dateFromString(mRaw.optString("expires_at"));
		} catch (ParseException e) {
			return null;
		}
	}

	public RemoteCollection<Account> getAccounts() {
		JSONArray array = mRaw.optJSONArray("accounts");
		ArrayList<Account> accounts = new ArrayList<Account>(array.length());

		for (int i = 0; i < array.length(); i++) {
			JSONObject raw = array.optJSONObject(i);
			Account account = new Account(raw);
			accounts.add(account);
		}

		return new RemoteCollection<Account>(accounts);
	}
}
