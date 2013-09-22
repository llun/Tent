package in.th.llun.tent;

import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.People;
import in.th.llun.tent.pages.ProgressFragment;
import in.th.llun.tent.pages.ProjectFragment;
import in.th.llun.tent.remote.Tent;
import in.th.llun.tent.tools.ImageLoader;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static int PAGE_PROGRESS = 0;
	public static int PAGE_PROJECTS = 1;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerMenu;
	private Tent mTent;

	private Fragment mPages[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPages = new Fragment[] { new ProgressFragment(), new ProjectFragment() };
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
			mDrawerMenu.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
				    long id) {
					if (position > 0) {
						selectPage(position - 1);
					}
				}
			});

			selectPage(PAGE_PROGRESS);
		}

	}

	private void selectPage(int position) {
		if (position >= mPages.length)
			return;
		Fragment fragment = mPages[position];
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

		public static class NormalRow {
			final public String mName;
			final public int mImageResource;
			final public int mBackgroundResource;

			public NormalRow(String name, int imageResource) {
				this(name, imageResource, 0);
			}

			public NormalRow(String name, int imageResource, int backgroundResource) {
				mName = name;
				mImageResource = imageResource;
				mBackgroundResource = backgroundResource;
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
			mMenus.add(new NormalRow(mResources.getString(R.string.menu_progress),
			    R.drawable.ic_progress));
			mMenus.add(new NormalRow(
			    mResources.getString(R.string.menu_all_projects),
			    R.drawable.ic_project));
			mMenus.add(new NormalRow(mResources.getString(R.string.menu_calendars),
			    R.drawable.ic_calendar));
			mMenus.add(new NormalRow(mResources.getString(R.string.menu_discussions),
			    R.drawable.ic_discussions));
			mMenus.add(new NormalRow(mResources.getString(R.string.menu_notes),
			    R.drawable.ic_note));
			mMenus.add(new NormalRow(mResources.getString(R.string.menu_settings),
			    R.drawable.ic_settings, R.drawable.menu_divider_row_background));
			mMenus.add(new NormalRow(mResources.getString(R.string.menu_logout),
			    R.drawable.ic_logout));

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
			}
			// Other normal menu
			else {
				View otherRow = convertView;
				if (otherRow == null) {
					otherRow = mInflater.inflate(R.layout.menu_icon_name_row, parent,
					    false);
				}

				NormalRow row = mMenus.get(position - 1);
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

}
