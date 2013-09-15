package in.th.llun.tent.model;

import java.util.List;

public class RemoteCollection<E extends RemoteObject> implements RemoteObject {

	protected List<E> mRaws;

	public RemoteCollection(List<E> raws) {
		mRaws = raws;
	}

	public List<E> collection() {
		return mRaws;
	}

	public String rawString() {
		StringBuilder builder = new StringBuilder("[");
		for (RemoteObject raw : mRaws) {
			builder.append(String.format("%s,", raw));
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("]");
		return builder.toString();
	}

}