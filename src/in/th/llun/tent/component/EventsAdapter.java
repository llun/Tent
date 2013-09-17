package in.th.llun.tent.component;

import in.th.llun.tent.model.Event;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
			
		}
		return row;
	}

}
