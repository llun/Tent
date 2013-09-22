package in.th.llun.tent;

import in.th.llun.tent.pages.MenuData;
import in.th.llun.tent.pages.ProjectProgressFragment;
import in.th.llun.tent.remote.Tent;

import java.util.ArrayList;
import java.util.List;

import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ProjectActivity extends Activity {

	public static final String EXTRA_PROJECT = "project";

	public static final int PAGE_PROGRESS = 0;

	private Tent mTent;

	private Fragment mPages[];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPages = new Fragment[] { new ProjectProgressFragment() };
		mTent = Tent.getInstance(getApplicationContext());
		if (!mTent.isLoggedIn()) {
			Intent loginPage = new Intent(this, LoginActivity.class);
			startActivity(loginPage);
		} else {
			setContentView(R.layout.activity_project);

			ArrayList<MenuData> menus = new ArrayList<MenuData>();
			menus.add(new MenuData(getString(R.string.menu_progress),
			    R.drawable.ic_progress, R.drawable.menu_row_background));
			menus.add(new MenuData(getString(R.string.menu_calendars),
			    R.drawable.ic_calendar, R.drawable.menu_row_background));
			menus.add(new MenuData(getString(R.string.menu_discussions),
			    R.drawable.ic_discussions, R.drawable.menu_row_background));
			menus.add(new MenuData(getString(R.string.menu_notes),
			    R.drawable.ic_note, R.drawable.menu_row_background));

			ActionBar actionBar = getActionBar();
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setListNavigationCallbacks(new ProjectMenuAdapter(
			    getLayoutInflater(), menus), new ActionBar.OnNavigationListener() {
				@Override
				public boolean onNavigationItemSelected(int itemPosition, long itemId) {
					return false;
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
		fragmentManager.beginTransaction().replace(R.id.project_content, fragment)
		    .commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private static class ProjectMenuAdapter extends BaseAdapter implements
	    SpinnerAdapter {

		private LayoutInflater mLayoutInflater;
		private List<MenuData> mMenus;

		public ProjectMenuAdapter(LayoutInflater inflater, List<MenuData> menus) {
			mLayoutInflater = inflater;
			mMenus = menus;
		}

		@Override
		public int getCount() {
			return mMenus.size();
		}

		@Override
		public Object getItem(int position) {
			return mMenus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView row = (TextView) convertView;
			if (row == null) {
				row = new TextView(mLayoutInflater.getContext());
			}

			MenuData data = mMenus.get(position);
			row.setText(data.mName);

			return row;
		}

		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				row = mLayoutInflater.inflate(R.layout.menu_icon_name_row, parent,
				    false);
			}

			MenuData data = mMenus.get(position);
			row.setTag(position);

			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			icon.setImageResource(data.mImageResource);

			TextView name = (TextView) row.findViewById(R.id.name);
			name.setText(data.mName);

			row.setBackgroundResource(data.mBackgroundResource);
			return row;
		}
	}
}
