package in.th.llun.tent.remote;

public interface ApiResponse<E extends Object> {

	void onError(Exception exception);

	void onResponse(E result);

}