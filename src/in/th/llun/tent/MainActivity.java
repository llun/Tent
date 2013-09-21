package in.th.llun.tent;

import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.People;
import in.th.llun.tent.remote.Tent;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerMenu;
	private Tent mTent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTent = Tent.getInstance(getApplicationContext());
		if (!mTent.isLoggedIn()) {
			Intent loginPage = new Intent(this, LoginActivity.class);
			startActivity(loginPage);
		} else {
			setContentView(R.layout.activity_main);

			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			    R.drawable.ic_drawer, R.string.menu_drawer_open,
			    R.string.menu_drawer_close);
			mDrawerMenu = (ListView) findViewById(R.id.left_drawer);
			mDrawerMenu.setAdapter(new MenuAdapter(getResources(),
			    getLayoutInflater()));
			selectPage(0);
		}

	}

	private void selectPage(int position) {
		Fragment fragment = new ProgressFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
		    .commit();

		mDrawerLayout.closeDrawer(mDrawerMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private static class MenuAdapter extends BaseAdapter {

		private static class NormalRow {
			final public String mName;
			final public int mImageResource;

			public NormalRow(String name, int imageResource) {
				mName = name;
				mImageResource = imageResource;
			}
		}

		private People mPeople;
		private ArrayList<NormalRow> mMenus;

		private LayoutInflater mInflater;
		private Resources mResources;
		private Tent mTent;

		public MenuAdapter(Resources resources, LayoutInflater inflater) {
			mInflater = inflater;
			mResources = resources;
			mTent = Tent.getInstance();

			mMenus = new ArrayList<NormalRow>();
			mMenus.add(new NormalRow(
			    mResources.getString(R.string.menu_all_projects),
			    R.drawable.ic_project));
		}

		@Override
		public int getCount() {
			// user + project
			return 2;
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
			switch (position) {
			case 0:
				View userRow = convertView;
				if (userRow == null) {
					userRow = mInflater.inflate(R.layout.menu_user_information_row,
					    parent, false);
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
			default:
				View otherRow = convertView;
				if (otherRow == null) {
					otherRow = mInflater.inflate(R.layout.menu_icon_name_row, parent,
					    false);
				}

				NormalRow row = mMenus.get(position - 1);

				ImageView icon = (ImageView) otherRow.findViewById(R.id.icon);
				icon.setImageResource(row.mImageResource);

				TextView name = (TextView) otherRow.findViewById(R.id.name);
				name.setText(row.mName);

				return otherRow;
			}
		}

		public int getItemViewType(int position) {
			switch (position) {
			case 0:
				return 0;
			default:
				return 1;
			}
		}

		public int getViewTypeCount() {
			// First row is profile
			return 1;
		}

		public void updatePeople(People people, View menu) {
			TextView username = (TextView) menu.findViewById(R.id.userName);
			username.setText(mPeople.getName());

			new ImageLoader((ImageView) menu.findViewById(R.id.userAvatar),
			    (ViewGroup) menu.findViewById(R.id.loadingView)).execute(mPeople
			    .getAvatarUrl());
		}

	}

}
