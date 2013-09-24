package in.th.llun.tent.pages;

import in.th.llun.tent.R;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.model.Topic;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProjectDiscussionFragment extends ProjectActivityFragment {

	@Override
	protected View createProjectFragmentView(LayoutInflater inflater,
	    ViewGroup container, Bundle savedInstance) {

		mTent.loadTopics(mProject, new BasecampResponse<RemoteCollection<Topic>>() {

			@Override
			public void onResponse(RemoteCollection<Topic> response) {
				// TODO Auto-generated method stub

			}
		});

		View view = inflater.inflate(R.layout.fragment_project_discussion,
		    container, false);
		return view;
	}

}
