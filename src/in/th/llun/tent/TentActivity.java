package in.th.llun.tent;

import in.th.llun.tent.remote.Tent;
import android.app.Activity;
import android.os.Bundle;

public class TentActivity extends Activity {

	private Tent mTent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTent = Tent.getInstance(getApplicationContext());

		if (!mTent.isLoggedIn()) {
			showLoginPage();
		} else {
			showMainPage();
		}

	}

	private void showLoginPage() {
		setContentView(R.layout.activity_login);
	}

	private void showMainPage() {
		setContentView(R.layout.activity_tent);
	}

}
