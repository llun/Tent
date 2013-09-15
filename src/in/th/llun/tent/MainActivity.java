package in.th.llun.tent;

import in.th.llun.tent.remote.Tent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

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

	@SuppressLint("SetJavaScriptEnabled")
	private void showLoginPage() {
		setContentView(R.layout.activity_main_login);

		final Activity activity = this;
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent loginPage = new Intent(activity, LoginActivity.class);
				activity.startActivity(loginPage);
			}
		});
	}

	private void showMainPage() {
		setContentView(R.layout.activity_main);
	}

}
