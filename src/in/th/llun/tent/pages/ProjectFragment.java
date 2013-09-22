package in.th.llun.tent.pages;

import in.th.llun.tent.ProjectActivity;
import in.th.llun.tent.R;
import in.th.llun.tent.model.BasecampResponse;
import in.th.llun.tent.model.Project;
import in.th.llun.tent.model.RemoteCollection;
import in.th.llun.tent.remote.Tent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProjectFragment extends Fragment {

	private Tent mTent;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

		mTent = Tent.getInstance();

		final View rootView = inflater.inflate(R.layout.fragment_projects,
		    container, false);
		ListView projectList = (ListView) rootView.findViewById(R.id.projectList);
		projectList.setAdapter(new ProjectAdapter(mTent, getResources(), inflater));
		projectList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
			    long id) {
				Project project = (Project) parent.getItemAtPosition(position);

				Intent projectIntent = new Intent(getActivity(), ProjectActivity.class);
				projectIntent.putExtra(ProjectActivity.EXTRA_PROJECT,
				    project.rawString());
				startActivity(projectIntent);
			}
		});

		return rootView;
	}

	private static class ProjectAdapter extends BaseAdapter {

		private LayoutInflater mLayoutInflater;
		private Resources mResources;
		private Tent mTent;

		private List<Project> mProjects;

		public ProjectAdapter(Tent tent, Resources resources,
		    LayoutInflater inflater) {
			mLayoutInflater = inflater;
			mResources = resources;
			mTent = tent;

			mProjects = new ArrayList<Project>();
			mTent.loadProjects(new BasecampResponse<RemoteCollection<Project>>() {

				@Override
				public void onResponse(RemoteCollection<Project> response) {
					Collections.sort(response.collection(), new Comparator<Project>() {

						@Override
						public int compare(Project lhs, Project rhs) {
							int lhsStar = lhs.isStarred() ? 0 : 1;
							int rhsStar = rhs.isStarred() ? 0 : 1;
							int compareStar = lhsStar - rhsStar;
							if (compareStar == 0) {
								return lhs.getName().compareTo(rhs.getName());
							}
							return compareStar;
						}
					});
					mProjects = response.collection();
					notifyDataSetChanged();
				}
			});
		}

		@Override
		public int getCount() {
			return mProjects.size();
		}

		@Override
		public Object getItem(int position) {
			return mProjects.get(position);
		}

		@Override
		public long getItemId(int position) {
			return Long.parseLong(mProjects.get(position).getId());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				row = mLayoutInflater.inflate(R.layout.grid_project, parent, false);
			}

			Project project = mProjects.get(position);
			TextView projectName = (TextView) row.findViewById(R.id.projectName);
			projectName.setText(project.getName());

			TextView projectUpdatedTime = (TextView) row
			    .findViewById(R.id.projectUpdatedTime);
			projectUpdatedTime.setText(String.format(
			    mResources.getString(R.string.project_last_updated),
			    new PrettyTime().format(project.getUpdatedAt())));

			ImageView starIcon = (ImageView) row.findViewById(R.id.projectStar);
			if (project.isStarred()) {
				starIcon.setVisibility(View.VISIBLE);
			} else {
				starIcon.setVisibility(View.INVISIBLE);
			}

			return row;
		}

	}
}
