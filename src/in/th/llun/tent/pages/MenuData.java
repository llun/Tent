package in.th.llun.tent.pages;

public class MenuData {
	final public String mName;
	final public int mImageResource;
	final public int mBackgroundResource;

	public MenuData(String name, int imageResource) {
		this(name, imageResource, 0);
	}

	public MenuData(String name, int imageResource, int backgroundResource) {
		mName = name;
		mImageResource = imageResource;
		mBackgroundResource = backgroundResource;
	}
}
