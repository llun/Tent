package in.th.llun.tent.model;

import org.json.JSONObject;

public class People extends JSONRemoteObject {

	public People(JSONObject raw) {
		super(raw);
	}

	public String getId() {
		return mRaw.optString("id");
	}

	public String getIdentityId() {
		return mRaw.optString("identity_id");
	}

	public String getAvatarUrl() {
		return mRaw.optString("avatar_url");
	}

	public String getName() {
		return mRaw.optString("name");
	}

}
