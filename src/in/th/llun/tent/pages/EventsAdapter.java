package in.th.llun.tent.pages;

import in.th.llun.tent.R;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Event;
import in.th.llun.tent.model.Project;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.remote.Tent;
import in.th.llun.tent.tools.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter {

	private Tent mTent;
	private List<Event> mEvents;
	private LayoutInflater mLayoutInflater;
	private Project mProject;

	public EventsAdapter(Tent tent, LayoutInflater layoutInflater, Project project) {
		mTent = tent;
		mEvents = new ArrayList<Event>();
		mLayoutInflater = layoutInflater;
		mProject = project;

		if (mProject != null) {
			mTent.loadProjectEvents(mProject, new EventsResponse());
		} else {
			mTent.loadEvents(new EventsResponse());
		}
	}

	public EventsAdapter(Tent tent, LayoutInflater layoutInflater) {
		this(tent, layoutInflater, null);
	}

	@Override
	public int getCount() {
		return mEvents.size();
	}

	@Override
	public Object getItem(int position) {
		return mEvents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(mEvents.get(position).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = mLayoutInflater.inflate(R.layout.grid_event, parent, false);
		}

		Event event = mEvents.get(position);

		ImageView avatarImageView = (ImageView) row
		    .findViewById(R.id.creatorAvatar);
		ViewGroup avatarLoadingView = (ViewGroup) row
		    .findViewById(R.id.loadingView);
		avatarLoadingView.setVisibility(View.VISIBLE);
		new ImageLoader(avatarImageView, avatarLoadingView).execute(event
		    .getCreator().getAvatarUrl());

		TextView title = (TextView) row.findViewById(R.id.eventTitle);
		title.setText(Html.fromHtml(String.format("%s %s %s", event.getCreator()
		    .getName(), event.getAction(), event.getTarget())));

		TextView timestamp = (TextView) row.findViewById(R.id.eventTime);
		timestamp.setText(new PrettyTime().format(event.getCreatedAt()));

		return row;
	}

	private final class EventsResponse implements
	    BasecampResponse<RemoteCollection<Event>> {

		@Override
		public void onResponse(RemoteCollection<Event> response) {
			mEvents.addAll(response.collection());
			notifyDataSetChanged();
		}

	}

}
