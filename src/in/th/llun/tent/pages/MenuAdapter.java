package in.th.llun.tent.pages;

import in.th.llun.tent.R;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.People;
import in.th.llun.tent.remote.Tent;
import in.th.llun.tent.tools.ImageLoader;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {

	public static class MenuData {
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

	private People mPeople;
	private List<MenuData> mMenus;

	private LayoutInflater mInflater;
	private Tent mTent;

	public MenuAdapter(LayoutInflater inflater,
	    List<MenuData> menus) {
		mInflater = inflater;
		mTent = Tent.getInstance();
		mMenus = menus;
	}

	@Override
	public int getCount() {
		// user + menus
		return mMenus.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {
			View userRow = convertView;
			if (userRow == null) {
				userRow = mInflater.inflate(R.layout.menu_user_information_row, parent,
				    false);
			}

			if (mPeople == null) {
				final View fUserRow = userRow;
				mTent.me(new BasecampResponse<People>() {

					@Override
					public void onResponse(People response) {
						mPeople = response;
						updatePeople(mPeople, fUserRow);
					}
				});
			} else {
				updatePeople(mPeople, userRow);
			}

			return userRow;
		}
		// Other normal menu
		else {
			View otherRow = convertView;
			if (otherRow == null) {
				otherRow = mInflater
				    .inflate(R.layout.menu_icon_name_row, parent, false);
			}

			MenuData row = mMenus.get(position - 1);
			otherRow.setTag(position - 1);

			ImageView icon = (ImageView) otherRow.findViewById(R.id.icon);
			icon.setImageResource(row.mImageResource);

			TextView name = (TextView) otherRow.findViewById(R.id.name);
			name.setText(row.mName);

			otherRow.setBackgroundResource(row.mBackgroundResource);

			return otherRow;
		}
	}

	public int getItemViewType(int position) {
		// User profile
		if (position == 0) {
			return 0;
		}
		// Other normal menu
		else {
			return 1;
		}
	}

	public int getViewTypeCount() {
		return 2;
	}

	public void updatePeople(People people, View menu) {
		TextView username = (TextView) menu.findViewById(R.id.userName);
		username.setText(mPeople.getName());

		new ImageLoader((ImageView) menu.findViewById(R.id.userAvatar),
		    (ViewGroup) menu.findViewById(R.id.loadingView)).execute(mPeople
		    .getAvatarUrl());
	}

}
