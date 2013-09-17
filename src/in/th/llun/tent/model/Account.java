package in.th.llun.tent.model;

import org.json.JSONObject;

public class Account extends JSONRemoteObject {

	public Account(JSONObject raw) {
		super(raw);
	}

	public String getId() {
		return mRaw.optString("id");
	}

	public String getProduct() {
		return mRaw.optString("production");
	}

	public String getName() {
		return mRaw.optString("name");
	}

	public String getAPIEndpoint() {
		return mRaw.optString("href");
	}

}
