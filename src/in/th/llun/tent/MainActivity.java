package in.th.llun.tent;

import in.th.llun.tent.component.EventsAdapter;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Event;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.remote.Tent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity {

	private Tent mTent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTent = Tent.getInstance(getApplicationContext());
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (!mTent.isLoggedIn()) {
			showLoginPage();
		} else {
			showMainPage();
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void showLoginPage() {
		Intent loginPage = new Intent(this, LoginActivity.class);
		startActivity(loginPage);
	}

	private void showMainPage() {
		setContentView(R.layout.activity_main_progress);

		SlidingMenu menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setFadeDegree(0.35f);
		menu.setBehindOffsetRes(R.dimen.behide_menu_offset);
		menu.setMenu(R.layout.main_menu);

		mTent.loadEvents(new BasecampResponse<RemoteCollection<Event>>() {

			@Override
			public void onResponse(RemoteCollection<Event> response) {
				EventsAdapter adapter = new EventsAdapter(response.collection(),
				    getLayoutInflater());
				GridView eventGrid = (GridView) findViewById(R.id.eventGrid);
				eventGrid.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		});
	}

}
