package in.th.llun.tent.pages;

import in.th.llun.tent.ProjectActivity;
import in.th.llun.tent.R;
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
import android.widget.GridView;
import android.widget.TextView;

public class ProjectProgressFragment extends Fragment {

	private Tent mTent;
	private Project mProject;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

		mTent = Tent.getInstance();

		final View rootView = inflater.inflate(R.layout.fragment_project_progress,
		    container, false);

		String rawProject = getArguments().getString(ProjectActivity.EXTRA_PROJECT);
		try {
			mProject = new Project(new JSONObject(rawProject));

		} catch (JSONException e) {
			Log.e(Tent.LOG_TAG, "Can't parse project raw string", e);
		}

		TextView projectName = (TextView) rootView.findViewById(R.id.projectName);
		projectName.setText(mProject.getName());

		TextView projectDescription = (TextView) rootView
		    .findViewById(R.id.projectDescription);
		projectDescription.setText(mProject.getDescription());

		GridView eventGrid = (GridView) rootView.findViewById(R.id.eventGrid);
		eventGrid.setAdapter(new EventsAdapter(mTent, inflater, mProject));

		return rootView;
	}
}
