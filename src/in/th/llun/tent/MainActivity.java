package in.th.llun.tent;

import in.th.llun.tent.pages.MenuAdapter;
import in.th.llun.tent.pages.MenuData;
import in.th.llun.tent.pages.ProgressFragment;
import in.th.llun.tent.pages.ProjectFragment;
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

	}

	public void onStart() {
		super.onStart();

		if (!mTent.isLoggedIn()) {
			Intent loginPage = new Intent(this, LoginActivity.class);
			startActivity(loginPage);
		} else {
			setContentView(R.layout.activity_main);

			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			    R.drawable.ic_drawer, R.string.menu_drawer_open,
			    R.string.menu_drawer_close);

			ArrayList<MenuData> menus = new ArrayList<MenuData>();
			menus.add(new MenuData(getString(R.string.menu_progress),
			    R.drawable.ic_progress));
			menus.add(new MenuData(getString(R.string.menu_all_projects),
			    R.drawable.ic_project));
			menus.add(new MenuData(getString(R.string.menu_calendars),
			    R.drawable.ic_calendar));
			menus.add(new MenuData(getString(R.string.menu_discussions),
			    R.drawable.ic_discussions));
			menus
			    .add(new MenuData(getString(R.string.menu_notes), R.drawable.ic_note));
			menus.add(new MenuData(getString(R.string.menu_settings),
			    R.drawable.ic_settings, R.drawable.menu_divider_row_background));
			menus.add(new MenuData(getString(R.string.menu_logout),
			    R.drawable.ic_logout));

			mDrawerMenu = (ListView) findViewById(R.id.leftDrawer);
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
		fragmentManager.beginTransaction().replace(R.id.contentFrame, fragment)
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
		if (mDrawerToggle != null) {
			mDrawerToggle.syncState();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
