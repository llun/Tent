package in.th.llun.tent;

import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Document;
import in.th.llun.tent.remote.Tent;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

public class DocumentActivity extends Activity {

	public static final String EXTRA_DOCUMENT = "document";

	private Tent mTent;
	private Document mDocument;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTent = Tent.getInstance(getApplicationContext());
		if (!mTent.isLoggedIn()) {
			Intent loginPage = new Intent(this, LoginActivity.class);
			startActivity(loginPage);
		} else {
			setContentView(R.layout.activity_document);

			ActionBar actionBar = getActionBar();
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);

			String rawDocument = getIntent().getStringExtra(EXTRA_DOCUMENT);
			try {
				mDocument = new Document(new JSONObject(rawDocument));
			} catch (JSONException e) {
				Log.e(Tent.LOG_TAG, "Can't parse raw project or document", e);
				finish();
				return;
			}

			EditText titleText = (EditText) findViewById(R.id.documentTitle);
			titleText.setText(mDocument.getTitle());

			final EditText contentText = (EditText) findViewById(R.id.documentContent);
			contentText.setText(getString(R.string.loading));
			contentText.setEnabled(false);

			mTent.getDocument(mDocument.getUrl(), new BasecampResponse<Document>() {

				@Override
				public void onResponse(Document response) {
					contentText.setText(Html.fromHtml(response.getContent()));
					contentText.setEnabled(true);
				}
			});

		}

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

}
