package in.th.llun.tent;

import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Event;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.remote.Tent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

		mTent.loadEvents(new BasecampResponse<RemoteCollection<Event>>() {

			@Override
			public void onResponse(RemoteCollection<Event> response) {
				
			}
		});
	}

}
