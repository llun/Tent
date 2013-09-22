package in.th.llun.tent.pages;

import in.th.llun.tent.R;
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

		mTent = Tent.getInstance();

		final View rootView = inflater.inflate(R.layout.fragment_progress,
		    container, false);
		GridView eventGrid = (GridView) rootView.findViewById(R.id.eventGrid);
		eventGrid.setAdapter(new EventsAdapter(mTent, inflater));
		return rootView;
	}
}
