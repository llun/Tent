package in.th.llun.tent.remote;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseRemoteResult implements RemoteResult {

	public void onResponse(JSONObject response) throws Exception {
		throw new Exception("Not support this type of response");
	}

	public void onResponse(JSONArray response) throws Exception {
		throw new Exception("Not support this type of response");
	}

	public void onResponse(String response) throws Exception {
		throw new Exception("Not support this type of response");
	}

}