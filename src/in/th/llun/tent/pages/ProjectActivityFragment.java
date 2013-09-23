package in.th.llun.tent.pages;

import in.th.llun.tent.ProjectActivity;
import in.th.llun.tent.model.Project;
import in.th.llun.tent.remote.Tent;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ProjectActivityFragment extends Fragment {

	protected Tent mTent;
	protected Project mProject;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

		mTent = Tent.getInstance();

		String rawProject = getArguments().getString(ProjectActivity.EXTRA_PROJECT);
		try {
			mProject = new Project(new JSONObject(rawProject));

		} catch (JSONException e) {
			Log.e(Tent.LOG_TAG, "Can't parse project raw string", e);
		}

		return createProjectFragmentView(inflater, container, savedInstanceState);
	}

	protected abstract View createProjectFragmentView(LayoutInflater inflater,
	    ViewGroup container, Bundle savedInstance);

}
