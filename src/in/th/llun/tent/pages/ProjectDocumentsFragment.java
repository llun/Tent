package in.th.llun.tent.pages;

import in.th.llun.tent.DocumentActivity;
import in.th.llun.tent.R;
import in.th.llun.tent.model.Document;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class ProjectDocumentsFragment extends ProjectActivityFragment {

	@Override
	protected View createProjectFragmentView(LayoutInflater inflater,
	    ViewGroup container, Bundle savedInstance) {
		View rootView = inflater.inflate(R.layout.fragment_project_documents,
		    container, false);
		TextView projectName = (TextView) rootView.findViewById(R.id.projectName);
		projectName.setText(mProject.getName());

		TextView projectDescription = (TextView) rootView
		    .findViewById(R.id.projectDescription);
		projectDescription.setText(mProject.getDescription());

		GridView documentGrid = (GridView) rootView.findViewById(R.id.documentGrid);
		documentGrid.setAdapter(new DocumentsAdapter(mTent, inflater, mProject));
		documentGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
			    long id) {
				Document document = (Document) parent.getItemAtPosition(position);

				Intent intent = new Intent(getActivity(), DocumentActivity.class);
				intent.putExtra(DocumentActivity.EXTRA_DOCUMENT, document.rawString());

				getActivity().startActivity(intent);
			}
		});

		return rootView;
	}
}
