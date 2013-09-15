package in.th.llun.tent.remote;

import org.json.JSONArray;
import org.json.JSONObject;

public interface RemoteResult {

	void onResponse(JSONObject response) throws Exception;

	void onResponse(JSONArray response) throws Exception;

	void onResponse(String response) throws Exception;

}
