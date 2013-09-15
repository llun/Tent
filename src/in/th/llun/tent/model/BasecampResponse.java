package in.th.llun.tent.model;

public interface BasecampResponse<E extends RemoteObject> {

	void onResponse(E response);

}
