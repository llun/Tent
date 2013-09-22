package in.th.llun.tent.pages;

import in.th.llun.tent.R;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Event;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.remote.Tent;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ProgressFragment extends Fragment {

	private Tent mTent;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

		mTent = Tent.getInstance(getActivity().getApplicationContext());

		final View rootView = inflater.inflate(R.layout.fragment_progress,
		    container, false);
		mTent.loadEvents(new BasecampResponse<RemoteCollection<Event>>() {

			@Override
			public void onResponse(RemoteCollection<Event> response) {
				EventsAdapter adapter = new EventsAdapter(response.collection(),
				    inflater);
				GridView eventGrid = (GridView) rootView.findViewById(R.id.eventGrid);
				eventGrid.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		});
		return rootView;
	}
}
