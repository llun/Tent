package in.th.llun.tent.component;

import in.th.llun.tent.ImageLoader;
import in.th.llun.tent.R;
import in.th.llun.tent.model.Event;

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

	private List<Event> mEvents;
	private LayoutInflater mLayoutInflater;

	public EventsAdapter(List<Event> events, LayoutInflater layoutInflater) {
		mEvents = events;
		mLayoutInflater = layoutInflater;
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

}
