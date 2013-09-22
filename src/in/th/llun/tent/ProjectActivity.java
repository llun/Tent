package in.th.llun.tent;

import in.th.llun.tent.pages.MenuAdapter;
import in.th.llun.tent.pages.MenuAdapter.MenuData;
import in.th.llun.tent.pages.ProjectProgressFragment;
import in.th.llun.tent.remote.Tent;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ProjectActivity extends Activity {

	public static final String EXTRA_PROJECT = "project";

	public static final int PAGE_PROGRESS = 0;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerMenu;
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
			setContentView(R.layout.activity_main);

			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			ArrayList<MenuAdapter.MenuData> menus = new ArrayList<MenuAdapter.MenuData>();
			menus.add(new MenuData(getString(R.string.menu_progress),
			    R.drawable.ic_progress));
			menus.add(new MenuData(getString(R.string.menu_calendars),
			    R.drawable.ic_calendar));
			menus.add(new MenuData(getString(R.string.menu_discussions),
			    R.drawable.ic_discussions));
			menus
			    .add(new MenuData(getString(R.string.menu_notes), R.drawable.ic_note));

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			    R.drawable.ic_drawer, R.string.menu_drawer_open,
			    R.string.menu_drawer_close);
			mDrawerMenu = (ListView) findViewById(R.id.left_drawer);
			mDrawerMenu.setAdapter(new MenuAdapter(getLayoutInflater(), menus));
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

}
