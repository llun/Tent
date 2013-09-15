package in.th.llun.tent.model;

import org.json.JSONObject;

public abstract class JSONRemoteObject implements RemoteObject {

	protected JSONObject mRaw;

	public JSONRemoteObject(JSONObject raw) {
		mRaw = raw;
	}

	@Override
	public String rawString() {
		return mRaw.toString();
	}

	public String toString() {
		return rawString();
	}

}