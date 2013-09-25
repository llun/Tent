package in.th.llun.tent.pages;

import in.th.llun.tent.R;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Project;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.model.Topic;
import in.th.llun.tent.remote.Tent;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DiscussionsAdapter extends BaseAdapter {

	private Tent mTent;
	private List<Topic> mTopics;
	private LayoutInflater mLayoutInflater;
	private Project mProject;

	public DiscussionsAdapter(Tent tent, LayoutInflater layoutInflater,
	    Project project) {
		mTent = tent;
		mTopics = new ArrayList<Topic>();
		mLayoutInflater = layoutInflater;
		mProject = project;

		mTent.loadTopics(mProject, new TopicsResponse());
	}

	public DiscussionsAdapter(Tent tent, LayoutInflater layoutInflater) {
		this(tent, layoutInflater, null);
	}

	@Override
	public int getCount() {
		return mTopics.size();
	}

	@Override
	public Object getItem(int position) {
		return mTopics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(mTopics.get(position).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = mLayoutInflater.inflate(R.layout.grid_topic, parent, false);
		}

		Topic topic = mTopics.get(position);

		TextView title = (TextView) row.findViewById(R.id.title);
		title.setText(topic.getTitle());

		TextView excerpt = (TextView) row.findViewById(R.id.excerpt);
		excerpt.setText(topic.getExcerpt());

		return row;
	}

	private final class TopicsResponse implements
	    BasecampResponse<RemoteCollection<Topic>> {

		@Override
		public void onResponse(RemoteCollection<Topic> response) {
			mTopics.addAll(response.collection());
			notifyDataSetChanged();
		}

	}

}
