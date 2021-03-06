package in.th.llun.tent.pages;

import in.th.llun.tent.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class ProjectProgressFragment extends ProjectActivityFragment {

	@Override
	protected View createProjectFragmentView(LayoutInflater inflater,
	    ViewGroup container, Bundle savedInstance) {
		View rootView = inflater.inflate(R.layout.fragment_project_progress,
		    container, false);
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
