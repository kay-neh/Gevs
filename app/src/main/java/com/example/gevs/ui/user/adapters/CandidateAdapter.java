package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gevs.R;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.util.Constants;

import java.util.HashMap;
import java.util.List;

public class CandidateAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Candidate>> expandableListDetail;

    public CandidateAdapter(Context context, List<String> expandableListTitle, HashMap<String, List<Candidate>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_group_party, null);
        }

        TextView partyName = convertView.findViewById(R.id.admin_party_name);
        partyName.setText(listTitle);

        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition, true);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Candidate expandedListCandidate = (Candidate) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_child_candidates, null);
        }

        ImageView candidateImage = convertView.findViewById(R.id.admin_candidate_imageView);
        TextView candidateName = convertView.findViewById(R.id.admin_candidate_name);
        TextView candidateConstituency = convertView.findViewById(R.id.admin_candidate_constituency);
        ImageView partyImage = convertView.findViewById(R.id.admin_party_imageView);

        if (expandedListCandidate.getParty().equals(Constants.PARTY_BLUE)) {
            partyImage.setImageResource(R.drawable.blue);
        } else if (expandedListCandidate.getParty().equals(Constants.PARTY_RED)) {
            partyImage.setImageResource(R.drawable.red);
        } else if (expandedListCandidate.getParty().equals(Constants.PARTY_YELLOW)) {
            partyImage.setImageResource(R.drawable.yellow);
        } else {
            partyImage.setImageResource(R.drawable.independent);
        }

        Glide.with(candidateImage.getContext()).load(expandedListCandidate.getPhoto()).into(candidateImage);
        candidateName.setText(expandedListCandidate.getName());
        candidateConstituency.setText(expandedListCandidate.getConstituency());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
