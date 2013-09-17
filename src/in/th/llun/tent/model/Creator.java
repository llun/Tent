package in.th.llun.tent.model;

import org.json.JSONObject;

public class Creator extends JSONRemoteObject {

	public Creator(JSONObject raw) {
	  super(raw);
  }
	
	public String getId() {
		return mRaw.optString("id");
	}
	
	public String getName() {
		return mRaw.optString("name");
	}
	
	public String getAvatarUrl() {
		return mRaw.optString("avatar_url");
	}

}
