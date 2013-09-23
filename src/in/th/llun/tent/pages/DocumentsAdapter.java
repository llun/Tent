package in.th.llun.tent.pages;

import in.th.llun.tent.R;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Document;
import in.th.llun.tent.model.Project;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.remote.Tent;

import java.util.ArrayList;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DocumentsAdapter extends BaseAdapter {

	private Tent mTent;
	private List<Document> mDocuments;
	private LayoutInflater mLayoutInflater;
	private Project mProject;

	public DocumentsAdapter(Tent tent, LayoutInflater layoutInflater,
	    Project project) {
		mTent = tent;
		mDocuments = new ArrayList<Document>();
		mLayoutInflater = layoutInflater;
		mProject = project;

		if (mProject != null) {
			mTent.loadProjectDocuments(mProject, new DocumentsResponse());
		} else {
		}
	}

	public DocumentsAdapter(Tent tent, LayoutInflater layoutInflater) {
		this(tent, layoutInflater, null);
	}

	@Override
	public int getCount() {
		return mDocuments.size();
	}

	@Override
	public Object getItem(int position) {
		return mDocuments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(mDocuments.get(position).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			row = mLayoutInflater.inflate(R.layout.grid_document, parent, false);
		}

		Document document = mDocuments.get(position);

		TextView titleTextView = (TextView) row.findViewById(R.id.title);
		TextView updatedTimeTextView = (TextView) row
		    .findViewById(R.id.updatedTime);

		titleTextView.setText(document.getTitle());
		updatedTimeTextView
		    .setText(new PrettyTime().format(document.getUpdatedAt()));

		return row;
	}

	private final class DocumentsResponse implements
	    BasecampResponse<RemoteCollection<Document>> {

		@Override
		public void onResponse(RemoteCollection<Document> response) {
			mDocuments.addAll(response.collection());
			notifyDataSetChanged();
		}

	}
}
