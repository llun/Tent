package in.th.llun.tent.pages;

import in.th.llun.tent.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ProjectDiscussionFragment extends ProjectActivityFragment {

	@Override
	protected View createProjectFragmentView(LayoutInflater inflater,
	    ViewGroup container, Bundle savedInstance) {

		View rootView = inflater.inflate(R.layout.fragment_project_discussion,
		    container, false);

		GridView discussionGrid = (GridView) rootView
		    .findViewById(R.id.discussionGrid);
		discussionGrid
		    .setAdapter(new DiscussionsAdapter(mTent, inflater, mProject));

		return rootView;
	}

}
